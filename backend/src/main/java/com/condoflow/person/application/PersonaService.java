package com.condoflow.person.application;

import com.condoflow.person.domain.model.Persona;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Capítulo 04: servicio temporal en memoria para aprender Spring MVC.
 * Los datos se pierden al reiniciar la aplicación; en el Capítulo 05 se reemplaza por PostgreSQL.
 */
@Service
public class PersonaService {

    private final List<Persona> personas = new ArrayList<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    public synchronized Persona registrar(Persona persona) {
        Persona guardada = persona.conId(secuencia.incrementAndGet());
        personas.add(guardada);
        return guardada;
    }

    public synchronized List<Persona> listar(String filtro) {
        if (filtro == null || filtro.isBlank()) {
            return List.copyOf(personas);
        }
        String texto = filtro.trim().toLowerCase(Locale.ROOT);
        return personas.stream()
                .filter(p -> p.nombreCompleto().toLowerCase(Locale.ROOT).contains(texto)
                        || p.getCorreoElectronico().contains(texto))
                .toList();
    }

    public synchronized Optional<Persona> buscarPorId(Long id) {
        return personas.stream()
                .filter(p -> p.getPersonaId().equals(id))
                .findFirst();
    }
}
