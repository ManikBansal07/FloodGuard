package com.floodguard.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI floodGuardAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("FloodGuard API")
                .description("API for flood prediction and management system")
                .version("1.0")
                .contact(new Contact()
                    .name("FloodGuard Team")
                    .email("support@floodguard.com")
                    .url("https://floodguard.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Development server")
            ));
    }
} 