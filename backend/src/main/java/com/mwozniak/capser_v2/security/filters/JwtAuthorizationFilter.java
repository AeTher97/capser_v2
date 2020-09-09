package com.mwozniak.capser_v2.security.filters;

import com.mwozniak.capser_v2.security.JwtTokenAuthentication;
import lombok.extern.log4j.Log4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if(authHeader==null){
            log.debug("Authorization header missing");
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }

        try {
            JwtTokenAuthentication authentication = new JwtTokenAuthentication(null,stripBearer(authHeader),null);
            Authentication authResult = authenticationManager.authenticate(authentication);

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authResult);

            SecurityContextHolder.setContext(securityContext);
            filterChain.doFilter(httpServletRequest,httpServletResponse);

        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }
    }

    private String stripBearer(String tokenWithBearer){
        return tokenWithBearer.replace("Bearer","").trim();
    }
}
