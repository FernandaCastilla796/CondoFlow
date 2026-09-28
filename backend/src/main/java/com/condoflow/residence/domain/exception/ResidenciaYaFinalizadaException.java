package com.condoflow.residence.domain.exception;

/** Transición inválida: sólo una residencia VIGENTE puede finalizarse. */
public class ResidenciaYaFinalizadaException extends RuntimeException {

    public ResidenciaYaFinalizadaException(Long residenciaId) {
        super("La residencia " + residenciaId + " ya está finalizada");
    }
}
