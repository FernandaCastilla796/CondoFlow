package com.condoflow.residence.infrastructure.adapter.out.persistence.entity;

import com.condoflow.person.infrastructure.adapter.out.persistence.entity.PersonaJpaEntity;
import com.condoflow.unit.infrastructure.adapter.out.persistence.entity.UnidadJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Mapeo JPA de condoflow.residencia.
 * @ManyToOne: muchas residencias apuntan a una misma persona (y a una misma unidad).
 * @JoinColumn: nombra la columna FK que ya existe en PostgreSQL (fk_residencia_persona, fk_residencia_unidad).
 */
@Entity
@Table(name = "residencia", schema = "condoflow")
public class ResidenciaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "residencia_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "persona_id", nullable = false)
    private PersonaJpaEntity persona;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unidad_id", nullable = false)
    private UnidadJpaEntity unidad;

    @Column(name = "tipo_residencia", nullable = false, length = 50)
    private String tipoResidencia;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    protected ResidenciaJpaEntity() {
        // requerido por JPA
    }

    public ResidenciaJpaEntity(Long id, PersonaJpaEntity persona, UnidadJpaEntity unidad, String tipoResidencia,
                               LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        this.id = id;
        this.persona = persona;
        this.unidad = unidad;
        this.tipoResidencia = tipoResidencia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public PersonaJpaEntity getPersona() {
        return persona;
    }

    public UnidadJpaEntity getUnidad() {
        return unidad;
    }

    public String getTipoResidencia() {
        return tipoResidencia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public String getEstado() {
        return estado;
    }
}
