package com.condoflow.person.infrastructure.adapter.in.web;

import com.condoflow.person.domain.exception.PersonaNoEncontradaException;
import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.domain.port.in.ActualizarPersonaUseCase;
import com.condoflow.person.domain.port.in.ConsultarPersonaUseCase;
import com.condoflow.person.domain.port.in.EliminarPersonaUseCase;
import com.condoflow.person.domain.port.in.RegistrarPersonaUseCase;
import com.condoflow.person.infrastructure.adapter.in.web.dto.ActualizarPersonaRequest;
import com.condoflow.person.infrastructure.adapter.in.web.dto.CrearPersonaRequest;
import com.condoflow.person.infrastructure.adapter.in.web.dto.PersonaResponse;
import com.condoflow.person.infrastructure.adapter.in.web.mapper.PersonaWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Adaptador de entrada HTTP del módulo person.
 * Depende de los Port IN (casos de uso), nunca de JpaRepository.
 */
@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final RegistrarPersonaUseCase registrar;
    private final ConsultarPersonaUseCase consultar;
    private final ActualizarPersonaUseCase actualizar;
    private final EliminarPersonaUseCase eliminar;

    public PersonaController(RegistrarPersonaUseCase registrar, ConsultarPersonaUseCase consultar,
                             ActualizarPersonaUseCase actualizar, EliminarPersonaUseCase eliminar) {
        this.registrar = registrar;
        this.consultar = consultar;
        this.actualizar = actualizar;
        this.eliminar = eliminar;
    }

    @PostMapping
    public ResponseEntity<PersonaResponse> crear(@Valid @RequestBody CrearPersonaRequest request) {
        Persona creada = registrar.registrar(PersonaWebMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(PersonaWebMapper.toResponse(creada));
    }

    @GetMapping
    public List<PersonaResponse> listar(@RequestParam(required = false) String filtro) {
        return consultar.listar(filtro).stream()
                .map(PersonaWebMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public PersonaResponse buscarPorId(@PathVariable Long id) {
        return consultar.buscarPorId(id)
                .map(PersonaWebMapper::toResponse)
                .orElseThrow(() -> new PersonaNoEncontradaException(id));
    }

    /** PUT idempotente: repetir la misma petición deja a la persona en el mismo estado. 200 / 400 / 404 / 409. */
    @PutMapping("/{id}")
    public PersonaResponse actualizar(@PathVariable Long id, @Valid @RequestBody ActualizarPersonaRequest request) {
        Persona actualizada = actualizar.actualizar(id, PersonaWebMapper.toDomain(request));
        return PersonaWebMapper.toResponse(actualizada);
    }

    /** DELETE sin cuerpo: el id viaja en la URL. 204 / 404 / 409 (tiene residencias o reservas). */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminar.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
