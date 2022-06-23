package com.hcmus.lovelybackend.service.impl;

import com.hcmus.lovelybackend.constant.AuthProvider;
import com.hcmus.lovelybackend.entity.common.AuMessageCommonResponse;
import com.hcmus.lovelybackend.entity.common.ResponseHeader;
import com.hcmus.lovelybackend.entity.common.TokenUser;
import com.hcmus.lovelybackend.entity.dao.UserDAO;
import com.hcmus.lovelybackend.entity.request.*;
import com.hcmus.lovelybackend.entity.response.AuPageResponse;
import com.hcmus.lovelybackend.entity.response.CurrentUserResponse;
import com.hcmus.lovelybackend.exception.runtime.*;
import com.hcmus.lovelybackend.repository.UserRepository;
import com.hcmus.lovelybackend.service.IUserService;
import com.hcmus.lovelybackend.utils.AppUtils;
import com.hcmus.lovelybackend.utils.JwtUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private ValidateCaptchaService validateCaptchaService;

    @Override
    public CurrentUserResponse getCurrentUser() {
        UserDetails userDetails = customUserDetailsService.getCurrentUserDetails();

        CurrentUserResponse currentUserResponse = new CurrentUserResponse();

        String newToken = refreshTokenService.generateNewTokenIfExpire();
        if (newToken != null) {
            currentUserResponse.setResponseHeader(new ResponseHeader(newToken, "Bearer"));
        } else {
            currentUserResponse.setResponseHeader(new ResponseHeader(null, "Bearer"));
        }
        currentUserResponse.setUser(userRepository.findByEmail(userDetails.getUsername()));
        return currentUserResponse;
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        final boolean isValidCaptcha = validateCaptchaService.validateCaptcha(registerRequest.getCaptcha());
        if (!isValidCaptcha) {
            throw new BadRequestException("INVALID_CAPTCHA");
        }
        UserDAO userDAO = new UserDAO();
        userDAO.setEmail(registerRequest.getEmail());
        userDAO.setPassword(registerRequest.getPassword());
        userDAO.setName(registerRequest.getName());
        if (userRepository.findByEmail(userDAO.getEmail()) != null) {
            throw new UserAlreadyExsitsException();
        }
        String encodedPassword = passwordEncoder.encode(userDAO.getPassword());
        userDAO.setPassword(encodedPassword);

        String randomeCode = RandomStringUtils.randomNumeric(6);
        userDAO.setVerificationCode(randomeCode);
        userDAO.setEnabled(false);
        userDAO.setProvider(AuthProvider.local);

        userRepository.save(userDAO);

        appUtils.sendVerificationEmail(userDAO, "registration");
    }

    @Override
    public boolean verify(VerifyRequest verifyRequest) {
        UserDAO userDAO = null;
        userDAO = userRepository.findByEmail(verifyRequest.getEmail());
        if (userDAO == null) {
            throw new VerifyEmailException();
        } else {
            if (!userDAO.getVerificationCode().equals(verifyRequest.getOtp())) {
                throw new VerifyEmailException();
            } else {
                if(!userDAO.getEnabled()){
                    userDAO.setEnabled(true);
                }
                userRepository.save(userDAO);
                return true;
            }
        }
    }

    @Override
    public boolean forgotPassword(String email) {
        UserDAO userDAO = null;
        userDAO = userRepository.findByEmail(email);
        if (userDAO != null) {
            String randomeCode = RandomStringUtils.randomNumeric(6);
            userDAO.setVerificationCode(randomeCode);
            userRepository.save(userDAO);
            appUtils.sendVerificationEmail(userDAO, "forgot password");
            return true;
        }
        return false;
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        UserDAO userDAO = null;
        userDAO = userRepository.findByEmail(request.getEmail());
        if (userDAO == null) {
            throw new UserNotFoundException();
        } else {
            if (!request.getOtp().equals(userDAO.getVerificationCode())) {
                throw new VerifyEmailException();
            } else {
                if (!request.getPassword().equals(request.getConfirmPassword())) {
                    throw new ConfirmPasswordIncorrectException();
                } else {
                    String encodedPassword = passwordEncoder.encode(request.getPassword());
                    userDAO.setPassword(encodedPassword);
                    userDAO.setVerificationCode(null);
                    userDAO.setEnabled(true);
                    userRepository.save(userDAO);
                }
            }
        }
    }

    @Override
    public UserDAO getUserProfile(String token) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        return tokenUser.getUserDAO();
    }

    @Override
    public AuMessageCommonResponse updateUserProfile(String token, UpdateUserProfileRequest request) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        UserDAO user = tokenUser.getUserDAO();
        int check = 0;
        if(StringUtils.isNotEmpty(request.getName())){
            user.setName(request.getName());
            check++;
        }
        if(request.getDateOfBirth() != null){
            user.setBirthDay(request.getDateOfBirth());
            check++;
        }
        if(StringUtils.isNotEmpty(request.getEmail())){
            user.setEmail(request.getEmail());
            check++;
        }
        if(check > 0) {
            userRepository.save(user);
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String newToken = jwtTokenUtil.generateToken(userDetails);
            tokenUser.setToken(newToken);
            return new AuMessageCommonResponse(tokenUser.getToken(), "Update user " + user.getName() + " success !!!");
        }
        throw new UpdateUserProfileException("Update User profile failed !!!");
    }

    @Override
    public AuMessageCommonResponse updateUserProfileByAdmin(String token, UpdateUserProfileRequest request, Integer userId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        UserDAO user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User not found");
        });
        int check = 0;
        if(StringUtils.isNotEmpty(request.getName())){
            user.setName(request.getName());
            check++;
        }
        if(request.getDateOfBirth() != null){
            user.setBirthDay(request.getDateOfBirth());
            check++;
        }
        if(StringUtils.isNotEmpty(request.getEmail())){
            user.setEmail(request.getEmail());
            check++;
        }
        if(check > 0) {
            userRepository.save(user);
            return new AuMessageCommonResponse(tokenUser.getToken(), "Update user " + user.getName() + " success !!!");
        }
        throw new UpdateUserProfileException("Update User profile failed !!!");
    }

    @Override
    public AuMessageCommonResponse createUser(String token, RegisterRequest registerRequest) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        UserDAO userDAO = new UserDAO();
        userDAO.setEmail(registerRequest.getEmail());
        userDAO.setPassword(registerRequest.getPassword());
        userDAO.setName(registerRequest.getName());
        if (userRepository.findByEmail(userDAO.getEmail()) != null) {
            throw new UserAlreadyExsitsException();
        }
        String encodedPassword = passwordEncoder.encode(userDAO.getPassword());
        userDAO.setPassword(encodedPassword);

        userDAO.setEnabled(true);
        userDAO.setProvider(AuthProvider.local);

        userRepository.save(userDAO);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Create user successfully.");
    }

    @Override
    public AuPageResponse getListUsers(String token, Integer page, Integer size) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        Page<UserDAO> users = userRepository.findAll(PageRequest.of(page, size));
        return new AuPageResponse(tokenUser.getToken(), users);
    }

    @Override
    public AuMessageCommonResponse deleteUser(String token, Integer userId) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        UserDAO userDAO = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User not found");
        });
        userRepository.delete(userDAO);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Delete user successfully.");
    }

    @Override
    public AuMessageCommonResponse changePassword(String token, ChangePasswordRequest request) {
        TokenUser tokenUser = appUtils.handleTokenCommon(token);
        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new BadRequestException("Confirm password is incorrect");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(request.getOldPassword(), tokenUser.getUserDAO().getPassword())){
            throw new BadRequestException("Old password is incorrect");
        }
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        UserDAO userDAO = tokenUser.getUserDAO();
        userDAO.setPassword(encodedNewPassword);
        userRepository.save(userDAO);
        return new AuMessageCommonResponse(tokenUser.getToken(), "Change password successfully.");
    }
}


