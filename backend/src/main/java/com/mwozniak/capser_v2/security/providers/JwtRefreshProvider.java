package com.mwozniak.capser_v2.security.providers;


import com.mwozniak.capser_v2.configuration.JwtConfiguration;
import com.mwozniak.capser_v2.enums.Roles;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.security.RefreshTokenAuthentication;
import com.mwozniak.capser_v2.security.TokenFactory;
import com.mwozniak.capser_v2.service.UserService;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtRefreshProvider implements AuthenticationProvider {

    private final JwtConfiguration jwtConfiguration;
    private final SecretKeySpec key;
    private final UserService userService;

    public JwtRefreshProvider(JwtConfiguration jwtConfiguration, UserService userService) {
        this.jwtConfiguration = jwtConfiguration;
        this.userService = userService;
        this.key = new SecretKeySpec(jwtConfiguration.getSecret().getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return validateToken(authentication.getCredentials().toString());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(RefreshTokenAuthentication.class);
    }

    @SuppressWarnings("unchecked")
    private Authentication validateToken(String jwtToken) {

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .requireIssuer(jwtConfiguration.getIssuer())
                    .parseClaimsJws(jwtToken).getBody();

            Optional<User> userOptional = userService.getUserOptional(UUID.fromString(claims.getSubject()));

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String role =  user.getRole().toString();
                userService.updateLastSeen(user);
                List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(role));
                return new RefreshTokenAuthentication(claims.getSubject(), TokenFactory.generateAuthToken(user.getId(), role),authorities);
            } else {
                throw new UsernameNotFoundException("User with this id doesn't exist");
            }
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        } catch (IOException | NullPointerException e) {
            throw new AuthenticationServiceException("Error occurred while trying to authenticate");
        }

    }
}
