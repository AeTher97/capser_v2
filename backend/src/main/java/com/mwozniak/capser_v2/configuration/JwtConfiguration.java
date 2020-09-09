package com.mwozniak.capser_v2.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfiguration {

    private String issuer;
    private String secret;
    private String authExpirationTime;
    private String refreshExpirationTime;
}
