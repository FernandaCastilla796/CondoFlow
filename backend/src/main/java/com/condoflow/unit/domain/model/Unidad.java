package com.condoflow.unit.domain.model;

import java.util.Objects;

/** Unidad habitacional del condominio (departamento, casa, local...). */
public class Unidad {

    private final Long unidadId;
    private final String numeroUnidad;
    private final String tipo;
    private final EstadoUnidad estado;

    public Unidad(Long unidadId, String numeroUnidad, String tipo, EstadoUnidad estado) {
        this.unidadId = unidadId;
        this.numeroUnidad = Objects.requireNonNull(numeroUnidad, "El número de unidad es obligatorio");
        this.tipo = Objects.requireNonNull(tipo, "El tipo de unidad es obligatorio");
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
    }

    public boolean estaActiva() {
        return estado == EstadoUnidad.ACTIVA;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public String getNumeroUnidad() {
        return numeroUnidad;
    }

    public String getTipo() {
        return tipo;
    }

    public EstadoUnidad getEstado() {
        return estado;
    }
}
