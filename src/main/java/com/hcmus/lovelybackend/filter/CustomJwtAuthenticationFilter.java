package com.hcmus.lovelybackend.filter;


import com.hcmus.lovelybackend.exception.runtime.RefreshTokenNotValidException;
import com.hcmus.lovelybackend.service.impl.RefreshTokenService;
import com.hcmus.lovelybackend.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        request.setAttribute("accessTokenExpire", false);
        String jwtToken = extractJwtFromRequest(request);
        try {

            // JWT Token is in the form "Bearer token". Remove Bearer word and
            // get only the Token

            if (StringUtils.hasText(jwtToken) && jwtTokenUtil.validateToken(jwtToken)) {
                UserDetails userDetails = new User(jwtTokenUtil.getUsernameFromToken(jwtToken), "",
                        jwtTokenUtil.getRolesFromToken(jwtToken));

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                log.warn("Cannot set the Security Context");
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            log.error("Expired Jwt Exception: ", ex);
            String refreshToken = request.getHeader("refresh-token");
            if (StringUtils.hasText(refreshToken)) {
                Boolean checkRfToken = refreshTokenService.checkRfTokenValid(jwtToken, refreshToken);
                if (!checkRfToken) {
                    log.info("Refresh Token is not valid");
                    resolver.resolveException(request, response, null, new RefreshTokenNotValidException());
                } else {
                    try {
                        UserDetails userDetails = new User(jwtTokenUtil.getUsernameFromExpireToken(jwtToken), "",
                                jwtTokenUtil.getRolesFromExpireToken(jwtToken));
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                        request.setAttribute("accessTokenExpire", true);
                        chain.doFilter(request, response);
                        return;
                    } catch (IllegalArgumentException e) {
                        log.error("Illegal Argument Exception 1: ", ex);
                        resolver.resolveException(request, response, null, ex);
                    }
                }
            } else {
                resolver.resolveException(request, response, null, new RefreshTokenNotValidException());
            }
        } catch (BadCredentialsException ex) {
            log.error("Bad Credentials Exception: ", ex);
            resolver.resolveException(request, response, null, ex);
        } catch (IllegalArgumentException ex) {
            log.error("Illegal Argument Exception 2: ", ex);
            resolver.resolveException(request, response, null, ex);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Objects.equals(request.getRequestURI(), "/") && Objects.equals(request.getMethod(), "GET");
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}