package com.condoflow.person.application;

import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.infrastructure.adapter.out.persistence.mapper.PersonaPersistenceMapper;
import com.condoflow.person.infrastructure.adapter.out.persistence.repository.SpringDataPersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Capítulo 05: la lista en memoria se reemplaza por PostgreSQL mediante Spring Data JPA.
 * Todavía depende directamente del repositorio de infraestructura; el Capítulo 06 lo invierte con un Port OUT.
 */
@Service
public class PersonaService {

    private final SpringDataPersonaRepository repository;

    public PersonaService(SpringDataPersonaRepository repository) {
        this.repository = repository;
    }

    public Persona registrar(Persona persona) {
        var guardada = repository.save(PersonaPersistenceMapper.toEntity(persona));
        return PersonaPersistenceMapper.toDomain(guardada);
    }

    public List<Persona> listar(String filtro) {
        var entidades = (filtro == null || filtro.isBlank())
                ? repository.findAll()
                : repository.buscar(filtro.trim());
        return entidades.stream().map(PersonaPersistenceMapper::toDomain).toList();
    }

    public Optional<Persona> buscarPorId(Long id) {
        return repository.findById(id).map(PersonaPersistenceMapper::toDomain);
    }
}
