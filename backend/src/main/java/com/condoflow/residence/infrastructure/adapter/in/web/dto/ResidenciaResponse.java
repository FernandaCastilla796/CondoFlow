package com.condoflow.residence.infrastructure.adapter.in.web.dto;

import java.time.LocalDate;

public record ResidenciaResponse(
        Long residenciaId,
        Long personaId,
        Long unidadId,
        String tipoResidencia,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String estado
) {
}
