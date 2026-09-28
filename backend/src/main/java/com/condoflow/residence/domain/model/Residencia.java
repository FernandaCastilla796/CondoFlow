package com.condoflow.residence.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Residencia: vincula una persona con una unidad durante un período (RN-01).
 * Dominio puro: conserva sólo los ids de persona y unidad; no tiene @Entity ni @ManyToOne.
 */
public class Residencia {

    private final Long residenciaId;
    private final Long personaId;
    private final Long unidadId;
    private final TipoResidencia tipoResidencia;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final EstadoResidencia estado;

    public Residencia(Long residenciaId, Long personaId, Long unidadId, TipoResidencia tipoResidencia,
                      LocalDate fechaInicio, LocalDate fechaFin, EstadoResidencia estado) {
        this.residenciaId = residenciaId;
        this.personaId = Objects.requireNonNull(personaId, "La persona es obligatoria");
        this.unidadId = Objects.requireNonNull(unidadId, "La unidad es obligatoria");
        this.tipoResidencia = Objects.requireNonNull(tipoResidencia, "El tipo de residencia es obligatorio");
        this.fechaInicio = Objects.requireNonNull(fechaInicio, "La fecha de inicio es obligatoria");
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
        this.fechaFin = fechaFin;
        if (fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    /** Toda residencia nueva nace VIGENTE y sin fecha de fin. */
    public static Residencia nueva(Long personaId, Long unidadId, TipoResidencia tipo, LocalDate fechaInicio) {
        return new Residencia(null, personaId, unidadId, tipo, fechaInicio, null, EstadoResidencia.VIGENTE);
    }

    /** Transición VIGENTE → FINALIZADA (RN-01). */
    public Residencia finalizar(LocalDate fechaFin) {
        Objects.requireNonNull(fechaFin, "La fecha de fin es obligatoria");
        if (!estaVigente()) {
            throw new IllegalStateException("La residencia " + residenciaId + " ya está finalizada");
        }
        return new Residencia(residenciaId, personaId, unidadId, tipoResidencia, fechaInicio,
                fechaFin, EstadoResidencia.FINALIZADA);
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
