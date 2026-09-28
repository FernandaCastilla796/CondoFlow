package com.condoflow.person.infrastructure.adapter.in.web;

import com.condoflow.person.application.PersonaService;
import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.infrastructure.adapter.in.web.dto.CrearPersonaRequest;
import com.condoflow.person.infrastructure.adapter.in.web.dto.PersonaResponse;
import com.condoflow.person.infrastructure.adapter.in.web.mapper.PersonaWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Adaptador de entrada HTTP del módulo person. */
@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService service;

    public PersonaController(PersonaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PersonaResponse> crear(@Valid @RequestBody CrearPersonaRequest request) {
        Persona creada = service.registrar(PersonaWebMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(PersonaWebMapper.toResponse(creada));
    }

    @GetMapping
    public List<PersonaResponse> listar(@RequestParam(required = false) String buscar) {
        return service.listar(buscar).stream()
                .map(PersonaWebMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(PersonaWebMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
