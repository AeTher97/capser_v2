package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public class CredentialTakenException extends CapserException {


    public CredentialTakenException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
