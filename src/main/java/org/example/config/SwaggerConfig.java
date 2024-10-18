package org.example.config;

import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Server(url = "http://localhost:8080/swagger-ui/index.html", description = "Swagger ui")
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("SpringBoot Demo Application")
                .pathsToMatch("/api/**")
                .build();
    }
}