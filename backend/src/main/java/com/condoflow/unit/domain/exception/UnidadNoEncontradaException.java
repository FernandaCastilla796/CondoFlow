package com.condoflow.unit.domain.exception;

public class UnidadNoEncontradaException extends RuntimeException {

    public UnidadNoEncontradaException(Long id) {
        super("No existe una unidad con id " + id);
    }
}
