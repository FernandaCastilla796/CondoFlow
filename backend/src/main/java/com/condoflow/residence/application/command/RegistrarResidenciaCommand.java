package com.condoflow.residence.application.command;

import com.condoflow.residence.domain.model.TipoResidencia;

import java.time.LocalDate;

/**
 * Record con los datos que necesita la operación "registrar residencia" (Capítulos 02 y 08).
 * No incluye id ni estado: el id lo genera PostgreSQL y toda residencia nueva nace VIGENTE.
 */
public record RegistrarResidenciaCommand(
        Long personaId,
        Long unidadId,
        TipoResidencia tipoResidencia,
        LocalDate fechaInicio
) {
}
