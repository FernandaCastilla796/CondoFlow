package com.condoflow.person.application.service;

import com.condoflow.person.domain.exception.CorreoPersonaDuplicadoException;
import com.condoflow.person.domain.exception.PersonaNoEncontradaException;
import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.domain.port.in.ActualizarPersonaUseCase;
import com.condoflow.person.domain.port.in.ConsultarPersonaUseCase;
import com.condoflow.person.domain.port.in.EliminarPersonaUseCase;
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
public class PersonaService implements RegistrarPersonaUseCase, ConsultarPersonaUseCase,
        ActualizarPersonaUseCase, EliminarPersonaUseCase {

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

    /**
     * PUT: reemplaza los datos de una persona que ya existe (404 si no existe).
     * El correo puede ser el mismo que ya tenía, pero no el de otra persona (409).
     * Cambiar el estado a INACTIVO es la baja lógica.
     */
    @Override
    @Transactional
    public Persona actualizar(Long id, Persona datos) {
        if (repositoryPort.buscarPorId(id).isEmpty()) {
            throw new PersonaNoEncontradaException(id);
        }
        if (repositoryPort.existePorCorreoElectronicoEnOtraPersona(datos.getCorreoElectronico(), id)) {
            throw new CorreoPersonaDuplicadoException(datos.getCorreoElectronico());
        }
        return repositoryPort.guardar(datos.conId(id));
    }

    /** DELETE: 404 si no existe; 409 si tiene residencias o reservas (lo detecta el adaptador por la FK). */
    @Override
    @Transactional
    public void eliminar(Long id) {
        if (repositoryPort.buscarPorId(id).isEmpty()) {
            throw new PersonaNoEncontradaException(id);
        }
        repositoryPort.eliminar(id);
    }
}
