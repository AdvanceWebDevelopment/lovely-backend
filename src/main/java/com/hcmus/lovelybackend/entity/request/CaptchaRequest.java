package com.hcmus.lovelybackend.entity.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaRequest {
    @NonNull
    String captcha;
}
