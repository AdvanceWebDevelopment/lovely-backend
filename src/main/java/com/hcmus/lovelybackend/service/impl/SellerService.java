package com.hcmus.lovelybackend.service.impl;

import com.hcmus.lovelybackend.constant.TransactionStatus;
import com.hcmus.lovelybackend.constant.TransactionType;
import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.common.TokenUser;
import com.hcmus.lovelybackend.entity.dao.*;
import com.hcmus.lovelybackend.entity.request.ReviewWinnerRequest;
import com.hcmus.lovelybackend.entity.request.UpdateProductRequest;
import com.hcmus.lovelybackend.entity.response.AuPageResponse;
import com.hcmus.lovelybackend.entity.response.GetListProductOfSellerResponse;
import com.hcmus.lovelybackend.exception.runtime.*;
import com.hcmus.lovelybackend.repository.*;
import com.hcmus.lovelybackend.service.ISellerService;
import com.hcmus.lovelybackend.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class SellerService implements ISellerService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductBidderRepository productBidderRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TemporaryBidderRepository temporaryBidderRepository;

    @Override
    public AuMessageCommonResponse createProduct(String token, Product product) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        if (product.getId() != null) {
            product.setId(null);
        }
        SubCategory subCategory = subCategoryRepository.getById(product.getSubCategory().getId());
        product.setSeller(tokenUser.getUserDAO());
        product.setSubCategory(subCategory);
        Set<Image> imageSet = product.getImages();
        imageSet.stream().forEach(image -> {
            image.setProduct(product);
        });
        List<Description> descriptions = product.getDescriptions();
        descriptions.stream().forEach(description -> {
            description.setCreateAt(LocalDateTime.now());
            description.setProduct(product);
        });
        product.setCreateAt(LocalDateTime.now());
        product.setImages(imageSet);
        product.setDescriptions(descriptions);
        productRepository.save(product);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Create product success !!!");
    }

    @Override
    public AuMessageCommonResponse updateDescriptionProduct(String token, UpdateProductRequest updateProductRequest, Integer productId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Product realProduct = productRepository.findAllById(productId).orElseThrow(ProductNotFoundException::new);
        if (!tokenUser.getUserDAO().getId().equals(realProduct.getSeller().getId())) {
            throw new ProductNotFoundException();
        }
        Description description = new Description(null, realProduct, LocalDateTime.now(), updateProductRequest.getDescription());
        List<Description> descriptions = realProduct.getDescriptions();
        descriptions.add(description);
        realProduct.setDescriptions(descriptions);
        productRepository.save(realProduct);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Update description product success !!!");
    }

    @Override
    public AuMessageCommonResponse rejectBidderBidProduct(String token, Integer productBidderId, Integer productId, Integer bidderId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Product product = productRepository.findAllById(productId).orElseThrow(ProductNotFoundException::new);
        if (!product.getSeller().getId().equals(tokenUser.getUserDAO().getId())) {
            throw new ProductNotFoundException();
        }
        ProductBidder productBidder = productBidderRepository.findById(productBidderId).orElseThrow(ProductNotFoundException::new);
        if (!productBidder.getProduct().getId().equals(productId)) {
            throw new ProductNotFoundException();
        }
        if (!productBidder.getBidder().getId().equals(bidderId)) {
            throw new UserNotFoundException();
        }
        if (!productBidder.getReject()) {
            productBidder.setReject(true);
            productBidderRepository.save(productBidder);
            if (product.getBidderHighest().getId().equals(productBidder.getBidder().getId())) {
                List<ProductBidder> productBidders = productBidderRepository.findAllByProductIdAndBidderIdNotOrderByPriceDesc(productId, productBidder.getBidder().getId());
                if (productBidders == null) {
                    product.setBidderHighest(null);
                } else {
                    product.setBidderHighest(productBidders.get(0).getBidder());
                }
                productRepository.save(product);
            }
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            UserDAO bidder = userRepository.findById(bidderId).get();
            appUtils.sendNotifictionEmail(bidder, "You have been refused to bid on this product.", product);
            executor.shutdown();
        });
        return new AuMessageCommonResponse(tokenUser.getToken(), "Bidder " + productBidder.getBidder().getId() + " was rejected in product " + productBidder.getProduct().getId());
    }

    @Override
    public AuMessageCommonResponse reviewWinner(String token, Integer winnerId, Integer productId, ReviewWinnerRequest request) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        UserDAO seller = tokenUser.getUserDAO();
        Product product = productRepository.findAllById(productId).orElseThrow(ProductNotFoundException::new);
        UserDAO winner = userRepository.findById(winnerId).orElseThrow(UserNotFoundException::new);

        LocalDateTime productEndAt = LocalDateTime.parse(product.getEndAt());
        if (!productEndAt.isBefore(LocalDateTime.now())) {
            throw new NoWinnerYetException();
        }
        if (!product.getBidderHighest().getId().equals(winner.getId())) {
            throw new UserNotFoundException("Winner does not match !!!");
        }
        if(transactionRepository.findAllByProductIdAndAssessorIdAndRecipientId(productId, tokenUser.getUserDAO().getId(), winnerId).isPresent()){
            throw new BadRequestException("You have already rated this winner.");
        }

        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setAssessor(seller);
        transaction.setRecipient(winner);
        transaction.setType(TransactionType.SELL);
        transaction.setComment(request.getComment());
        transaction.setIsLike(request.getLike());

        Transaction newTransaction = transactionRepository.save(transaction);
        List<Transaction> transactions = transactionRepository.findAllByRecipientId(winner.getId());
        transactions.add(newTransaction);
        List<Transaction> transactionsLike = transactions.stream().filter(p -> p.getIsLike().equals(true)).collect(Collectors.toList());
        double point = transactionsLike.isEmpty() ? 0 : (double) ((transactionsLike.size()/transactions.size()) * 100);
        winner.setPoint(point);
        userRepository.save(winner);

        return new AuMessageCommonResponse(tokenUser.getToken(), "Seller " + seller.getName() + " evaluates winner " + winner.getName() + " successfully.");
    }

    @Override
    public GetListProductOfSellerResponse getListProductBySeller(String token, Integer page, Integer size) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        return new GetListProductOfSellerResponse(tokenUser.getToken(), productRepository.findAllBySellerId(tokenUser.getUserDAO().getId(), LocalDateTime.now(), PageRequest.of(page, size)));
    }

    @Override
    public GetListProductOfSellerResponse getListProductHaveWinner(String token, Integer page, Integer size) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        return new GetListProductOfSellerResponse(tokenUser.getToken(), productRepository.findAllBySellerIdAndHaveWinner(tokenUser.getUserDAO().getId(), LocalDateTime.now(), PageRequest.of(page, size)));
    }

    @Override
    public AuMessageCommonResponse rejectWinner(String token, Integer winnerId, Integer productId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        UserDAO seller = tokenUser.getUserDAO();
        Product product = productRepository.findAllById(productId).orElseThrow(ProductNotFoundException::new);
        UserDAO winner = userRepository.findById(winnerId).orElseThrow(UserNotFoundException::new);

        if (!product.getEndAtTypeDateTime().isBefore(LocalDateTime.now())) {
            throw new NoWinnerYetException();
        }
        if (!product.getBidderHighest().getId().equals(winner.getId())) {
            throw new UserNotFoundException("Winner does not match !!!");
        }

        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setAssessor(seller);
        transaction.setRecipient(winner);
        transaction.setType(TransactionType.SELL);
        transaction.setComment("Người thắng không thanh toán.");
        transaction.setIsLike(false);
        transaction.setStatus(TransactionStatus.REJECT);

        transactionRepository.save(transaction);

        return new AuMessageCommonResponse(tokenUser.getToken(), "Seller " + seller.getName() + " reject winner " + winner.getName() + " successfully.");
    }

    @Override
    public AuPageResponse getListBidderTemporary(String token, Integer productId, Integer page, Integer size) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        if (product.getEndAtTypeDateTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Product is expire.");
        }
        if (!product.getSeller().getId().equals(tokenUser.getUserDAO().getId())) {
            throw new BadRequestException("You cannot get list request bid in this product.");
        }
        Page<TemporaryBidder> temporaryBidders = temporaryBidderRepository.findAllByProductIdAndSellerId(productId, tokenUser.getUserDAO().getId(), PageRequest.of(page, size));
        return new AuPageResponse(tokenUser.getToken(), temporaryBidders);
    }

    @Override
    public AuMessageCommonResponse acceptBidProduct(String token, Integer bidderId, Integer productId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        userRepository.findById(bidderId).orElseThrow(UserNotFoundException::new);
        if (!product.getSeller().getId().equals(tokenUser.getUserDAO().getId())) {
            throw new BadRequestException("You cannot accept user bid this product");
        }
        if (product.getEndAtTypeDateTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Product is expire.");
        }
        TemporaryBidder temporaryBidder = temporaryBidderRepository.findAllByProductIdAndBidderId(productId, bidderId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Cannot find user request bid this product.");
                });
        if (temporaryBidder.getCreatedAtTypeDateTime().isAfter(LocalDateTime.now().plusDays(7))) {
            throw new BadRequestException("Request is expire");
        }
        temporaryBidder.setStatus(true);
        temporaryBidderRepository.save(temporaryBidder);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Accept user bid product success");
    }

    @Override
    public AuMessageCommonResponse rejectBidProduct(String token, Integer bidderId, Integer productId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        userRepository.findById(bidderId).orElseThrow(UserNotFoundException::new);
        TemporaryBidder temporaryBidder = temporaryBidderRepository.findAllByProductIdAndBidderId(productId, bidderId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Cannot find user request bid this product.");
                });
        temporaryBidderRepository.deleteById(temporaryBidder.getId());
        return new AuMessageCommonResponse(tokenUser.getToken(), "Reject user bid product success");
    }
}
