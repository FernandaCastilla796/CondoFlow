package com.condoflow.residence.domain;

public class Residencia {

    private Long residenciaId;
    private Long personaId;
    private Long unidadId;

    public Residencia(Long residenciaId, Long personaId, Long unidadId) {
        if (residenciaId == null) {
            throw new IllegalArgumentException("El ID de residencia no puede ser null");
        }

        if (personaId == null) {
            throw new IllegalArgumentException("El ID de persona no puede ser null");
        }

        if (unidadId == null) {
            throw new IllegalArgumentException("El ID de unidad no puede ser null");
        }

        this.residenciaId = residenciaId;
        this.personaId = personaId;
        this.unidadId = unidadId;
    }

    public Long getResidenciaId() {
        return residenciaId;
    }

    public Long getPersonaId() {
        return personaId;
    }

    public Long getUnidadId() {
        return unidadId;
    }
}