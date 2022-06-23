package com.hcmus.lovelybackend.exception.runtime;

public class VerifyEmailException extends RuntimeException{
    public VerifyEmailException(){
        super("The verification code is incorrect");
    }
}
