package com.condoflow;

import com.condoflow.person.application.PersonaService;
import com.condoflow.person.domain.Persona;
import com.condoflow.person.infrastructure.memory.PersonaRepositoryEnMemoria;
import com.condoflow.residence.application.command.RegistrarResidenciaCommand;
import com.condoflow.residence.domain.Residencia;
import com.condoflow.residence.domain.TipoResidencia;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        // ===== Capítulo 02: servicio + repositorio en memoria =====
        var repository = new PersonaRepositoryEnMemoria();
        var service = new PersonaService(repository);

        Persona juan = service.registrar(new Persona(1L, "Juan", "Perez", "juan.perez@gmail.com"));
        service.registrar(new Persona(2L, "Maria", "Gomez", "maria.gomez@gmail.com"));

        System.out.println("Cantidad de personas: " + service.listar().size());
        System.out.println("Persona encontrada: " + service.obtener(1L).getNombre());

        // Caso negativo 1: id inexistente -> PersonaNoEncontradaException
        try {
            service.obtener(999L);
        } catch (RuntimeException ex) {
            System.out.println("ERROR CONTROLADO: " + ex.getMessage());
        }

        // Caso negativo 2: correo duplicado (UNIQUE) -> CorreoPersonaDuplicadoException
        try {
            service.registrar(new Persona(3L, "Pedro", "Lopez", "JUAN.PEREZ@gmail.com"));
        } catch (RuntimeException ex) {
            System.out.println("ERROR CONTROLADO: " + ex.getMessage());
        }

        // ===== Capítulo 01: relación 1:N Persona -> Residencia =====
        // El record transporta sólo los datos que necesita la operación de registro.
        var command = new RegistrarResidenciaCommand(
                juan.getPersonaId(), 10L, TipoResidencia.PROPIETARIO, LocalDate.of(2025, 1, 15));

        Residencia primera = new Residencia(1L, command.personaId(), command.unidadId(),
                command.tipoResidencia(), command.fechaInicio());
        Residencia segunda = new Residencia(2L, juan.getPersonaId(), 11L,
                TipoResidencia.INQUILINO, LocalDate.of(2026, 3, 1));

        juan.agregarResidencia(primera);
        juan.agregarResidencia(segunda);

        // RN-01: vigencia temporal
        primera.finalizar(LocalDate.of(2026, 2, 28));

        System.out.println("Residencias de " + juan.getNombre() + ": " + juan.getResidencias().size());
        juan.getResidencias().forEach(r -> System.out.println(
                "  - unidad " + r.getUnidadId() + " | " + r.getTipoResidencia() + " | " + r.getEstado()));

        // Caso negativo 3: fecha de fin anterior a la de inicio
        try {
            segunda.finalizar(LocalDate.of(2020, 1, 1));
        } catch (RuntimeException ex) {
            System.out.println("ERROR CONTROLADO: " + ex.getMessage());
        }
    }
}
