package com.condoflow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Arranca el contexto completo contra PostgreSQL: si Flyway falla o el mapeo JPA
 * no coincide con las tablas (ddl-auto=validate), esta prueba falla.
 */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Testcontainers(disabledWithoutDocker = true)
class CondoflowBackendApplicationTests {

    @Test
    void contextLoads() {
    }
}
