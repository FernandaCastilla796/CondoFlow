package com.condoflow.residence.domain.port.in;

import com.condoflow.residence.domain.model.Residencia;

/** Port IN: RF-14 registrar residencia vigente. */
public interface RegistrarResidenciaUseCase {

    Residencia registrar(Residencia residencia);
}
