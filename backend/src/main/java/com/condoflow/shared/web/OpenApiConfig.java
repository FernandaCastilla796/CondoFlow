package com.condoflow.shared.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos de la documentación de la API (springdoc-openapi, dependencia indicada por el docente).
 * Swagger UI: /swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI condoflowOpenApi() {
        return new OpenAPI().info(new Info()
                .title("CondoFlow API")
                .version("v1")
                .description("Administración operativa de condominio (PA-04)."));
    }
}
