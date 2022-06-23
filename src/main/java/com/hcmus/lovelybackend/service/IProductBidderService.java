package com.hcmus.lovelybackend.service;

import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.common.AuObjectCommonResponse;
import com.hcmus.lovelybackend.entity.dao.ProductBidder;
import com.hcmus.lovelybackend.entity.request.BidProductRequest;
import com.hcmus.lovelybackend.entity.request.ReviewWinnerRequest;
import com.hcmus.lovelybackend.entity.response.AuEvaluationResponse;
import com.hcmus.lovelybackend.entity.response.AuPageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductBidderService {
    List<ProductBidder> getTop5MostBids();

    AuObjectCommonResponse saveBidOfBidder(String token, Integer productId, BidProductRequest request);

    Page<ProductBidder> getListBidOfProduct(Integer productId, Integer page, Integer size);

    AuEvaluationResponse getEvaluationDetails(String token, Integer page, Integer size);

    AuPageResponse getProductsWin(String token, Integer page, Integer size);

    AuPageResponse getListBidByBidder(String token, Integer page, Integer size);

    AuMessageCommonResponse reviewSeller(String token, Integer sellerId, Integer productId, ReviewWinnerRequest request);

    AuObjectCommonResponse payNowProduct(String token, Integer productId);

    AuObjectCommonResponse autoBid(String token, Integer productId, BidProductRequest request);
}
