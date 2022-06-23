package com.hcmus.lovelybackend.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "OTP is mandatory")
    private String otp;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Confirm password is mandatory")
    private String confirmPassword;
}
