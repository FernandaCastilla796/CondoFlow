package com.condoflow.person.domain;

import com.condoflow.residence.domain.Residencia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Persona {

    private Long personaId;
    private String nombre;
    private String apellido;
    private String correoElectronico;

    private final List<Residencia> residencias = new ArrayList<>();

    public Persona(Long personaId, String nombre, String apellido, String correoElectronico) {
        this.personaId = personaId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correoElectronico = correoElectronico;
    }

    public Long getPersonaId() {
        return personaId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void agregarResidencia(Residencia residencia) {
        if (residencia == null) {
            throw new IllegalArgumentException("La residencia no puede ser null");
        }

        residencias.add(residencia);
    }

    public List<Residencia> getResidencias() {
        return Collections.unmodifiableList(residencias);
    }
}