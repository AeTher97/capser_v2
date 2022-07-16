package com.mwozniak.capser_v2.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CapserAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public CapserAuthenticationFailureHandler(){
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new FailureMessage(e.getMessage())));
        httpServletResponse.setStatus(401);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Data
    @AllArgsConstructor
    private static class FailureMessage {
        private String error;

    }
}
