package com.hcmus.lovelybackend.service.impl;

import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.common.TokenUser;
import com.hcmus.lovelybackend.entity.dao.Product;
import com.hcmus.lovelybackend.entity.dao.TemporaryBidder;
import com.hcmus.lovelybackend.entity.dao.WatchList;
import com.hcmus.lovelybackend.entity.response.AuPageResponse;
import com.hcmus.lovelybackend.exception.runtime.AddProductToWatchListException;
import com.hcmus.lovelybackend.exception.runtime.BadRequestException;
import com.hcmus.lovelybackend.exception.runtime.ProductNotFoundException;
import com.hcmus.lovelybackend.repository.ProductRepository;
import com.hcmus.lovelybackend.repository.TemporaryBidderRepository;
import com.hcmus.lovelybackend.repository.UserRepository;
import com.hcmus.lovelybackend.repository.WatchListRepository;
import com.hcmus.lovelybackend.service.IBidderService;
import com.hcmus.lovelybackend.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BidderService implements IBidderService {

    @Autowired
    private WatchListRepository watchListRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private TemporaryBidderRepository temporaryBidderRepository;

    @Override
    public AuMessageCommonResponse saveProductToWatchList(String token, Integer productId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        WatchList watchList = watchListRepository.save(new WatchList(null, tokenUser.getUserDAO(), product));
        if(watchList == null){
            throw new AddProductToWatchListException();
        }
        return new AuMessageCommonResponse(tokenUser.getToken(), "Add product to watch list success !!!");
    }

    @Override
    public AuPageResponse getWatchList(String token, Integer page, Integer size) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        if(page == null){
            page = 0;
        }
        if(size == null){
            size = 10;
        }
        Page<WatchList> watchLists = watchListRepository.findAllByBidderId(tokenUser.getUserDAO().getId(), PageRequest.of(page, size));
        return new AuPageResponse(tokenUser.getToken(), watchLists);
    }

    @Override
    public AuMessageCommonResponse requestBidProduct(String token, Integer productId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        if (product.getEndAtTypeDateTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Product is expire.");
        }
        if(product.getSeller().getId().equals(tokenUser.getUserDAO().getId())){
            throw new BadRequestException("You cannot request bid this product.");
        }
        TemporaryBidder temporaryBidder = new TemporaryBidder();
        temporaryBidder.setBidder(tokenUser.getUserDAO());
        temporaryBidder.setProduct(product);
        temporaryBidder.setStatus(false);
        temporaryBidderRepository.save(temporaryBidder);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Request bid product success.");
    }
}
