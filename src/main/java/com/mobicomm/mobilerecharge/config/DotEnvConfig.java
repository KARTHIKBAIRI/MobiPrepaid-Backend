package com.mobicomm.mobilerecharge.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class DotEnvConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .directory("./") // Looks for .env in project root
                .ignoreIfMissing() // Don't fail if .env is missing
                .load();
    }

    @Bean
    public MapPropertySource dotEnvPropertySource(Dotenv dotenv, ConfigurableEnvironment environment) {
        // Define allowed .env variables
        String[] allowedKeys = {
            "db_url", "db_username", "db_password",
            "jwt_secret",
            "mail_username", "mail_password",
            "admin_username", "admin_password",
            "frontend_url"
        };

        Map<String, Object> envMap = dotenv.entries()
                .stream()
                .filter(entry -> {
                    for (String key : allowedKeys) {
                        if (entry.getKey().equals(key)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toMap(
                        DotenvEntry::getKey,
                        DotenvEntry::getValue
                ));

        MapPropertySource propertySource = new MapPropertySource("dotenv", envMap);
        environment.getPropertySources().addFirst(propertySource);
        return propertySource;
    }
}