package com.hcmus.lovelybackend.exception.runtime;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}

