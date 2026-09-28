package com.condoflow;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Levanta un PostgreSQL 16 real en Docker para las pruebas de integración.
 * Spring Boot toma URL, usuario y contraseña del contenedor (@ServiceConnection)
 * y Flyway aplica V1..Vn antes de cada ejecución.
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer("postgres:16-alpine");
    }
}
