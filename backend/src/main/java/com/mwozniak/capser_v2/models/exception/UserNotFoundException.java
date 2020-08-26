package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CapserException{
    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException(String message,Throwable t){
        super(message, t);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}

