package com.hcmus.lovelybackend.service.impl;

import com.hcmus.lovelybackend.constant.TransactionType;
import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.common.AuObjectCommonResponse;
import com.hcmus.lovelybackend.entity.common.TokenUser;
import com.hcmus.lovelybackend.entity.dao.*;
import com.hcmus.lovelybackend.entity.request.BidProductRequest;
import com.hcmus.lovelybackend.entity.request.ReviewWinnerRequest;
import com.hcmus.lovelybackend.entity.response.AuEvaluationResponse;
import com.hcmus.lovelybackend.entity.response.AuPageResponse;
import com.hcmus.lovelybackend.exception.runtime.*;
import com.hcmus.lovelybackend.repository.*;
import com.hcmus.lovelybackend.service.IProductBidderService;
import com.hcmus.lovelybackend.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductBidderService implements IProductBidderService {

    @Autowired
    private ProductBidderRepository productBidderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private TemporaryBidderRepository temporaryBidderRepository;

    @Autowired
    private AutoBidListRepository autoBidListRepository;

    @Autowired
    SimpMessagingTemplate template;

    @Override
    public List<ProductBidder> getTop5MostBids() {
        return (List<ProductBidder>) productBidderRepository.findAll();
    }

    @Scheduled(fixedDelay = 60000)
    public void scheduleTaskToAutoBid() {
        log.info("Handle auto bid product");
        List<AutoBidList> autoBidLists = autoBidListRepository.findAll();
        if (!autoBidLists.isEmpty()) {
            autoBidLists.forEach(a -> {
                if (a.getProduct().getEndAtTypeDateTime().isBefore(LocalDateTime.now())) {
                    autoBidListRepository.delete(a);
                } else {
                    Product product = productRepository.findById(a.getProduct().getId()).get();
                    if (a.getPrice() >= (product.getCurrentPrice() + product.getStepPrice()) && !a.getBidder().getId().equals(product.getBidderHighest().getId())) {
                        UserDAO user = userRepository.findById(a.getBidder().getId()).get();
                        BidProductRequest request = new BidProductRequest();
                        request.setPrice(product.getCurrentPrice() + product.getStepPrice());
                        ProductBidder newProductBid = handleBidOfBidder(product, user, request);
                        TextMessageDTO textMessageDTO = new TextMessageDTO();
                        textMessageDTO.setMessage(newProductBid);
                        template.convertAndSend("/topic/history", textMessageDTO);
                    }
                }
            });
        }
    }

    @Override
    public AuObjectCommonResponse saveBidOfBidder(String token, Integer productId, BidProductRequest request) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        UserDAO user = tokenUser.getUserDAO();
        ProductBidder newProductBid = null;
        Product product = productRepository.findAllById(productId).orElseThrow(() -> {
            throw new NotFoundException("Product Not Found !!!");
        });
        Set<ProductBidder> productBidders = productBidderRepository.findAllByProductIdAndBidderIdAndReject(productId, user.getId(), true);
        if (productBidders.size() != 0) {
            throw new BadRequestException("You have been refused to bid on this product.");
        }
        if (product.getSeller().getId().equals(user.getId())) {
            throw new BadRequestException("You cannot bid on this product.");
        }
        if (!LocalDateTime.now().isBefore(product.getEndAtTypeDateTime())) {
            throw new BadRequestException("The product has expired and cannot be bid.");
        }
        if (request.getPrice() < (product.getCurrentPrice() + product.getStepPrice())) {
            throw new BadRequestException("Invalid Price. Price should be more than " + (product.getCurrentPrice() + product.getStepPrice()));
        }
        Optional<TemporaryBidder> temporaryBidder = temporaryBidderRepository.findAllByProductIdAndBidderId(productId, tokenUser.getUserDAO().getId());
        if (temporaryBidder.isPresent()) {
            if (temporaryBidder.get().getStatus().equals(true)) {
                newProductBid = handleBidOfBidder(product, user, request);
                return new AuObjectCommonResponse(tokenUser.getToken(), newProductBid);
            }
        }
        Page<Transaction> transactions = transactionRepository.findAllByRecipientId(user.getId(), Pageable.unpaged());
        if (transactions.getTotalElements() == 0) {
            throw new BadRequestException("Unrated users cannot participate in the auction. Please request permission to bid on this product.");
        }
        AtomicInteger goodPoint = new AtomicInteger();
        transactions.forEach(transaction -> {
            goodPoint.getAndIncrement();
            if (!transaction.getIsLike()) {
                goodPoint.getAndDecrement();
            }
        });
        double point = (double) ((goodPoint.get() / transactions.getTotalElements()) * 100);
        if (point < 80) {
            throw new BadRequestException("Ops, Your rating is too low to participate in the auction. Please request permission to bid on this product.");
        }

        newProductBid = handleBidOfBidder(product, user, request);

        return new AuObjectCommonResponse(tokenUser.getToken(), newProductBid);
    }

    public ProductBidder handleBidOfBidder(Product product, UserDAO user, BidProductRequest request) {
        ProductBidder productBidder = new ProductBidder();
        productBidder.setBidder(user);
        productBidder.setProduct(product);
        productBidder.setPrice(request.getPrice());
        ProductBidder newProductBid = productBidderRepository.save(productBidder);
        UserDAO oldBidderHighest = product.getBidderHighest();
        product.setBidderHighest(user);
        product.setCurrentPrice(request.getPrice());
        if (product.getAutoBid()) {
            if (LocalDateTime.now().isAfter(product.getEndAtTypeDateTime().minusMinutes(5))
                    && LocalDateTime.now().isBefore(product.getEndAtTypeDateTime().plusMinutes(5))) {
                product.setEndAt(product.getEndAtTypeDateTime().plusMinutes(10));
            }
        }
        productRepository.save(product);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            appUtils.sendNotifictionEmail(product.getSeller(), "Your product has just had a new bid.", product);
            appUtils.sendNotifictionEmail(user, "You have successfully bid on a product.", product);
            if (oldBidderHighest != null && !oldBidderHighest.getId().equals(user.getId())) {
                appUtils.sendNotifictionEmail(oldBidderHighest, "There's just been a new auction for the product you hold the price for.", product);
            }
            executor.shutdown();
        });

        return newProductBid;
    }

    @Override
    public Page<ProductBidder> getListBidOfProduct(Integer productId, Integer page, Integer size) {
        productRepository.findAllById(productId).orElseThrow(() -> {
            throw new NotFoundException("Product Not Found !!!");
        });
        return productBidderRepository.findAllByProductIdOrderByPriceDesc(productId, PageRequest.of(page, size));
    }

    @Override
    public AuEvaluationResponse getEvaluationDetails(String token, Integer page, Integer size) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Page<Transaction> transactions = transactionRepository.findAllByRecipientId(tokenUser.getUserDAO().getId(), PageRequest.of(page, size));
        return new AuEvaluationResponse(tokenUser.getToken(), tokenUser.getUserDAO(), transactions);
    }

    @Override
    public AuPageResponse getProductsWin(String token, Integer page, Integer size) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Page<Product> products = productRepository.findAllByBidderHighestIdAndHaveWinner(tokenUser.getUserDAO().getId(), LocalDateTime.now(), PageRequest.of(page, size));
        return new AuPageResponse(tokenUser.getToken(), products);
    }

    @Override
    public AuPageResponse getListBidByBidder(String token, Integer page, Integer size) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Page<ProductBidder> productBidders = productBidderRepository.findAllByBidderId(tokenUser.getUserDAO().getId(), PageRequest.of(page, size));
        return new AuPageResponse(tokenUser.getToken(), productBidders);
    }

    @Override
    public AuMessageCommonResponse reviewSeller(String token, Integer sellerId, Integer productId, ReviewWinnerRequest request) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        UserDAO winner = tokenUser.getUserDAO();
        Product product = productRepository.findAllById(productId).orElseThrow(ProductNotFoundException::new);
        UserDAO seller = userRepository.findById(sellerId).orElseThrow(UserNotFoundException::new);

        LocalDateTime productEndAt = LocalDateTime.parse(product.getEndAt());
        if (!productEndAt.isBefore(LocalDateTime.now())) {
            throw new NoWinnerYetException();
        }
        if (!product.getBidderHighest().getId().equals(winner.getId())) {
            throw new UserNotFoundException("Winner does not match !!!");
        }
        if(transactionRepository.findAllByProductIdAndAssessorIdAndRecipientId(productId, tokenUser.getUserDAO().getId(), sellerId).isPresent()){
            throw new BadRequestException("You have already rated this seller.");
        }

        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setAssessor(winner);
        transaction.setRecipient(seller);
        transaction.setType(TransactionType.BID);
        transaction.setComment(request.getComment());
        transaction.setIsLike(request.getLike());

        Transaction newTransaction = transactionRepository.save(transaction);
        List<Transaction> transactions = transactionRepository.findAllByRecipientId(seller.getId());
        transactions.add(newTransaction);
        List<Transaction> transactionsLike = transactions.stream().filter(p -> p.getIsLike().equals(true)).collect(Collectors.toList());
        double point = transactionsLike.isEmpty() ? 0 : (double) ((transactionsLike.size()/transactions.size()) * 100);
        seller.setPoint(point);
        userRepository.save(seller);

        return new AuMessageCommonResponse(tokenUser.getToken(), "Winner " + winner.getName() + " evaluates seller " + seller.getName() + " successfully.");
    }

    @Override
    public AuObjectCommonResponse payNowProduct(String token, Integer productId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        UserDAO user = tokenUser.getUserDAO();
        Product product = productRepository.findAllById(productId).orElseThrow(() -> {
            throw new NotFoundException("Product Not Found !!!");
        });
        if (product.getQuickPrice() == null) {
            throw new BadRequestException("The product is not accepted for immediate purchase.");
        }
        Set<ProductBidder> productBidders = productBidderRepository.findAllByProductIdAndBidderIdAndReject(productId, user.getId(), true);
        if (productBidders.size() != 0) {
            throw new BadRequestException("You have been refused to bid on this product.");
        }
        if (product.getSeller().getId().equals(user.getId())) {
            throw new BadRequestException("You cannot bid on this product.");
        }
        if (!LocalDateTime.now().isBefore(product.getEndAtTypeDateTime())) {
            throw new BadRequestException("The product has expired and cannot be bid.");
        }
        ProductBidder productBidder = new ProductBidder();
        productBidder.setBidder(user);
        productBidder.setProduct(product);
        productBidder.setStatus(true);
        productBidder.setPrice(product.getQuickPrice());
        ProductBidder newProductBid = productBidderRepository.save(productBidder);
        product.setCurrentPrice(product.getQuickPrice());
        product.setEndAt(LocalDateTime.now().minusSeconds(10));
        product.setBidderHighest(user);
        UserDAO oldBidderHighest = product.getBidderHighest();
        productRepository.save(product);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            appUtils.sendNotifictionEmail(product.getSeller(), "Your product has just had a new bid.", product);
            appUtils.sendNotifictionEmail(user, "You have successfully bid on a product.", product);
            if (oldBidderHighest != null && !oldBidderHighest.getId().equals(user.getId())) {
                appUtils.sendNotifictionEmail(oldBidderHighest, "There's just been a new auction for the product you hold the price for.", product);
            }
            executor.shutdown();
        });

        return new AuObjectCommonResponse(tokenUser.getToken(), newProductBid);
    }

    @Override
    public AuObjectCommonResponse autoBid(String token, Integer productId, BidProductRequest request) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        UserDAO user = tokenUser.getUserDAO();
        ProductBidder newProductBid = null;
        Product product = productRepository.findAllById(productId).orElseThrow(() -> {
            throw new NotFoundException("Product Not Found !!!");
        });
        Set<ProductBidder> productBidders = productBidderRepository.findAllByProductIdAndBidderIdAndReject(productId, user.getId(), true);
        if (productBidders.size() != 0) {
            throw new BadRequestException("You have been refused to bid on this product.");
        }
        if (product.getSeller().getId().equals(user.getId())) {
            throw new BadRequestException("You cannot bid on this product.");
        }
        if (!LocalDateTime.now().isBefore(product.getEndAtTypeDateTime())) {
            throw new BadRequestException("The product has expired and cannot be bid.");
        }
        if (request.getPrice() < (product.getCurrentPrice() + product.getStepPrice())) {
            throw new BadRequestException("Invalid Price. Price should be more than " + (product.getCurrentPrice() + product.getStepPrice()));
        }
        Optional<TemporaryBidder> temporaryBidder = temporaryBidderRepository.findAllByProductIdAndBidderId(productId, tokenUser.getUserDAO().getId());
        if (temporaryBidder.isPresent()) {
            if (temporaryBidder.get().getStatus().equals(true)) {
                newProductBid = handleAutoBid(product, user, request);
                return new AuObjectCommonResponse(tokenUser.getToken(), newProductBid);
            }
        }
        Page<Transaction> transactions = transactionRepository.findAllByRecipientId(user.getId(), Pageable.unpaged());
        if (transactions.getTotalElements() == 0) {
            throw new BadRequestException("Unrated users cannot participate in the auction. Please request permission to bid on this product.");
        }
        AtomicInteger goodPoint = new AtomicInteger();
        transactions.forEach(transaction -> {
            goodPoint.getAndIncrement();
            if (!transaction.getIsLike()) {
                goodPoint.getAndDecrement();
            }
        });
        double point = (double) ((goodPoint.get() / transactions.getTotalElements()) * 100);
        if (point < 80) {
            throw new BadRequestException("Ops, Your rating is too low to participate in the auction. Please request permission to bid on this product.");
        }
        newProductBid = handleAutoBid(product, user, request);

        return new AuObjectCommonResponse(tokenUser.getToken(), newProductBid);
    }

    private ProductBidder handleAutoBid(Product product, UserDAO user, BidProductRequest request) {
        Optional<AutoBidList> autoBidList = autoBidListRepository.findAllByBidderIdAndProductId(user.getId(), product.getId());
        AutoBidList autoBidList1;
        if (autoBidList.isPresent()) {
            autoBidList1 = autoBidList.get();
        } else {
            autoBidList1 = new AutoBidList();
            autoBidList1.setBidder(user);
            autoBidList1.setProduct(product);
        }
        autoBidList1.setPrice(request.getPrice());
        autoBidListRepository.save(autoBidList1);
        request.setPrice(product.getCurrentPrice() + product.getStepPrice());
        return handleBidOfBidder(product, user, request);
    }
}
