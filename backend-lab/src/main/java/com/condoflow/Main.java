package com.condoflow;

import com.condoflow.person.application.PersonaService;
import com.condoflow.person.domain.Persona;
import com.condoflow.person.infrastructure.memory.PersonaRepositoryEnMemoria;

public class Main {

    public static void main(String[] args) {

        // Repositorio en memoria
        var repository = new PersonaRepositoryEnMemoria();

        // Servicio
        var service = new PersonaService(repository);

        // Registrar primera persona
        Persona persona1 = new Persona(
                1L,
                "Juan",
                "Perez",
                "juan.perez@gmail.com"
        );

        service.registrar(persona1);

        // Registrar segunda persona
        Persona persona2 = new Persona(
                2L,
                "Maria",
                "Gomez",
                "maria.gomez@gmail.com"
        );

        service.registrar(persona2);

        // Listar personas
        System.out.println("Cantidad de personas: "
                + service.listar().size());

        // Buscar persona existente
        System.out.println("Persona encontrada: "
                + service.obtener(1L).getNombre());

        // Buscar persona inexistente
        try {
            service.obtener(999L);
        } catch (RuntimeException ex) {
            System.out.println("ERROR CONTROLADO: "
                    + ex.getMessage());
        }

        // Intentar registrar correo duplicado
        try {
            Persona personaDuplicada = new Persona(
                    3L,
                    "Pedro",
                    "Lopez",
                    "juan.perez@gmail.com"
            );

            service.registrar(personaDuplicada);

        } catch (RuntimeException ex) {
            System.out.println("ERROR CONTROLADO: "
                    + ex.getMessage());
        }
    }
}