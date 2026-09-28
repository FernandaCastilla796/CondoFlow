package com.condoflow.person.domain.exception;

public class PersonaNoEncontradaException extends RuntimeException {

    public PersonaNoEncontradaException(Long id) {
        super("No existe una persona con id " + id);
    }
}
