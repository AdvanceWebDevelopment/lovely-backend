package com.hcmus.lovelybackend.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHeader {
    private String accessToken;
    private String tokenType = "Bearer";
}
