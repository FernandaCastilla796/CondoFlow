package com.condoflow.person.domain.exception;

/** Regla de negocio: el correo electrónico identifica a una sola persona (UNIQUE). */
public class CorreoPersonaDuplicadoException extends RuntimeException {

    public CorreoPersonaDuplicadoException(String correo) {
        super("Ya existe una persona registrada con el correo " + correo);
    }
}
