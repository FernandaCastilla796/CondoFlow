package com.condoflow.person.infrastructure.adapter.in.web.mapper;

import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.infrastructure.adapter.in.web.dto.ActualizarPersonaRequest;
import com.condoflow.person.infrastructure.adapter.in.web.dto.CrearPersonaRequest;
import com.condoflow.person.infrastructure.adapter.in.web.dto.PersonaResponse;

/** Traduce entre el contrato HTTP (DTOs) y el modelo de dominio. */
public final class PersonaWebMapper {

    private PersonaWebMapper() {
    }

    public static Persona toDomain(CrearPersonaRequest request) {
        return Persona.nueva(
                request.nombre(),
                request.apellido(),
                request.documento(),
                request.telefono(),
                request.correoElectronico());
    }

    /** El id lo pone el caso de uso a partir de la URL; aquí la persona todavía no lo tiene. */
    public static Persona toDomain(ActualizarPersonaRequest request) {
        return new Persona(
                null,
                request.nombre(),
                request.apellido(),
                request.documento(),
                request.telefono(),
                request.correoElectronico(),
                request.estado());
    }

    public static PersonaResponse toResponse(Persona persona) {
        return new PersonaResponse(
                persona.getPersonaId(),
                persona.getNombre(),
                persona.getApellido(),
                persona.getDocumento(),
                persona.getTelefono(),
                persona.getCorreoElectronico(),
                persona.getEstado().name());
    }
}
