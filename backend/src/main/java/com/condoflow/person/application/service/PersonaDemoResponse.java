package com.condoflow.person.application.service;

/** Capítulo 03: record de salida del endpoint demo de la entidad padre. */
public record PersonaDemoResponse(
        Long personaId,
        String nombre,
        String apellido,
        String correoElectronico
) {
}
