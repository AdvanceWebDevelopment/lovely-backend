package com.hcmus.lovelybackend.service.impl;

import com.hcmus.lovelybackend.entity.dao.UserDAO;
import com.hcmus.lovelybackend.repository.UserRepository;
import com.hcmus.lovelybackend.utils.JwtUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class RefreshTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    public String generateRefreshToken() {
        return RandomStringUtils.random(32, true, false);
    }

    public Boolean checkRfTokenValid(String accessToken, String rfToken) {
        String curUsername = jwtUtil.getUsernameFromExpireToken(accessToken);
        if (StringUtils.hasText(curUsername)) {
            UserDAO userDAO = userRepository.findByEmail(curUsername);
            if (userDAO.getRefreshToken().equals(rfToken)) {
                return true;
            }
        }
        return false;
    }

    public String generateNewTokenIfExpire() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean statusTokenExpire = (Boolean) request.getAttribute("accessTokenExpire");
        if (statusTokenExpire) {
            UserDetails userDetails = (UserDetails) principal;
            return jwtUtil.generateToken(userDetails);
        }
        return null;
    }

}
