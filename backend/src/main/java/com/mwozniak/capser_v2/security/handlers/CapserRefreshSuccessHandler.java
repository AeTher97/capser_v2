package com.mwozniak.capser_v2.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CapserRefreshSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    public CapserRefreshSuccessHandler() {
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        httpServletResponse.addHeader("Authorization", authentication.getCredentials().toString());
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new SuccessMessage(authentication.getCredentials().toString())));
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

    }

    @Data
    @AllArgsConstructor
    private static class SuccessMessage {
        private String authToken;

    }
}
