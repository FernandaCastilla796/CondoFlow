package com.condoflow.residence.domain.port.in;

import com.condoflow.residence.domain.model.Residencia;

import java.util.List;
import java.util.Optional;

public interface ConsultarResidenciaUseCase {

    Optional<Residencia> buscarPorId(Long id);

    /** Todas las residencias, para el listado del frontend (Guía 05). */
    List<Residencia> listar();

    List<Residencia> listarPorPersona(Long personaId);
}
