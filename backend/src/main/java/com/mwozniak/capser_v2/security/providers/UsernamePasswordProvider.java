package com.mwozniak.capser_v2.security.providers;


import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.security.TokenFactory;
import com.mwozniak.capser_v2.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsernamePasswordProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UsernamePasswordProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<User> userOptional = userService.getUser(authentication.getPrincipal().toString());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
                try {
                    userService.updateLastSeen(user);
                    return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                            new TokenHolder(
                                    TokenFactory.generateAuthToken(user.getId(), user.getRole().toString()),
                                    TokenFactory.generateRefreshToken(user.getId())),
                            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString())));
                } catch (IOException | NullPointerException e) {
                    throw new AuthenticationServiceException("Error occurred while trying to authenticate");
                }
            } else {
                throw new BadCredentialsException("Invalid password");
            }
        } else {
            throw new UsernameNotFoundException("User with this username not found");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);

    }
}
