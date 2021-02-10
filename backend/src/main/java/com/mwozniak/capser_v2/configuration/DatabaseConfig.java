package com.mwozniak.capser_v2.configuration;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.server.UID;

@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
public class DatabaseConfig {


    @Bean
    public DataSource dataSource(DatabaseProperties databaseProperties) throws URISyntaxException {
        String dbUrl;
        String username;
        String password;
        if (System.getenv().get("DYNO") != null) {
            URI dbUri = new URI(databaseProperties.getUrl());
            username = dbUri.getUserInfo().split(":")[0];
            password = dbUri.getUserInfo().split(":")[1];
            dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
        } else {
            dbUrl = "jdbc:postgresql://" + databaseProperties.getUrl() + "?sslmode=require";
            username = databaseProperties.getUsername();
            password = databaseProperties.getPassword();
        }

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(dbUrl);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();


    }
}
