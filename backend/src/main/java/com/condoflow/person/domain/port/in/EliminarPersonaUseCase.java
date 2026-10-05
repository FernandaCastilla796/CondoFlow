package com.condoflow.person.domain.port.in;

/** Port IN: eliminar una persona que no tiene registros asociados (DELETE, Guía 06 del frontend). */
public interface EliminarPersonaUseCase {

    void eliminar(Long id);
}
