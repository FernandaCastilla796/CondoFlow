package com.condoflow.residence.domain.port.out;

import com.condoflow.residence.domain.model.Residencia;

import java.util.List;
import java.util.Optional;

public interface ResidenciaRepositoryPort {

    /** Inserta si la residencia no tiene id; si lo tiene, actualiza esa fila (incluida la FK persona_id). */
    Residencia guardar(Residencia residencia);

    Optional<Residencia> buscarPorId(Long id);

    List<Residencia> listar();

    List<Residencia> listarPorPersonaId(Long personaId);

    /** Respalda el índice UNIQUE parcial uq_residencia_vigente_persona_unidad. */
    boolean existeVigente(Long personaId, Long unidadId);

    /** Igual que el anterior, pero ignora a la propia residencia (para el PUT). */
    boolean existeVigenteEnOtraResidencia(Long personaId, Long unidadId, Long residenciaId);

    void eliminar(Long id);
}
