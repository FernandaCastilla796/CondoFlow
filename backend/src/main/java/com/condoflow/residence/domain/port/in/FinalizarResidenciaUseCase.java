package com.condoflow.residence.domain.port.in;

import com.condoflow.residence.domain.model.Residencia;

import java.time.LocalDate;

/** Port IN: RN-01, transición VIGENTE → FINALIZADA registrando la fecha de fin. */
public interface FinalizarResidenciaUseCase {

    Residencia finalizar(Long residenciaId, LocalDate fechaFin);
}
