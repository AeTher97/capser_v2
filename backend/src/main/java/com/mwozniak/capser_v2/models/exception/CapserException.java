package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public abstract class CapserException extends RuntimeException {
    CapserException(String message) {
        super(message);
    }

    CapserException(String message, Throwable t) {
        super(message, t);
    }

    public abstract HttpStatus getStatusCode();
}
