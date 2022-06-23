package com.hcmus.lovelybackend.entity.common;

import com.hcmus.lovelybackend.entity.dao.ProductBidder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuMessageCommonResponse {
    private ResponseHeader responseHeader;
    private String message;

    public AuMessageCommonResponse(String newToken, String message) {
        this.responseHeader = new ResponseHeader(newToken, "Bearer ");
        this.message = message;
    }
}
