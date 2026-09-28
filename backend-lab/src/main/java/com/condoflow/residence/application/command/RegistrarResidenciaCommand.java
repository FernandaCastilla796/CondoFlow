package com.condoflow.residence.application.command;

import com.condoflow.residence.domain.TipoResidencia;

import java.time.LocalDate;

/**
 * Datos mínimos para registrar una residencia. No incluye id ni estado:
 * el id lo asigna el sistema y toda residencia nueva nace VIGENTE.
 */
public record RegistrarResidenciaCommand(
        Long personaId,
        Long unidadId,
        TipoResidencia tipoResidencia,
        LocalDate fechaInicio
) {
}
