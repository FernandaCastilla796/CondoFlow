package com.condoflow.residence.domain.port.in;

import com.condoflow.residence.application.command.ActualizarResidenciaCommand;
import com.condoflow.residence.domain.model.Residencia;

/** Port IN: reemplazar los datos de una residencia, incluida la persona a la que pertenece. */
public interface ActualizarResidenciaUseCase {

    Residencia actualizar(Long id, ActualizarResidenciaCommand command);
}
