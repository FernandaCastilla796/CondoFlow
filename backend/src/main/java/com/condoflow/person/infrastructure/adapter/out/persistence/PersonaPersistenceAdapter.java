package com.condoflow.person.infrastructure.adapter.out.persistence;

import com.condoflow.person.domain.exception.PersonaConRegistrosAsociadosException;
import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.domain.port.out.PersonaRepositoryPort;
import com.condoflow.person.infrastructure.adapter.out.persistence.mapper.PersonaPersistenceMapper;
import com.condoflow.person.infrastructure.adapter.out.persistence.repository.SpringDataPersonaRepository;
import org.springframework.dao.DataIntegrityViolationException;
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

    /** save() hace INSERT si la entidad no tiene id y UPDATE si ya lo tiene. */
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

    @Override
    public boolean existePorCorreoElectronicoEnOtraPersona(String correoElectronico, Long personaId) {
        return repository.existsByCorreoElectronicoAndIdNot(correoElectronico, personaId);
    }

    /**
     * flush() ejecuta el DELETE en este momento: si PostgreSQL lo rechaza por las FK
     * fk_residencia_persona o fk_reserva_persona, el error técnico se traduce aquí a una excepción del negocio.
     */
    @Override
    public void eliminar(Long id) {
        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new PersonaConRegistrosAsociadosException(id);
        }
    }
}
