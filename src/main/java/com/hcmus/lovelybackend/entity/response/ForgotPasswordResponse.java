package com.hcmus.lovelybackend.entity.response;

import com.hcmus.lovelybackend.entity.common.GoodFormatResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ForgotPasswordResponse extends GoodFormatResponse {

    public ForgotPasswordResponse(LocalDateTime timestamp, HttpStatus status, String message) {
        super(timestamp, status, message);
    }
}
