package com.condoflow.unit.domain.port.in;

import com.condoflow.unit.domain.model.Unidad;

import java.util.List;
import java.util.Optional;

/** Port IN de consulta de unidades. Otros módulos (residence) lo usan para validar que una unidad exista. */
public interface ConsultarUnidadUseCase {

    Optional<Unidad> buscarPorId(Long id);

    List<Unidad> listar();
}
