package com.condoflow.person.domain;

import java.util.Optional;

public interface PersonaRepository {

    Persona guardar(Persona persona);

    Optional<Persona> buscarPorId(Long id);
}