package com.example.loanservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI loanServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Loan Service API")
                        .description("API documentation for the Loan Service")
                        .version("v1.0"));
    }
}
