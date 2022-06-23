package com.hcmus.lovelybackend.exception.runtime;

public class SendOTPEmailException extends RuntimeException{
    public SendOTPEmailException(){
        super("Cannot send OTP Email");
    }

}
