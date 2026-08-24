package com.condoflow;

import com.condoflow.person.domain.Persona;
import com.condoflow.residence.domain.Residencia;

public class Main {

    public static void main(String[] args) {

        Persona persona = new Persona(
                1L,
                "Juan",
                "Perez",
                "juan.perez@gmail.com"
        );

        Residencia residencia1 = new Residencia(
                1L,
                1L,
                101L
        );

        Residencia residencia2 = new Residencia(
                2L,
                1L,
                102L
        );

        persona.agregarResidencia(residencia1);
        persona.agregarResidencia(residencia2);

        System.out.println("Persona: "
                + persona.getNombre() + " "
                + persona.getApellido());

        System.out.println("Correo: "
                + persona.getCorreoElectronico());

        System.out.println("Cantidad de residencias: "
                + persona.getResidencias().size());
    }
}