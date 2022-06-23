package com.hcmus.lovelybackend.exception.runtime;

public class UserAlreadyExsitsException extends RuntimeException{
    public UserAlreadyExsitsException(){
        super("User Already Exists");
    }
}
