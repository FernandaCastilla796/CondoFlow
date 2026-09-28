package com.condoflow.residence.domain.port.in;

import com.condoflow.residence.domain.model.Residencia;

import java.util.List;
import java.util.Optional;

public interface ConsultarResidenciaUseCase {

    Optional<Residencia> buscarPorId(Long id);

    List<Residencia> listarPorPersona(Long personaId);
}
