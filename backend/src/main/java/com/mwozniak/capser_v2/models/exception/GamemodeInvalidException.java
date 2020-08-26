package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public class GamemodeInvalidException extends CapserException {
    public GamemodeInvalidException(String message){
        super(message);
    }

    public GamemodeInvalidException(String message,Throwable t){
        super(message, t);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}

