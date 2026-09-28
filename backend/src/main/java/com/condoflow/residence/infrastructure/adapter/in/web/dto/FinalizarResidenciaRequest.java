package com.condoflow.residence.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FinalizarResidenciaRequest(
        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate fechaFin
) {
}
