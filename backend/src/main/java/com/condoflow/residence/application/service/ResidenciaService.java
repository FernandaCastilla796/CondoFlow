package com.condoflow.residence.application.service;

import com.condoflow.person.domain.exception.PersonaNoEncontradaException;
import com.condoflow.person.domain.port.in.ConsultarPersonaUseCase;
import com.condoflow.residence.domain.exception.ResidenciaVigenteDuplicadaException;
import com.condoflow.residence.domain.model.Residencia;
import com.condoflow.residence.domain.port.in.ConsultarResidenciaUseCase;
import com.condoflow.residence.domain.port.in.RegistrarResidenciaUseCase;
import com.condoflow.residence.domain.port.out.ResidenciaRepositoryPort;
import com.condoflow.unit.domain.exception.UnidadNoEncontradaException;
import com.condoflow.unit.domain.port.in.ConsultarUnidadUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Casos de uso del módulo residence.
 * Valida los padres mediante los Port IN públicos de person y unit, nunca con sus JpaRepository.
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

    @Override
    public Residencia registrar(Residencia residencia) {
        if (consultarPersona.buscarPorId(residencia.getPersonaId()).isEmpty()) {
            throw new PersonaNoEncontradaException(residencia.getPersonaId());
        }
        if (consultarUnidad.buscarPorId(residencia.getUnidadId()).isEmpty()) {
            throw new UnidadNoEncontradaException(residencia.getUnidadId());
        }
        if (repositoryPort.existeVigente(residencia.getPersonaId(), residencia.getUnidadId())) {
            throw new ResidenciaVigenteDuplicadaException(residencia.getPersonaId(), residencia.getUnidadId());
        }
        return repositoryPort.guardar(residencia);
    }

    @Override
    public Optional<Residencia> buscarPorId(Long id) {
        return repositoryPort.buscarPorId(id);
    }

    @Override
    public List<Residencia> listarPorPersona(Long personaId) {
        if (consultarPersona.buscarPorId(personaId).isEmpty()) {
            throw new PersonaNoEncontradaException(personaId);
        }
        return repositoryPort.listarPorPersonaId(personaId);
    }
}
