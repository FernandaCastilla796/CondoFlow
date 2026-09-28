package com.condoflow.residence.application.command;

public record RegistrarResidenciaCommand(
        Long personaId,
        Long unidadId
) {
}