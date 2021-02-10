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

@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
public class DatabaseConfig {


    @Bean
    public DataSource dataSource(DatabaseProperties databaseProperties) throws URISyntaxException {
        String dbUrl;
        if (System.getenv().get("DYNO") != null) {
            dbUrl = databaseProperties.getUrl().replace("postgres://","jdbc:postgresql://").split("@")[1];
        } else {
            dbUrl = "jdbc:postgresql://" + databaseProperties.getUrl() + "?sslmode=require";
        }

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(dbUrl);
        dataSourceBuilder.username(databaseProperties.getUsername());
        dataSourceBuilder.password(databaseProperties.getPassword());
        return dataSourceBuilder.build();


    }
}
