package com.condoflow.residence.application.service;

import com.condoflow.person.domain.exception.PersonaNoEncontradaException;
import com.condoflow.person.domain.port.in.ConsultarPersonaUseCase;
import com.condoflow.residence.application.command.ActualizarResidenciaCommand;
import com.condoflow.residence.application.command.RegistrarResidenciaCommand;
import com.condoflow.residence.domain.exception.ResidenciaNoEncontradaException;
import com.condoflow.residence.domain.exception.ResidenciaVigenteDuplicadaException;
import com.condoflow.residence.domain.model.EstadoResidencia;
import com.condoflow.residence.domain.model.Residencia;
import com.condoflow.residence.domain.port.in.ActualizarResidenciaUseCase;
import com.condoflow.residence.domain.port.in.ConsultarResidenciaUseCase;
import com.condoflow.residence.domain.port.in.EliminarResidenciaUseCase;
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
public class ResidenciaService implements RegistrarResidenciaUseCase, ConsultarResidenciaUseCase,
        ActualizarResidenciaUseCase, EliminarResidenciaUseCase {

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
        validarPadres(command.personaId(), command.unidadId());
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

    /**
     * PUT (Guía 07 del frontend): cambiar personaId reasigna la residencia a otra persona (UPDATE de la FK).
     * Se valida igual que al registrar: la residencia y los padres deben existir (404) y no puede quedar
     * otra residencia VIGENTE de la misma persona en la misma unidad (409). El dominio valida las fechas (400).
     */
    @Override
    @Transactional
    public Residencia actualizar(Long id, ActualizarResidenciaCommand command) {
        if (repositoryPort.buscarPorId(id).isEmpty()) {
            throw new ResidenciaNoEncontradaException(id);
        }
        validarPadres(command.personaId(), command.unidadId());

        Residencia actualizada = new Residencia(id, command.personaId(), command.unidadId(),
                command.tipoResidencia(), command.fechaInicio(), command.fechaFin(), command.estado());

        if (actualizada.getEstado() == EstadoResidencia.VIGENTE
                && repositoryPort.existeVigenteEnOtraResidencia(command.personaId(), command.unidadId(), id)) {
            throw new ResidenciaVigenteDuplicadaException(command.personaId(), command.unidadId());
        }
        return repositoryPort.guardar(actualizada);
    }

    /** DELETE: 404 si no existe. Ninguna tabla referencia a residencia, así que no hay conflicto de FK. */
    @Override
    @Transactional
    public void eliminar(Long id) {
        if (repositoryPort.buscarPorId(id).isEmpty()) {
            throw new ResidenciaNoEncontradaException(id);
        }
        repositoryPort.eliminar(id);
    }

    private void validarPadres(Long personaId, Long unidadId) {
        if (consultarPersona.buscarPorId(personaId).isEmpty()) {
            throw new PersonaNoEncontradaException(personaId);
        }
        if (consultarUnidad.buscarPorId(unidadId).isEmpty()) {
            throw new UnidadNoEncontradaException(unidadId);
        }
    }
}
