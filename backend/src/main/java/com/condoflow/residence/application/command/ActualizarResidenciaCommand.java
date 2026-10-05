package com.condoflow.residence.application.command;

import com.condoflow.residence.domain.model.EstadoResidencia;
import com.condoflow.residence.domain.model.TipoResidencia;

import java.time.LocalDate;

/**
 * Datos de la operación "actualizar residencia" (PUT, Guía 07 del frontend).
 * Representación completa: permite reasignar la persona (cambiar la FK persona_id),
 * cambiar la unidad o el tipo, y finalizar la residencia (estado FINALIZADA con fecha de fin).
 */
public record ActualizarResidenciaCommand(
        Long personaId,
        Long unidadId,
        TipoResidencia tipoResidencia,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        EstadoResidencia estado
) {
}
