package com.condoflow.shared.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS para desarrollo (Guía 05 del frontend): el navegador sólo deja que la app React de Vite
 * (http://localhost:5173) lea las respuestas de /api/** si el backend autoriza ese origen.
 * CORS no autentica usuarios: sólo decide qué origen del navegador puede llamar a la API.
 * En producción se autoriza el dominio real del frontend, no localhost.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
