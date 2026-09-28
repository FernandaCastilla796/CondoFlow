package com.condoflow.residence.infrastructure.adapter.in.web;

import com.condoflow.residence.domain.model.Residencia;
import com.condoflow.residence.domain.port.in.ConsultarResidenciaUseCase;
import com.condoflow.residence.domain.port.in.RegistrarResidenciaUseCase;
import com.condoflow.residence.infrastructure.adapter.in.web.dto.CrearResidenciaRequest;
import com.condoflow.residence.infrastructure.adapter.in.web.dto.ResidenciaResponse;
import com.condoflow.residence.infrastructure.adapter.in.web.mapper.ResidenciaWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/residencias")
public class ResidenciaController {

    private final RegistrarResidenciaUseCase registrar;
    private final ConsultarResidenciaUseCase consultar;

    public ResidenciaController(RegistrarResidenciaUseCase registrar, ConsultarResidenciaUseCase consultar) {
        this.registrar = registrar;
        this.consultar = consultar;
    }

    @PostMapping
    public ResponseEntity<ResidenciaResponse> registrar(@Valid @RequestBody CrearResidenciaRequest request) {
        Residencia creada = registrar.registrar(ResidenciaWebMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ResidenciaWebMapper.toResponse(creada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResidenciaResponse> buscarPorId(@PathVariable Long id) {
        return consultar.buscarPorId(id)
                .map(ResidenciaWebMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/persona/{personaId}")
    public List<ResidenciaResponse> listarPorPersona(@PathVariable Long personaId) {
        return consultar.listarPorPersona(personaId).stream()
                .map(ResidenciaWebMapper::toResponse)
                .toList();
    }
}
