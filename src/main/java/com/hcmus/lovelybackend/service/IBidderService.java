package com.hcmus.lovelybackend.service;

import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.response.AuPageResponse;

public interface IBidderService {
    AuMessageCommonResponse saveProductToWatchList(String token, Integer productId);
    AuPageResponse getWatchList(String token, Integer page, Integer size);
    AuMessageCommonResponse requestBidProduct(String token, Integer productId);
}
