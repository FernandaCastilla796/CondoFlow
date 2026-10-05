package com.condoflow.residence.infrastructure.adapter.in.web.dto;

import com.condoflow.residence.domain.model.EstadoResidencia;
import com.condoflow.residence.domain.model.TipoResidencia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * Datos para PUT /api/residencias/{id}: representación completa de la residencia.
 * personaId puede ser otra persona (reasignación). fechaFin es obligatoria sólo si el estado es FINALIZADA;
 * esa regla la valida el dominio y responde 400.
 */
public record ActualizarResidenciaRequest(
        @NotNull(message = "La persona es obligatoria")
        @Positive(message = "El id de persona debe ser positivo")
        Long personaId,

        @NotNull(message = "La unidad es obligatoria")
        @Positive(message = "El id de unidad debe ser positivo")
        Long unidadId,

        @NotNull(message = "El tipo de residencia es obligatorio (PROPIETARIO o INQUILINO)")
        TipoResidencia tipoResidencia,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        LocalDate fechaFin,

        @NotNull(message = "El estado es obligatorio (VIGENTE o FINALIZADA)")
        EstadoResidencia estado
) {
}
