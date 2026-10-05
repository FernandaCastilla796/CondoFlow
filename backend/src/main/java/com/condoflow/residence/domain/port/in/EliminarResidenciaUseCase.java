package com.condoflow.residence.domain.port.in;

/** Port IN: eliminar una residencia registrada por error (DELETE, Guía 07 del frontend). */
public interface EliminarResidenciaUseCase {

    void eliminar(Long id);
}
