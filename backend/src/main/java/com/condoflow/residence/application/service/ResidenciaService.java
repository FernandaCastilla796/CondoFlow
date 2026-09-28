package com.condoflow.residence.application.service;

import com.condoflow.person.domain.exception.PersonaNoEncontradaException;
import com.condoflow.person.domain.port.in.ConsultarPersonaUseCase;
import com.condoflow.residence.domain.exception.ResidenciaNoEncontradaException;
import com.condoflow.residence.domain.exception.ResidenciaVigenteDuplicadaException;
import com.condoflow.residence.domain.model.Residencia;
import com.condoflow.residence.domain.port.in.ConsultarResidenciaUseCase;
import com.condoflow.residence.domain.port.in.FinalizarResidenciaUseCase;
import com.condoflow.residence.domain.port.in.RegistrarResidenciaCommand;
import com.condoflow.residence.domain.port.in.RegistrarResidenciaUseCase;
import com.condoflow.residence.domain.port.out.ResidenciaRepositoryPort;
import com.condoflow.unit.domain.exception.UnidadNoEncontradaException;
import com.condoflow.unit.domain.port.in.ConsultarUnidadUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Casos de uso del módulo residence.
 * Valida los padres mediante los Port IN públicos de person y unit, nunca con sus JpaRepository.
 */
@Service
public class ResidenciaService
        implements RegistrarResidenciaUseCase, ConsultarResidenciaUseCase, FinalizarResidenciaUseCase {

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
     * Límite transaccional: las verificaciones (persona, unidad, residencia vigente) y el INSERT
     * se ejecutan en la misma transacción. Si algo falla, no queda ningún efecto parcial.
     */
    @Override
    @Transactional
    public Residencia registrar(RegistrarResidenciaCommand command) {
        // 1. validar existencia de los padres (404)
        if (consultarPersona.buscarPorId(command.personaId()).isEmpty()) {
            throw new PersonaNoEncontradaException(command.personaId());
        }
        if (consultarUnidad.buscarPorId(command.unidadId()).isEmpty()) {
            throw new UnidadNoEncontradaException(command.unidadId());
        }
        // 2. validar conflicto de negocio (409)
        if (repositoryPort.existeVigente(command.personaId(), command.unidadId())) {
            throw new ResidenciaVigenteDuplicadaException(command.personaId(), command.unidadId());
        }
        // 3. construir dominio  4. guardar por Port OUT
        Residencia nueva = Residencia.nueva(command.personaId(), command.unidadId(),
                command.tipoResidencia(), command.fechaInicio());
        return repositoryPort.guardar(nueva);
    }

    /** Leer, cambiar de estado y guardar debe ser atómico: por eso también es transaccional. */
    @Override
    @Transactional
    public Residencia finalizar(Long residenciaId, LocalDate fechaFin) {
        Residencia actual = repositoryPort.buscarPorId(residenciaId)
                .orElseThrow(() -> new ResidenciaNoEncontradaException(residenciaId));
        return repositoryPort.guardar(actual.finalizar(fechaFin));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Residencia> buscarPorId(Long id) {
        return repositoryPort.buscarPorId(id);
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
