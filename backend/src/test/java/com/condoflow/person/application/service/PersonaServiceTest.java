package com.condoflow.person.application.service;

import com.condoflow.person.domain.exception.CorreoPersonaDuplicadoException;
import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.domain.port.out.PersonaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Prueba unitaria del caso de uso: el Port OUT se reemplaza por una implementación en memoria.
 * Demuestra la ventaja de la arquitectura hexagonal: la regla se prueba sin Spring ni PostgreSQL.
 */
class PersonaServiceTest {

    private PersonaService service;

    @BeforeEach
    void setUp() {
        service = new PersonaService(new PersonaRepositoryEnMemoria());
    }

    @Test
    void registraUnaPersonaNuevaYLeAsignaId() {
        Persona guardada = service.registrar(maria("maria@condoflow.com"));

        assertThat(guardada.getPersonaId()).isNotNull();
        assertThat(service.buscarPorId(guardada.getPersonaId())).isPresent();
    }

    @Test
    void rechazaCorreoDuplicadoAunqueCambienMayusculas() {
        service.registrar(maria("maria@condoflow.com"));

        assertThatThrownBy(() -> service.registrar(maria("MARIA@CondoFlow.com")))
                .isInstanceOf(CorreoPersonaDuplicadoException.class)
                .hasMessageContaining("maria@condoflow.com");
    }

    @Test
    void buscarPorIdInexistenteDevuelveVacio() {
        assertThat(service.buscarPorId(999L)).isEmpty();
    }

    private static Persona maria(String correo) {
        return Persona.nueva("Maria", "Lopez", "7845123", "+591 70000001", correo);
    }

    /** Implementación mínima del Port OUT sólo para pruebas. */
    private static final class PersonaRepositoryEnMemoria implements PersonaRepositoryPort {

        private final List<Persona> datos = new ArrayList<>();

        @Override
        public Persona guardar(Persona persona) {
            Persona conId = persona.conId((long) datos.size() + 1);
            datos.add(conId);
            return conId;
        }

        @Override
        public Optional<Persona> buscarPorId(Long id) {
            return datos.stream().filter(p -> p.getPersonaId().equals(id)).findFirst();
        }

        @Override
        public List<Persona> listar(String filtro) {
            return List.copyOf(datos);
        }

        @Override
        public boolean existePorCorreoElectronico(String correoElectronico) {
            return datos.stream().anyMatch(p -> p.getCorreoElectronico().equals(correoElectronico));
        }
    }
}
