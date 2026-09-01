package com.condoflow.person.exception;

public class PersonaNoEncontradaException extends RuntimeException {

    public PersonaNoEncontradaException(Long id) {
        super("No existe la persona con id: " + id);
    }
}