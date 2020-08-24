package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public class GameValidationException  extends CapserException{

    public GameValidationException(String message){
        super(message);
    }

    public GameValidationException(String message,Throwable t){
        super(message, t);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
