package com.hcmus.lovelybackend.service;

import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.dao.Product;
import com.hcmus.lovelybackend.entity.request.ReviewWinnerRequest;
import com.hcmus.lovelybackend.entity.request.UpdateProductRequest;
import com.hcmus.lovelybackend.entity.response.AuPageResponse;
import com.hcmus.lovelybackend.entity.response.GetListProductOfSellerResponse;

public interface ISellerService {
    AuMessageCommonResponse createProduct(String token, Product product);

    AuMessageCommonResponse updateDescriptionProduct(String token, UpdateProductRequest updateProductRequest, Integer productId);

    AuMessageCommonResponse rejectBidderBidProduct(String token, Integer productBidderId, Integer productId, Integer bidderId);

    AuMessageCommonResponse reviewWinner(String token, Integer winnerId, Integer productId, ReviewWinnerRequest request);

    GetListProductOfSellerResponse getListProductBySeller(String token, Integer page, Integer size);

    GetListProductOfSellerResponse getListProductHaveWinner(String token, Integer page, Integer size);

    AuMessageCommonResponse rejectWinner(String token, Integer winnerId, Integer productId);

    AuPageResponse getListBidderTemporary(String token, Integer productId, Integer page, Integer size);

    AuMessageCommonResponse acceptBidProduct(String token, Integer bidderId, Integer productId);

    AuMessageCommonResponse rejectBidProduct(String token, Integer bidderId, Integer productId);
}
