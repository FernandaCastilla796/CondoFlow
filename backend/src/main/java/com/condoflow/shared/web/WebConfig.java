package com.condoflow.shared.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Configuración web compartida: CORS para los clientes y metadatos de la documentación OpenAPI. */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String[] allowedOrigins;

    public WebConfig(@Value("${condoflow.cors.allowed-origins}") String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    /**
     * En desarrollo React corre en otro origen (http://localhost:5173) que el backend (:8080),
     * y el navegador bloquea la petición salvo que el backend la permita explícitamente.
     * Se habilitan sólo los orígenes configurados, no "*".
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }

    @Bean
    public OpenAPI condoflowOpenApi() {
        return new OpenAPI().info(new Info()
                .title("CondoFlow API")
                .version("v1")
                .description("Administración operativa de condominio (PA-04). "
                        + "Todos los errores siguen el formato ApiError."));
    }
}
