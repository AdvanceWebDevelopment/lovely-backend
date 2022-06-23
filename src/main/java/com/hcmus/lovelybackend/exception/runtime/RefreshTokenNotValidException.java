package com.hcmus.lovelybackend.exception.runtime;

public class RefreshTokenNotValidException extends RuntimeException{

    public RefreshTokenNotValidException() {
        super("Refresh token is not valid !!!");
    }
}
