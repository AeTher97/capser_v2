package com.mwozniak.capser_v2.security.filters;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
public class UsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {

    private UsernamePasswordFilter(){
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email= request.getParameter(getUsernameParameter());
        String password = request.getParameter(getPasswordParameter());

        if(StringUtils.isEmpty(email)){
            throw new AuthenticationCredentialsNotFoundException("Username field not found");
        }

        if(StringUtils.isEmpty(password)){
            throw new AuthenticationCredentialsNotFoundException("Password not found");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,password);
        return getAuthenticationManager().authenticate(token);
    }


    public static UsernamePasswordFilter getUsernamePasswordFilter(AuthenticationManager authenticationManager, String path){
        UsernamePasswordFilter usernamePasswordFilter = new UsernamePasswordFilter();
        usernamePasswordFilter.setAuthenticationSuccessHandler(new CapserAuthenticationSuccessHandler());
        usernamePasswordFilter.setAuthenticationFailureHandler(new CapserAuthenticationFailureHandler());
        usernamePasswordFilter.setAuthenticationManager(authenticationManager);
        usernamePasswordFilter.setFilterProcessesUrl(path);
        usernamePasswordFilter.setUsernameParameter("username");
        return usernamePasswordFilter;
    }

}
