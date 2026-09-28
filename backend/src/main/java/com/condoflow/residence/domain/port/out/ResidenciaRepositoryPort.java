package com.condoflow.residence.domain.port.out;

import com.condoflow.residence.domain.model.Residencia;

import java.util.List;
import java.util.Optional;

public interface ResidenciaRepositoryPort {

    Residencia guardar(Residencia residencia);

    Optional<Residencia> buscarPorId(Long id);

    List<Residencia> listarPorPersonaId(Long personaId);

    /** Respalda el índice UNIQUE parcial uq_residencia_vigente_persona_unidad. */
    boolean existeVigente(Long personaId, Long unidadId);
}
