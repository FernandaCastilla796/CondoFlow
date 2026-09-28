package com.condoflow.residence.domain.port.in;

import com.condoflow.residence.domain.model.TipoResidencia;

import java.time.LocalDate;

/**
 * Datos de entrada del caso de uso "registrar residencia".
 * Vive junto al Port IN para que el núcleo no dependa del DTO HTTP.
 */
public record RegistrarResidenciaCommand(
        Long personaId,
        Long unidadId,
        TipoResidencia tipoResidencia,
        LocalDate fechaInicio
) {
}
