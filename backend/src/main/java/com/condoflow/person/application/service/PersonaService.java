package com.condoflow.person.application.service;

import com.condoflow.person.domain.exception.CorreoPersonaDuplicadoException;
import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.domain.port.in.ConsultarPersonaUseCase;
import com.condoflow.person.domain.port.in.RegistrarPersonaUseCase;
import com.condoflow.person.domain.port.out.PersonaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Casos de uso del módulo person. Depende sólo del Port OUT: no conoce JPA, Spring Data ni PostgreSQL.
 */
@Service
public class PersonaService implements RegistrarPersonaUseCase, ConsultarPersonaUseCase {

    private final PersonaRepositoryPort repositoryPort;

    public PersonaService(PersonaRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    /** La verificación del correo y el INSERT forman una sola unidad de trabajo. */
    @Override
    @Transactional
    public Persona registrar(Persona persona) {
        if (repositoryPort.existePorCorreoElectronico(persona.getCorreoElectronico())) {
            throw new CorreoPersonaDuplicadoException(persona.getCorreoElectronico());
        }
        return repositoryPort.guardar(persona);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Persona> buscarPorId(Long id) {
        return repositoryPort.buscarPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Persona> listar(String filtro) {
        return repositoryPort.listar(filtro == null ? null : filtro.trim());
    }
}
