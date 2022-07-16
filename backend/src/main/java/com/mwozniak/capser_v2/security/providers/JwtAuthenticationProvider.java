package com.mwozniak.capser_v2.security.providers;

import com.mwozniak.capser_v2.configuration.JwtConfiguration;
import com.mwozniak.capser_v2.security.JwtTokenAuthentication;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.spec.SecretKeySpec;
import java.util.Collections;

@Log4j2
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtConfiguration jwtConfiguration;
    private final SecretKeySpec key;

    public JwtAuthenticationProvider(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
        key = new SecretKeySpec(jwtConfiguration.getSecret().getBytes(),SignatureAlgorithm.HS512.getJcaName());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return validateToken(authentication.getCredentials().toString());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(JwtTokenAuthentication.class);
    }

    private Authentication validateToken(String jwtToken){

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .requireIssuer(jwtConfiguration.getIssuer())
                    .parseClaimsJws(jwtToken).getBody();

                return new JwtTokenAuthentication(claims.getSubject(),jwtToken, Collections.singletonList(new SimpleGrantedAuthority(claims.get("rol").toString())));

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.info(e.getMessage());
            throw new BadCredentialsException(e.getMessage(),e);
        }

    }
}
