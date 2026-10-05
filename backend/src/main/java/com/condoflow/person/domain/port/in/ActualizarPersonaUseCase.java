package com.condoflow.person.domain.port.in;

import com.condoflow.person.domain.model.Persona;

/** Port IN: reemplazar los datos de una persona existente (PUT, Guía 06 del frontend). */
public interface ActualizarPersonaUseCase {

    Persona actualizar(Long id, Persona datos);
}
