package com.mwozniak.capser_v2.security;

import com.mwozniak.capser_v2.configuration.JwtConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class TokenFactory {

    public static String generateAuthToken(UUID id, String role, JwtConfiguration jwtConfiguration) throws IOException {


        String jwtIssuer = jwtConfiguration.getIssuer();
        String key = jwtConfiguration.getSecret();
        long authExp = Long.parseLong(jwtConfiguration.getAuthExpirationTime());

        return Jwts.builder().setSubject(id.toString()).claim("rol", role).setIssuer(jwtIssuer)
                .setExpiration(new Date(System.currentTimeMillis() + authExp))
                .signWith(new SecretKeySpec(key.getBytes(), SignatureAlgorithm.HS512.getJcaName())).compact();
    }


    public static String generateRefreshToken(UUID id, JwtConfiguration jwtConfiguration) throws IOException {

        String jwtIssuer = jwtConfiguration.getIssuer();
        String key = jwtConfiguration.getSecret();
        long refreshExp = Long.parseLong(jwtConfiguration.getRefreshExpirationTime());


        return Jwts.builder().setSubject(id.toString()).setIssuer(jwtIssuer)
                .setExpiration(new Date(System.currentTimeMillis() + refreshExp))
                .signWith(new SecretKeySpec(key.getBytes(), SignatureAlgorithm.HS512.getJcaName())).compact();
    }


}
