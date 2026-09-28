package com.condoflow.person.domain.port.out;

import com.condoflow.person.domain.model.Persona;

import java.util.List;
import java.util.Optional;

/**
 * Port OUT: lo que el núcleo necesita de la persistencia, sin mencionar JPA ni PostgreSQL.
 * Lo implementa PersonaPersistenceAdapter en infraestructura.
 */
public interface PersonaRepositoryPort {

    Persona guardar(Persona persona);

    Optional<Persona> buscarPorId(Long id);

    List<Persona> listar(String filtro);

    /** Respalda la regla UNIQUE uq_persona_correo. */
    boolean existePorCorreoElectronico(String correoElectronico);
}
