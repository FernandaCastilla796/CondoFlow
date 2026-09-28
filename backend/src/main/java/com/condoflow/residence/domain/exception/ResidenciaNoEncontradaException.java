package com.condoflow.residence.domain.exception;

public class ResidenciaNoEncontradaException extends RuntimeException {

    public ResidenciaNoEncontradaException(Long id) {
        super("No existe una residencia con id " + id);
    }
}
