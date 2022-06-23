package com.hcmus.lovelybackend.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuObjectCommonResponse {
    private ResponseHeader responseHeader;
    private Object object;

    public AuObjectCommonResponse(String newToken, Object object) {
        this.responseHeader = new ResponseHeader(newToken, "Bearer ");
        this.object = object;
    }
}