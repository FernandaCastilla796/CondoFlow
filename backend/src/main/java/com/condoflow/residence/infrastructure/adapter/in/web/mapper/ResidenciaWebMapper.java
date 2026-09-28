package com.condoflow.residence.infrastructure.adapter.in.web.mapper;

import com.condoflow.residence.domain.model.Residencia;
import com.condoflow.residence.infrastructure.adapter.in.web.dto.CrearResidenciaRequest;
import com.condoflow.residence.infrastructure.adapter.in.web.dto.ResidenciaResponse;

public final class ResidenciaWebMapper {

    private ResidenciaWebMapper() {
    }

    public static Residencia toDomain(CrearResidenciaRequest request) {
        return Residencia.nueva(request.personaId(), request.unidadId(),
                request.tipoResidencia(), request.fechaInicio());
    }

    public static ResidenciaResponse toResponse(Residencia r) {
        return new ResidenciaResponse(
                r.getResidenciaId(),
                r.getPersonaId(),
                r.getUnidadId(),
                r.getTipoResidencia().name(),
                r.getFechaInicio(),
                r.getFechaFin(),
                r.getEstado().name());
    }
}
