package com.condoflow.person.domain.port.in;

import com.condoflow.person.domain.model.Persona;

/** Port IN: capacidad de registrar una persona en el condominio. */
public interface RegistrarPersonaUseCase {

    Persona registrar(Persona persona);
}
