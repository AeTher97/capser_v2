package com.mwozniak.capser_v2.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class TokenFactory {

    public static String generateAuthToken(UUID id, String role) throws IOException {
        Properties properties = getProperties();

        verifyProperties(properties);

        String jwtIssuer = properties.getProperty("jwt.issuer");
        String key = properties.getProperty("jwt.secret");
        long authExp = Long.parseLong(properties.getProperty("jwt.auth-expiration-time"));

        return Jwts.builder().setSubject(id.toString()).claim("rol", role).setIssuer(jwtIssuer)
                .setExpiration(new Date(System.currentTimeMillis() + authExp))
                .signWith(new SecretKeySpec(key.getBytes(), SignatureAlgorithm.HS512.getJcaName())).compact();
    }


    public static String generateRefreshToken(UUID id) throws IOException {
        Properties properties = getProperties();

        verifyProperties(properties);

        String jwtIssuer = properties.getProperty("jwt.issuer");
        String key = properties.getProperty("jwt.secret");
        long refreshExp = Long.parseLong(properties.getProperty("jwt.refresh-expiration-time"));


        return Jwts.builder().setSubject(id.toString()).setIssuer(jwtIssuer)
                .setExpiration(new Date(System.currentTimeMillis() + refreshExp))
                .signWith(new SecretKeySpec(key.getBytes(), SignatureAlgorithm.HS512.getJcaName())).compact();
    }

    private static void verifyProperties(Properties properties) throws IOException {
        String jwtIssuer = properties.getProperty("jwt.issuer");
        String key = properties.getProperty("jwt.secret");

        if (StringUtils.isEmpty(jwtIssuer)) {
            throw new IOException("Jwt issuer not present in properties");
        }

        if (StringUtils.isEmpty(key)) {
            throw new IOException("Jwt key not present in properties");
        }
    }

    private static Properties getProperties() throws IOException {
        InputStream stream = TokenFactory.class.getResourceAsStream("/application.properties");
        Properties properties = new Properties();
        properties.load(stream);
        return properties;
    }
}
