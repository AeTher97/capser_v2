package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public class DataValidationException extends CapserException {

    public DataValidationException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
