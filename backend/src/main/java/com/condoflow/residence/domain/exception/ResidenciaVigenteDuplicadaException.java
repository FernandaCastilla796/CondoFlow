package com.condoflow.residence.domain.exception;

/**
 * Regla de negocio: una persona no puede tener dos residencias VIGENTES en la misma unidad.
 * En PostgreSQL la respalda el índice UNIQUE parcial uq_residencia_vigente_persona_unidad.
 */
public class ResidenciaVigenteDuplicadaException extends RuntimeException {

    public ResidenciaVigenteDuplicadaException(Long personaId, Long unidadId) {
        super("La persona " + personaId + " ya tiene una residencia vigente en la unidad " + unidadId);
    }
}
