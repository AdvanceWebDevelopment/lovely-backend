package com.hcmus.lovelybackend.exception.runtime;

public class ConfirmPasswordIncorrectException extends RuntimeException{
    public ConfirmPasswordIncorrectException(){
        super("Confirm password is not correct");
    }
}
