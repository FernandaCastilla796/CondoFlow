package com.condoflow.person.infrastructure.memory;

import com.condoflow.person.domain.Persona;
import com.condoflow.person.domain.port.PersonaRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PersonaRepositoryEnMemoria implements PersonaRepository {

    private final Map<Long, Persona> datos = new LinkedHashMap<>();

    @Override
    public Persona guardar(Persona persona) {
        datos.put(persona.getPersonaId(), persona);
        return persona;
    }

    @Override
    public Optional<Persona> buscarPorId(Long id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public List<Persona> listarTodos() {
        return new ArrayList<>(datos.values());
    }

    @Override
    public boolean existePorCorreoElectronico(String correoElectronico) {
        return datos.values().stream()
                .anyMatch(persona ->
                        persona.getCorreoElectronico().equalsIgnoreCase(correoElectronico));
    }
}