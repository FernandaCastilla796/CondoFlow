package com.condoflow.person.domain.model;

import java.util.Locale;
import java.util.Objects;

/**
 * Persona relacionada con el condominio (residente, personal o administrador).
 * Clase de dominio: no conoce Spring, JPA ni PostgreSQL.
 */
public class Persona {

    private final Long personaId;
    private final String nombre;
    private final String apellido;
    private final String documento;
    private final String telefono;
    private final String correoElectronico;
    private final EstadoPersona estado;

    public Persona(Long personaId, String nombre, String apellido, String documento,
                   String telefono, String correoElectronico, EstadoPersona estado) {
        this.personaId = personaId;
        this.nombre = requerido(nombre, "nombre");
        this.apellido = requerido(apellido, "apellido");
        this.documento = requerido(documento, "documento");
        this.telefono = requerido(telefono, "telefono");
        this.correoElectronico = normalizarCorreo(correoElectronico);
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
    }

    /** Crea una persona nueva: todavía sin id y en estado ACTIVO. */
    public static Persona nueva(String nombre, String apellido, String documento,
                                String telefono, String correoElectronico) {
        return new Persona(null, nombre, apellido, documento, telefono, correoElectronico, EstadoPersona.ACTIVO);
    }

    /** El correo es la clave natural (UNIQUE): se guarda sin espacios y en minúsculas. */
    public static String normalizarCorreo(String correo) {
        return requerido(correo, "correoElectronico").toLowerCase(Locale.ROOT);
    }

    private static String requerido(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
        return valor.trim();
    }

    public Persona conId(Long id) {
        return new Persona(id, nombre, apellido, documento, telefono, correoElectronico, estado);
    }

    public String nombreCompleto() {
        return nombre + " " + apellido;
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

    public String getDocumento() {
        return documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public EstadoPersona getEstado() {
        return estado;
    }
}
