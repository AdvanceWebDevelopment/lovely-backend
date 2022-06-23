package com.hcmus.lovelybackend.entity.response;

import com.hcmus.lovelybackend.entity.common.GoodFormatResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResetPasswordResponse extends GoodFormatResponse {
    public ResetPasswordResponse(LocalDateTime timestamp, HttpStatus status, String message) {
        super(timestamp, status, message);
    }
}
