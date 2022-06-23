package com.hcmus.lovelybackend.service;

import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.dao.UserDAO;
import com.hcmus.lovelybackend.entity.request.*;
import com.hcmus.lovelybackend.entity.response.AuPageResponse;
import com.hcmus.lovelybackend.entity.response.CurrentUserResponse;

public interface IUserService {
    CurrentUserResponse getCurrentUser();
    void register(RegisterRequest registerRequest);
    boolean verify(VerifyRequest verifyRequest);
    boolean forgotPassword(String email);
    void resetPassword(ResetPasswordRequest request);
    UserDAO getUserProfile(String token);
    AuMessageCommonResponse updateUserProfile(String token, UpdateUserProfileRequest request);
    AuMessageCommonResponse updateUserProfileByAdmin(String token, UpdateUserProfileRequest request, Integer userId);
    AuMessageCommonResponse createUser(String token, RegisterRequest request);
    AuPageResponse getListUsers(String token, Integer page, Integer size);
    AuMessageCommonResponse deleteUser(String token, Integer userId);
    AuMessageCommonResponse changePassword(String token, ChangePasswordRequest request);
}
