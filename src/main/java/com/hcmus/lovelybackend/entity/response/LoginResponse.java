package com.hcmus.lovelybackend.entity.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private String accessToken;
    private String tokenType;
    private String refreshToken;

    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.refreshToken = refreshToken;
    }
}