package com.condoflow.unit.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Mapeo JPA de la tabla condoflow.unidad. */
@Entity
@Table(name = "unidad", schema = "condoflow")
public class UnidadJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unidad_id")
    private Long id;

    @Column(name = "numero_unidad", nullable = false, unique = true, length = 20)
    private String numeroUnidad;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    protected UnidadJpaEntity() {
        // requerido por JPA
    }

    public Long getId() {
        return id;
    }

    public String getNumeroUnidad() {
        return numeroUnidad;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }
}
