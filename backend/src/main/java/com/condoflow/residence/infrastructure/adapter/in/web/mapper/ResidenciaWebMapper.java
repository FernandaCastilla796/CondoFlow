package com.condoflow.residence.infrastructure.adapter.in.web.mapper;

import com.condoflow.residence.application.command.ActualizarResidenciaCommand;
import com.condoflow.residence.application.command.RegistrarResidenciaCommand;
import com.condoflow.residence.domain.model.Residencia;
import com.condoflow.residence.infrastructure.adapter.in.web.dto.ActualizarResidenciaRequest;
import com.condoflow.residence.infrastructure.adapter.in.web.dto.CrearResidenciaRequest;
import com.condoflow.residence.infrastructure.adapter.in.web.dto.ResidenciaResponse;

public final class ResidenciaWebMapper {

    private ResidenciaWebMapper() {
    }

    public static RegistrarResidenciaCommand toCommand(CrearResidenciaRequest request) {
        return new RegistrarResidenciaCommand(request.personaId(), request.unidadId(),
                request.tipoResidencia(), request.fechaInicio());
    }

    public static ActualizarResidenciaCommand toCommand(ActualizarResidenciaRequest request) {
        return new ActualizarResidenciaCommand(request.personaId(), request.unidadId(), request.tipoResidencia(),
                request.fechaInicio(), request.fechaFin(), request.estado());
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
