package com.hcmus.lovelybackend.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Old Password is mandatory")
    private String oldPassword;

    @NotBlank(message = "New Password is mandatory")
    private String newPassword;

    @NotBlank(message = "Confirm password is mandatory")
    private String confirmPassword;
}
