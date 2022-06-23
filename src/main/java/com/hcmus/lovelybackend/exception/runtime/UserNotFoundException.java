package com.hcmus.lovelybackend.exception.runtime;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("User Not Found");
    }
    public UserNotFoundException(String message){
        super(message);
    }
}
