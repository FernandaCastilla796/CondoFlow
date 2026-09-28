package com.condoflow.unit.infrastructure.adapter.out.persistence;

import com.condoflow.unit.domain.model.EstadoUnidad;
import com.condoflow.unit.domain.model.Unidad;
import com.condoflow.unit.domain.port.out.UnidadRepositoryPort;
import com.condoflow.unit.infrastructure.adapter.out.persistence.entity.UnidadJpaEntity;
import com.condoflow.unit.infrastructure.adapter.out.persistence.repository.SpringDataUnidadRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UnidadPersistenceAdapter implements UnidadRepositoryPort {

    private final SpringDataUnidadRepository repository;

    public UnidadPersistenceAdapter(SpringDataUnidadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Unidad> buscarPorId(Long id) {
        return repository.findById(id).map(UnidadPersistenceAdapter::toDomain);
    }

    @Override
    public List<Unidad> listar() {
        return repository.findAll(Sort.by("numeroUnidad")).stream()
                .map(UnidadPersistenceAdapter::toDomain)
                .toList();
    }

    private static Unidad toDomain(UnidadJpaEntity e) {
        return new Unidad(e.getId(), e.getNumeroUnidad(), e.getTipo(), EstadoUnidad.valueOf(e.getEstado()));
    }
}
