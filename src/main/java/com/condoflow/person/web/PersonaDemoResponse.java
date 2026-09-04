package com.condoflow.person.web;

public record PersonaDemoResponse(
        Long personaId,
        String nombre,
        String apellido,
        String correoElectronico
) {
}