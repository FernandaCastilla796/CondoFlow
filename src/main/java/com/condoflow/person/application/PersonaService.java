package com.condoflow.person.application;

import com.condoflow.person.domain.Persona;
import com.condoflow.person.domain.port.PersonaRepository;
import com.condoflow.person.exception.CorreoPersonaDuplicadoException;
import com.condoflow.person.exception.PersonaNoEncontradaException;

import java.util.List;

public class PersonaService {

    private final PersonaRepository repository;

    public PersonaService(PersonaRepository repository) {
        this.repository = repository;
    }

    public Persona registrar(Persona persona) {
        if (repository.existePorCorreoElectronico(persona.getCorreoElectronico())) {
            throw new CorreoPersonaDuplicadoException(
                    persona.getCorreoElectronico()
            );
        }

        return repository.guardar(persona);
    }

    public Persona obtener(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() ->
                        new PersonaNoEncontradaException(id)
                );
    }

    public List<Persona> listar() {
        return repository.listarTodos();
    }
}