package com.condoflow.person.domain.port.in;

import com.condoflow.person.domain.model.Persona;

import java.util.List;
import java.util.Optional;

/**
 * Port IN: capacidades de consulta de personas.
 * También es el contrato público que usan otros módulos (por ejemplo residence) para validar que una persona exista.
 */
public interface ConsultarPersonaUseCase {

    Optional<Persona> buscarPorId(Long id);

    List<Persona> listar(String filtro);
}
