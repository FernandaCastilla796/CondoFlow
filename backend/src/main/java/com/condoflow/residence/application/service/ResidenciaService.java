package com.condoflow.residence.application.service;

import com.condoflow.person.domain.exception.PersonaNoEncontradaException;
import com.condoflow.person.domain.port.in.ConsultarPersonaUseCase;
import com.condoflow.residence.application.command.RegistrarResidenciaCommand;
import com.condoflow.residence.domain.exception.ResidenciaVigenteDuplicadaException;
import com.condoflow.residence.domain.model.Residencia;
import com.condoflow.residence.domain.port.in.ConsultarResidenciaUseCase;
import com.condoflow.residence.domain.port.in.RegistrarResidenciaUseCase;
import com.condoflow.residence.domain.port.out.ResidenciaRepositoryPort;
import com.condoflow.unit.domain.exception.UnidadNoEncontradaException;
import com.condoflow.unit.domain.port.in.ConsultarUnidadUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Casos de uso del módulo residence.
 * Valida los padres mediante los Port IN públicos de person y unit, nunca con sus JpaRepository (Capítulo 07).
 */
@Service
public class ResidenciaService implements RegistrarResidenciaUseCase, ConsultarResidenciaUseCase {

    private final ResidenciaRepositoryPort repositoryPort;
    private final ConsultarPersonaUseCase consultarPersona;
    private final ConsultarUnidadUseCase consultarUnidad;

    public ResidenciaService(ResidenciaRepositoryPort repositoryPort,
                             ConsultarPersonaUseCase consultarPersona,
                             ConsultarUnidadUseCase consultarUnidad) {
        this.repositoryPort = repositoryPort;
        this.consultarPersona = consultarPersona;
        this.consultarUnidad = consultarUnidad;
    }

    /**
     * Límite transaccional (Capítulo 08): las verificaciones y el INSERT forman una sola operación.
     * Si algo falla, no queda ningún efecto parcial.
     */
    @Override
    @Transactional
    public Residencia registrar(RegistrarResidenciaCommand command) {
        // 1. validar existencia del padre
        if (consultarPersona.buscarPorId(command.personaId()).isEmpty()) {
            throw new PersonaNoEncontradaException(command.personaId());
        }
        if (consultarUnidad.buscarPorId(command.unidadId()).isEmpty()) {
            throw new UnidadNoEncontradaException(command.unidadId());
        }
        // 2. validar conflicto de negocio
        if (repositoryPort.existeVigente(command.personaId(), command.unidadId())) {
            throw new ResidenciaVigenteDuplicadaException(command.personaId(), command.unidadId());
        }
        // 3. construir dominio
        Residencia nueva = Residencia.nueva(command.personaId(), command.unidadId(),
                command.tipoResidencia(), command.fechaInicio());
        // 4. guardar por Port OUT
        return repositoryPort.guardar(nueva);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Residencia> buscarPorId(Long id) {
        return repositoryPort.buscarPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Residencia> listar() {
        return repositoryPort.listar();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Residencia> listarPorPersona(Long personaId) {
        if (consultarPersona.buscarPorId(personaId).isEmpty()) {
            throw new PersonaNoEncontradaException(personaId);
        }
        return repositoryPort.listarPorPersonaId(personaId);
    }
}
