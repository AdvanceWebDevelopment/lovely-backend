package com.hcmus.lovelybackend.utils;

import com.hcmus.lovelybackend.constant.RoleConstant;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class JwtUtil {

    private String secret;
    private int jwtExpirationInMs;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    // generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        if (roles.contains(new SimpleGrantedAuthority("0")) || roles.contains(new SimpleGrantedAuthority(RoleConstant.ROLE_ADMIN.toString()))) {
            claims.put(RoleConstant.ROLE_ADMIN.toString(), true);
        }
        if (roles.contains(new SimpleGrantedAuthority("1")) || roles.contains(new SimpleGrantedAuthority(RoleConstant.ROLE_BIDDER.toString()))) {
            claims.put(RoleConstant.ROLE_BIDDER.toString(), true);
        }
        if (roles.contains(new SimpleGrantedAuthority("2")) || roles.contains(new SimpleGrantedAuthority(RoleConstant.ROLE_SELLER.toString()))) {
            claims.put(RoleConstant.ROLE_SELLER.toString(), true);
        }
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public boolean validateToken(String authToken) {
        try {
            // Jwt token has not been tampered with
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        }
//        } catch (ExpiredJwtException ex) {
//            log.error("Expired JWT token.", ex);
//            throw new CredentialsExpiredException("Expired jwt credentials ", ex);
//        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getUsernameFromExpireToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    public List<SimpleGrantedAuthority> getRolesFromExpireToken(String authToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody();
            return getRolesFromToken(authToken);
        } catch (ExpiredJwtException e) {
            return convertRoleFromClaim(e.getClaims());
        }
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String authToken) {
        List<SimpleGrantedAuthority> roles = null;
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody();
        return convertRoleFromClaim(claims);
    }

    public List<SimpleGrantedAuthority> convertRoleFromClaim(Claims claims) {
        List<SimpleGrantedAuthority> roles = null;
        Boolean isAdmin = claims.get(RoleConstant.ROLE_ADMIN.toString(), Boolean.class);
        Boolean isBidder = claims.get(RoleConstant.ROLE_BIDDER.toString(), Boolean.class);
        Boolean isSeller = claims.get(RoleConstant.ROLE_SELLER.toString(), Boolean.class);
        if (isAdmin != null) {
            roles = List.of(new SimpleGrantedAuthority(RoleConstant.ROLE_ADMIN.toString()));
        }
        if (isBidder != null) {
            roles = List.of(new SimpleGrantedAuthority(RoleConstant.ROLE_BIDDER.toString()));
        }
        if (isSeller != null) {
            roles = List.of(new SimpleGrantedAuthority(RoleConstant.ROLE_SELLER.toString()));
        }
        return roles;
    }

}