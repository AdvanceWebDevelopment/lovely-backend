package com.hcmus.lovelybackend.service.impl;

import com.hcmus.lovelybackend.constant.FilterSearchConstant;
import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.common.TokenUser;
import com.hcmus.lovelybackend.entity.dao.Product;
import com.hcmus.lovelybackend.entity.dao.TemporaryBidder;
import com.hcmus.lovelybackend.entity.response.GetProductResponse;
import com.hcmus.lovelybackend.exception.runtime.NotFoundException;
import com.hcmus.lovelybackend.exception.runtime.ProductNotFoundException;
import com.hcmus.lovelybackend.repository.ProductBidderRepository;
import com.hcmus.lovelybackend.repository.ProductRepository;
import com.hcmus.lovelybackend.repository.TemporaryBidderRepository;
import com.hcmus.lovelybackend.service.IProductService;
import com.hcmus.lovelybackend.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TemporaryBidderRepository temporaryBidderRepository;

    @Autowired
    private AppUtils appUtils;

    @Scheduled(fixedRate = 300000)
    public void scheduleTaskToSendMail() {
        sendEmailNotifyToProduct();
        log.info("Send email to user to notify information of product.");
    }

    @Scheduled(fixedRate = 432000000)
    public void scheduleTaskToDelReqBid() {
        List<TemporaryBidder> temporaryBidder = temporaryBidderRepository.findAll();
        if (!temporaryBidder.isEmpty()) {
            temporaryBidder.forEach(t -> {
                if (t.getCreatedAtTypeDateTime().plusDays(7).isBefore(LocalDateTime.now())) {
                    temporaryBidderRepository.deleteById(t.getId());
                }
            });
        }
        log.info("Remove all request bid of user is expire.");
    }

    private void sendEmailNotifyToProduct() {
        List<Product> products = productRepository.findAllByEndAtExpired(LocalDateTime.now());
        if (products != null) {
            products.forEach(p -> {
                if (!p.getNotified()) {
                    if (p.getBidderHighest() == null) {
                        appUtils.sendNotifictionEmail(p.getSeller(), "The product has ended the auction and there is no winner.", p);
                    } else {
                        appUtils.sendNotifictionEmail(p.getSeller(), "The product has ended the auction and has a winner.", p);
                        appUtils.sendNotifictionEmail(p.getBidderHighest(), "The product has ended the auction and has a winner.", p);
                    }
                    p.setNotified(true);
                    productRepository.save(p);
                }
            });
        }
    }

    @Override
    public List<Product> getProducts(String top) {
        List<Product> products = null;
        if (top.equals("price")) {
            products = productRepository.findAll(PageRequest.of(0, 5, Sort.by("currentPrice").descending())).getContent();
        } else if (top.equals("date")) {
            products = productRepository.findTop5toEnd(LocalDateTime.now(), PageRequest.of(0, 5));
        } else if (top.equals("most-bids")) {
            products = productRepository.findTop5MostBids(LocalDateTime.now(), PageRequest.of(0, 5));
        } else {
            products = productRepository.findAll();
        }
        products.forEach(p -> {
            if (p.getImages() != null) {
                p.getImages().removeIf(image -> !image.getIsMain());
                p.setImages(p.getImages());
            }
        });
        return products;
    }

    @Override
    public GetProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        Page<Product> products = productRepository.findAllBySubCategoryIdAndIdNot(product.getSubCategory().getId(), PageRequest.of(0, 6), id);
        products.forEach(p -> {
            p.getImages().removeIf(image -> !image.getIsMain());
            p.setImages(p.getImages());
        });
        GetProductResponse response = new GetProductResponse(product, products);
        return response;
    }

    @Override
    public Page<Product> searchProduct(Integer categoryId, Integer subcategoryId, String text, FilterSearchConstant sortBy, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        if (sortBy != null) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy.toString()));
        }
        if (subcategoryId != null) {
            return productRepository.findAllBySubCategoryIdAndNameContaining(subcategoryId, text, pageable);
        }
        if (categoryId != null) {
            if (categoryId == 1) {
                return productRepository.findAllByNameContaining(text, pageable);
            }
            return productRepository.findAllByCategoryIdAndName(categoryId, text, pageable);
        }
        return productRepository.findAllByNameContaining(text, pageable);
    }

    @Override
    public AuMessageCommonResponse deleteProduct(String token, Integer productId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        productRepository.findById(productId).orElseThrow(() -> {
            throw new NotFoundException("Product not found !!!");
        });
        productRepository.deleteById(productId);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Delete product successfully.");
    }
}
