package com.condoflow.person.application.service;

import com.condoflow.person.domain.exception.CorreoPersonaDuplicadoException;
import com.condoflow.person.domain.exception.PersonaConRegistrosAsociadosException;
import com.condoflow.person.domain.exception.PersonaNoEncontradaException;
import com.condoflow.person.domain.model.EstadoPersona;
import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.domain.port.out.PersonaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Prueba unitaria del caso de uso: el Port OUT se reemplaza por una implementación en memoria.
 * Demuestra la ventaja de la arquitectura hexagonal: la regla se prueba sin Spring ni PostgreSQL.
 */
class PersonaServiceTest {

    private PersonaRepositoryEnMemoria repositorio;
    private PersonaService service;

    @BeforeEach
    void setUp() {
        repositorio = new PersonaRepositoryEnMemoria();
        service = new PersonaService(repositorio);
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

    // ===== Guía 06 del frontend: actualizar y eliminar =====

    @Test
    void actualizaLosDatosYElEstadoConservandoElId() {
        Persona creada = service.registrar(maria("maria@condoflow.com"));
        Persona cambios = new Persona(null, "Maria", "Lopez Vaca", "7845123", "+591 70000009",
                "maria@condoflow.com", EstadoPersona.INACTIVO);

        Persona actualizada = service.actualizar(creada.getPersonaId(), cambios);

        assertThat(actualizada.getPersonaId()).isEqualTo(creada.getPersonaId());
        assertThat(actualizada.getApellido()).isEqualTo("Lopez Vaca");
        assertThat(actualizada.getEstado()).isEqualTo(EstadoPersona.INACTIVO);
        assertThat(service.listar(null)).hasSize(1);
    }

    @Test
    void actualizarPersonaInexistenteLanzaNoEncontrada() {
        assertThatThrownBy(() -> service.actualizar(999L, maria("otra@condoflow.com")))
                .isInstanceOf(PersonaNoEncontradaException.class);
    }

    @Test
    void actualizarConElCorreoDeOtraPersonaLanzaConflicto() {
        service.registrar(maria("maria@condoflow.com"));
        Persona carlos = service.registrar(maria("carlos@condoflow.com"));

        assertThatThrownBy(() -> service.actualizar(carlos.getPersonaId(), maria("maria@condoflow.com")))
                .isInstanceOf(CorreoPersonaDuplicadoException.class);
    }

    @Test
    void eliminaUnaPersonaSinRegistrosAsociados() {
        Persona creada = service.registrar(maria("maria@condoflow.com"));

        service.eliminar(creada.getPersonaId());

        assertThat(service.buscarPorId(creada.getPersonaId())).isEmpty();
    }

    @Test
    void eliminarPersonaInexistenteLanzaNoEncontrada() {
        assertThatThrownBy(() -> service.eliminar(999L))
                .isInstanceOf(PersonaNoEncontradaException.class);
    }

    @Test
    void noEliminaUnaPersonaConResidencias() {
        Persona creada = service.registrar(maria("maria@condoflow.com"));
        repositorio.marcarConResidencias(creada.getPersonaId());

        assertThatThrownBy(() -> service.eliminar(creada.getPersonaId()))
                .isInstanceOf(PersonaConRegistrosAsociadosException.class);
        assertThat(service.buscarPorId(creada.getPersonaId())).isPresent();
    }

    private static Persona maria(String correo) {
        return Persona.nueva("Maria", "Lopez", "7845123", "+591 70000001", correo);
    }

    /** Implementación mínima del Port OUT sólo para pruebas. */
    private static final class PersonaRepositoryEnMemoria implements PersonaRepositoryPort {

        private final List<Persona> datos = new ArrayList<>();
        private final Set<Long> conResidencias = new HashSet<>();
        private long secuencia = 0;

        void marcarConResidencias(Long id) {
            conResidencias.add(id);
        }

        @Override
        public Persona guardar(Persona persona) {
            if (persona.getPersonaId() == null) {
                Persona conId = persona.conId(++secuencia);
                datos.add(conId);
                return conId;
            }
            datos.replaceAll(p -> p.getPersonaId().equals(persona.getPersonaId()) ? persona : p);
            return persona;
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

        @Override
        public boolean existePorCorreoElectronicoEnOtraPersona(String correoElectronico, Long personaId) {
            return datos.stream().anyMatch(p -> p.getCorreoElectronico().equals(correoElectronico)
                    && !p.getPersonaId().equals(personaId));
        }

        /** Simula la FK de PostgreSQL: con residencias no se puede borrar. */
        @Override
        public void eliminar(Long id) {
            if (conResidencias.contains(id)) {
                throw new PersonaConRegistrosAsociadosException(id);
            }
            datos.removeIf(p -> p.getPersonaId().equals(id));
        }
    }
}
