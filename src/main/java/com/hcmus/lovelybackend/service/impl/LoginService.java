package com.hcmus.lovelybackend.service.impl;

import com.hcmus.lovelybackend.entity.dao.UserDAO;
import com.hcmus.lovelybackend.entity.request.LoginRequest;
import com.hcmus.lovelybackend.entity.response.LoginResponse;
import com.hcmus.lovelybackend.repository.UserRepository;
import com.hcmus.lovelybackend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public LoginResponse handleLogin(LoginRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        String token = jwtTokenUtil.generateToken(userDetails);
        String rfToken = refreshTokenService.generateRefreshToken();

        UserDAO userDAO = userRepository.findByEmail(userDetails.getUsername());
        if (userDAO != null) {
            userDAO.setRefreshToken(rfToken);
            userRepository.save(userDAO);
        }

        return new LoginResponse(token, rfToken);
    }

}
