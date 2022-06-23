package com.hcmus.lovelybackend.service;

import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.response.AuPageResponse;

public interface IUpgradeListService {
    AuPageResponse getUpgradeList(String token, Integer page, Integer size);
    AuPageResponse getListSeller(String token, Integer page, Integer size);
    AuMessageCommonResponse acceptUpgradeUser(String token, Integer bidderId);
    AuMessageCommonResponse downgradeUser(String token, Integer sellerId);
    AuMessageCommonResponse requestUpgrade(String token);
}
