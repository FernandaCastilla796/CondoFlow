package com.condoflow.person.infrastructure.adapter.out.persistence.mapper;

import com.condoflow.person.domain.model.EstadoPersona;
import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.infrastructure.adapter.out.persistence.entity.PersonaJpaEntity;

/** Conversión explícita entre el modelo de dominio y la entidad JPA. */
public final class PersonaPersistenceMapper {

    private PersonaPersistenceMapper() {
    }

    public static PersonaJpaEntity toEntity(Persona persona) {
        return new PersonaJpaEntity(
                persona.getPersonaId(),
                persona.getNombre(),
                persona.getApellido(),
                persona.getDocumento(),
                persona.getTelefono(),
                persona.getCorreoElectronico(),
                persona.getEstado().name());
    }

    public static Persona toDomain(PersonaJpaEntity entity) {
        return new Persona(
                entity.getId(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getDocumento(),
                entity.getTelefono(),
                entity.getCorreoElectronico(),
                EstadoPersona.valueOf(entity.getEstado()));
    }
}
