package com.hcmus.lovelybackend.exception.runtime;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(){
        super("Product Not Found");
    }
}
