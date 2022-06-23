package com.hcmus.lovelybackend.controller;

import com.hcmus.lovelybackend.entity.request.*;
import com.hcmus.lovelybackend.entity.response.*;
import com.hcmus.lovelybackend.exception.runtime.BadRequestException;
import com.hcmus.lovelybackend.exception.runtime.VerifyEmailException;
import com.hcmus.lovelybackend.service.IUserService;
import com.hcmus.lovelybackend.service.impl.LoginService;
import com.hcmus.lovelybackend.service.impl.ValidateCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ValidateCaptchaService validateCaptchaService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {
        return ResponseEntity.ok(loginService.handleLogin(authenticationRequest));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> handleRegister(@RequestBody RegisterRequest registerRequest, HttpServletRequest request) throws Exception {
        userService.register(registerRequest);
        RegisterResponse response = new RegisterResponse(LocalDateTime.now(), HttpStatus.CREATED, "Please confirm email.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyRequest verifyRequest) {
        if (userService.verify(verifyRequest)) {
            VerifyResponse response = new VerifyResponse(LocalDateTime.now(), HttpStatus.OK, "The OTP has been verified.");
            return ResponseEntity.ok(response);
        }
        throw new VerifyEmailException();
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public ResponseEntity<?> handleForgotPassword(@RequestBody ForgotPasswordRequest request) {
        if (userService.forgotPassword(request.getEmail())) {
            ForgotPasswordResponse response = new ForgotPasswordResponse(LocalDateTime.now(), HttpStatus.ACCEPTED, "Please confirm email.");
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        throw new VerifyEmailException();
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity<?> handleResetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        ResetPasswordResponse response = new ResetPasswordResponse(LocalDateTime.now(), HttpStatus.OK, "Change password successfully.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/captcha")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> welcome(@RequestBody CaptchaRequest captchaRequest){
        final boolean isValidCaptcha = validateCaptchaService.validateCaptcha(captchaRequest.getCaptcha());
        if (!isValidCaptcha) {
            throw new BadRequestException("INVALID_CAPTCHA");
        }
        return ResponseEntity.ok(new CaptchaVerifyResponse());
    }
}