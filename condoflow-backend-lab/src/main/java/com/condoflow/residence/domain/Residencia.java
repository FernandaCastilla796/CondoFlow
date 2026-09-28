package com.condoflow.residence.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Residencia {

    private final Long residenciaId;
    private final Long personaId;
    private final Long unidadId;
    private final TipoResidencia tipoResidencia;
    private final LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoResidencia estado;

    public Residencia(Long residenciaId, Long personaId, Long unidadId,
                      TipoResidencia tipoResidencia, LocalDate fechaInicio) {
        this.residenciaId = Objects.requireNonNull(residenciaId, "El ID de residencia no puede ser null");
        this.personaId = Objects.requireNonNull(personaId, "El ID de persona no puede ser null");
        this.unidadId = Objects.requireNonNull(unidadId, "El ID de unidad no puede ser null");
        this.tipoResidencia = Objects.requireNonNull(tipoResidencia, "El tipo de residencia no puede ser null");
        this.fechaInicio = Objects.requireNonNull(fechaInicio, "La fecha de inicio no puede ser null");
        this.estado = EstadoResidencia.VIGENTE;
    }

    /**
     * RN-01: la residencia tiene vigencia temporal. Al finalizar se registra la fecha de fin,
     * que no puede ser anterior a la fecha de inicio.
     */
    public void finalizar(LocalDate fechaFin) {
        Objects.requireNonNull(fechaFin, "La fecha de fin no puede ser null");
        if (estado == EstadoResidencia.FINALIZADA) {
            throw new IllegalStateException("La residencia ya está finalizada");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        this.fechaFin = fechaFin;
        this.estado = EstadoResidencia.FINALIZADA;
    }

    public boolean estaVigente() {
        return estado == EstadoResidencia.VIGENTE;
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

    public TipoResidencia getTipoResidencia() {
        return tipoResidencia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public EstadoResidencia getEstado() {
        return estado;
    }
}
