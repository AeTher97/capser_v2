package com.mwozniak.capser_v2.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "database")
public class DatabaseProperties {

    String url;
    String username;
    String password;

}
