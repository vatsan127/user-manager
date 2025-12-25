package com.github.user_manager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Manager API")
                        .description("API for managing users and their profiles with JPA one-to-one relationship")
                        .version("v1"));
    }
}
