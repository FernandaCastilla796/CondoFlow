package com.condoflow.residence.infrastructure.adapter.out.persistence.mapper;

import com.condoflow.person.infrastructure.adapter.out.persistence.entity.PersonaJpaEntity;
import com.condoflow.residence.domain.model.EstadoResidencia;
import com.condoflow.residence.domain.model.Residencia;
import com.condoflow.residence.domain.model.TipoResidencia;
import com.condoflow.residence.infrastructure.adapter.out.persistence.entity.ResidenciaJpaEntity;
import com.condoflow.unit.infrastructure.adapter.out.persistence.entity.UnidadJpaEntity;

/**
 * La entidad JPA guarda referencias a PersonaJpaEntity/UnidadJpaEntity;
 * el dominio conserva sólo los ids.
 */
public final class ResidenciaPersistenceMapper {

    private ResidenciaPersistenceMapper() {
    }

    public static ResidenciaJpaEntity toJpa(Residencia r, PersonaJpaEntity persona, UnidadJpaEntity unidad) {
        return new ResidenciaJpaEntity(
                r.getResidenciaId(),
                persona,
                unidad,
                r.getTipoResidencia().name(),
                r.getFechaInicio(),
                r.getFechaFin(),
                r.getEstado().name());
    }

    public static Residencia toDomain(ResidenciaJpaEntity e) {
        // getPersona().getId() no dispara un SELECT extra: Hibernate conoce el id del proxy LAZY.
        return new Residencia(
                e.getId(),
                e.getPersona().getId(),
                e.getUnidad().getId(),
                TipoResidencia.valueOf(e.getTipoResidencia()),
                e.getFechaInicio(),
                e.getFechaFin(),
                EstadoResidencia.valueOf(e.getEstado()));
    }
}
