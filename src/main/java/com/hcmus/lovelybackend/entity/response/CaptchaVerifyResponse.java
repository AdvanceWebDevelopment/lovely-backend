package com.hcmus.lovelybackend.entity.response;

import com.hcmus.lovelybackend.entity.common.GoodFormatResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class CaptchaVerifyResponse extends GoodFormatResponse {
    public CaptchaVerifyResponse() {
        super(LocalDateTime.now(), HttpStatus.OK, "Captcha authentication successful.");
    }
}
