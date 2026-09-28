package com.condoflow.unit.domain.port.out;

import com.condoflow.unit.domain.model.Unidad;

import java.util.List;
import java.util.Optional;

public interface UnidadRepositoryPort {

    Optional<Unidad> buscarPorId(Long id);

    List<Unidad> listar();
}
