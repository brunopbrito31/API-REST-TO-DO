package com.brunopbrito31.apitodo.models.exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String msg){
        super(msg);
    }
}
