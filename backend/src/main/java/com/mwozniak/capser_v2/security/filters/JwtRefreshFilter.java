package com.mwozniak.capser_v2.security.filters;

import com.mwozniak.capser_v2.security.RefreshTokenAuthentication;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtRefreshFilter extends UsernamePasswordAuthenticationFilter {

    private JwtRefreshFilter(){

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String refreshToken = request.getParameter(getPasswordParameter());

        if(StringUtils.isEmpty(refreshToken)){
            throw new AuthenticationCredentialsNotFoundException("Refresh token not found");
        }

        RefreshTokenAuthentication authenticationToken = new RefreshTokenAuthentication(null,refreshToken,null);
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    public static JwtRefreshFilter getJwtRefreshFilter(AuthenticationManager authenticationManager, String path){
        JwtRefreshFilter jwtRefreshFilter = new JwtRefreshFilter();
        jwtRefreshFilter.setAuthenticationSuccessHandler(new CapserRefreshSuccessHandler());
        jwtRefreshFilter.setAuthenticationFailureHandler(new CapserAuthenticationFailureHandler());
        jwtRefreshFilter.setAuthenticationManager(authenticationManager);
        jwtRefreshFilter.setFilterProcessesUrl(path);
        jwtRefreshFilter.setPasswordParameter("refreshToken");
        return jwtRefreshFilter;
    }
}
