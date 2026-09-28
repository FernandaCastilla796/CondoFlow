package com.condoflow.person.exception;

public class CorreoPersonaDuplicadoException extends RuntimeException {

    public CorreoPersonaDuplicadoException(String correo) {
        super("Ya existe una persona con el correo: " + correo);
    }
}