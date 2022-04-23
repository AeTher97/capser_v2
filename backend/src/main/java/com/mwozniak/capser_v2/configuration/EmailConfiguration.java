package com.mwozniak.capser_v2.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "email")
@Data
public class EmailConfiguration {

    private String username;
    private String token;
    private String clientId;
    private String clientSecret;
    private int port = 587;
    private String smtpHost = "smtp.gmail.com";
}
