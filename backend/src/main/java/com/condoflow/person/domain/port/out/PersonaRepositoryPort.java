package com.condoflow.person.domain.port.out;

import com.condoflow.person.domain.model.Persona;

import java.util.List;
import java.util.Optional;

/**
 * Port OUT: lo que el núcleo necesita de la persistencia, sin mencionar JPA ni PostgreSQL.
 * Lo implementa PersonaPersistenceAdapter en infraestructura.
 */
public interface PersonaRepositoryPort {

    /** Inserta si la persona no tiene id; si lo tiene, actualiza esa fila. */
    Persona guardar(Persona persona);

    Optional<Persona> buscarPorId(Long id);

    List<Persona> listar(String filtro);

    /** Respalda la regla UNIQUE uq_persona_correo. */
    boolean existePorCorreoElectronico(String correoElectronico);

    /** Igual que el anterior, pero ignora a la propia persona (al actualizar puede conservar su correo). */
    boolean existePorCorreoElectronicoEnOtraPersona(String correoElectronico, Long personaId);

    /**
     * Borra la persona. Si tiene filas que la referencian (FK de residencia o reserva),
     * lanza PersonaConRegistrosAsociadosException.
     */
    void eliminar(Long id);
}
