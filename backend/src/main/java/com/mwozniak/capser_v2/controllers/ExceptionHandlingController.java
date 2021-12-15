package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.responses.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class ExceptionHandlingController {

    @ExceptionHandler(value = CapserException.class)
    private ResponseEntity<Object> handleException(CapserException e){
        log.error(e.getMessage());

        return ResponseEntity.status(e.getStatusCode().value()).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = IllegalStateException.class)
    private ResponseEntity<Object> handleIllegalStateException(IllegalStateException e){
        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(e.getMessage()));
    }
}
