package com.condoflow.person.infrastructure.adapter.out.persistence;

import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.domain.port.out.PersonaRepositoryPort;
import com.condoflow.person.infrastructure.adapter.out.persistence.mapper.PersonaPersistenceMapper;
import com.condoflow.person.infrastructure.adapter.out.persistence.repository.SpringDataPersonaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/** Adaptador de salida: implementa el Port OUT usando Spring Data JPA. */
@Component
public class PersonaPersistenceAdapter implements PersonaRepositoryPort {

    private final SpringDataPersonaRepository repository;

    public PersonaPersistenceAdapter(SpringDataPersonaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Persona guardar(Persona persona) {
        var guardada = repository.save(PersonaPersistenceMapper.toEntity(persona));
        return PersonaPersistenceMapper.toDomain(guardada);
    }

    @Override
    public Optional<Persona> buscarPorId(Long id) {
        return repository.findById(id).map(PersonaPersistenceMapper::toDomain);
    }

    @Override
    public List<Persona> listar(String filtro) {
        var entidades = (filtro == null || filtro.isBlank())
                ? repository.findAll(Sort.by("id"))
                : repository.buscar(filtro);
        return entidades.stream().map(PersonaPersistenceMapper::toDomain).toList();
    }

    @Override
    public boolean existePorCorreoElectronico(String correoElectronico) {
        return repository.existsByCorreoElectronico(correoElectronico);
    }
}
