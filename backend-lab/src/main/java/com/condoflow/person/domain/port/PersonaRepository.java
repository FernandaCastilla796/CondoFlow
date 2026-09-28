package com.condoflow.person.domain.port;

import com.condoflow.person.domain.Persona;

import java.util.List;
import java.util.Optional;

public interface PersonaRepository {

    Persona guardar(Persona persona);

    Optional<Persona> buscarPorId(Long id);

    List<Persona> listarTodos();

    boolean existePorCorreoElectronico(String correoElectronico);
}