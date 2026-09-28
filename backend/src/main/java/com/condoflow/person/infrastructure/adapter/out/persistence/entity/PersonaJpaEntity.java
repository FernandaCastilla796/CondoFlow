package com.condoflow.person.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Mapeo JPA de la tabla condoflow.persona (V1 + V3).
 * Es un detalle de infraestructura: el dominio usa Persona, no esta clase.
 */
@Entity
@Table(name = "persona", schema = "condoflow")
public class PersonaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "persona_id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "documento", nullable = false, length = 50)
    private String documento;

    @Column(name = "telefono", nullable = false, length = 30)
    private String telefono;

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 150)
    private String correoElectronico;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    protected PersonaJpaEntity() {
        // requerido por JPA
    }

    public PersonaJpaEntity(Long id, String nombre, String apellido, String documento,
                            String telefono, String correoElectronico, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getEstado() {
        return estado;
    }
}
