package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public class ResetTokenExpiredException extends CapserException {

    public ResetTokenExpiredException(String message) {
        super(message);
    }

    public ResetTokenExpiredException(String message, Throwable t) {
        super(message, t);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
