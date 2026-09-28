package com.condoflow.residence.infrastructure.adapter.out.persistence;

import com.condoflow.person.infrastructure.adapter.out.persistence.repository.SpringDataPersonaRepository;
import com.condoflow.residence.domain.model.EstadoResidencia;
import com.condoflow.residence.domain.model.Residencia;
import com.condoflow.residence.domain.port.out.ResidenciaRepositoryPort;
import com.condoflow.residence.infrastructure.adapter.out.persistence.mapper.ResidenciaPersistenceMapper;
import com.condoflow.residence.infrastructure.adapter.out.persistence.repository.SpringDataResidenciaRepository;
import com.condoflow.unit.infrastructure.adapter.out.persistence.repository.SpringDataUnidadRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implementa el Port OUT de residencias.
 * Las referencias a los repositorios JPA de persona y unidad quedan dentro de infraestructura:
 * el caso de uso nunca las ve.
 */
@Component
public class ResidenciaPersistenceAdapter implements ResidenciaRepositoryPort {

    private final SpringDataResidenciaRepository repository;
    private final SpringDataPersonaRepository personaRepository;
    private final SpringDataUnidadRepository unidadRepository;

    public ResidenciaPersistenceAdapter(SpringDataResidenciaRepository repository,
                                        SpringDataPersonaRepository personaRepository,
                                        SpringDataUnidadRepository unidadRepository) {
        this.repository = repository;
        this.personaRepository = personaRepository;
        this.unidadRepository = unidadRepository;
    }

    @Override
    public Residencia guardar(Residencia residencia) {
        // getReferenceById crea un proxy con el id, sin hacer SELECT de la persona/unidad.
        var persona = personaRepository.getReferenceById(residencia.getPersonaId());
        var unidad = unidadRepository.getReferenceById(residencia.getUnidadId());
        var entity = ResidenciaPersistenceMapper.toJpa(residencia, persona, unidad);
        return ResidenciaPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Residencia> buscarPorId(Long id) {
        return repository.findById(id).map(ResidenciaPersistenceMapper::toDomain);
    }

    @Override
    public List<Residencia> listarPorPersonaId(Long personaId) {
        return repository.findByPersona_IdOrderByFechaInicioDesc(personaId).stream()
                .map(ResidenciaPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existeVigente(Long personaId, Long unidadId) {
        return repository.existsByPersona_IdAndUnidad_IdAndEstado(
                personaId, unidadId, EstadoResidencia.VIGENTE.name());
    }
}
