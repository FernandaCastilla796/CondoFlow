package com.condoflow.residence.infrastructure.adapter.in.web.dto;

import com.condoflow.residence.domain.model.TipoResidencia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * Datos para registrar una residencia. tipoResidencia se recibe como enum:
 * un valor fuera de PROPIETARIO/INQUILINO no se puede convertir y responde 400.
 */
public record CrearResidenciaRequest(
        @NotNull(message = "La persona es obligatoria")
        @Positive(message = "El id de persona debe ser positivo")
        Long personaId,

        @NotNull(message = "La unidad es obligatoria")
        @Positive(message = "El id de unidad debe ser positivo")
        Long unidadId,

        @NotNull(message = "El tipo de residencia es obligatorio (PROPIETARIO o INQUILINO)")
        TipoResidencia tipoResidencia,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio
) {
}
