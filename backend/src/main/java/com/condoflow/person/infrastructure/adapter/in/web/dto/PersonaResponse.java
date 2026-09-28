package com.condoflow.person.infrastructure.adapter.in.web.dto;

/**
 * Representación de una persona que devuelve la API.
 * A diferencia del request, incluye el id generado y el estado.
 */
public record PersonaResponse(
        Long personaId,
        String nombre,
        String apellido,
        String documento,
        String telefono,
        String correoElectronico,
        String estado
) {
}
