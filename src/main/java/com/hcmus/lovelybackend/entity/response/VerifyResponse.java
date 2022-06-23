package com.hcmus.lovelybackend.entity.response;

import com.hcmus.lovelybackend.entity.common.GoodFormatResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class VerifyResponse extends GoodFormatResponse {
    public VerifyResponse(LocalDateTime timestamp, HttpStatus status, String message) {
        super(timestamp, status, message);
    }
}
