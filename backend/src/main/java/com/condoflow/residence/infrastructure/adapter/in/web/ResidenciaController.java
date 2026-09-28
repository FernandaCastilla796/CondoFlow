package com.condoflow.residence.infrastructure.adapter.in.web;

import com.condoflow.residence.domain.exception.ResidenciaNoEncontradaException;
import com.condoflow.residence.domain.model.Residencia;
import com.condoflow.residence.domain.port.in.ConsultarResidenciaUseCase;
import com.condoflow.residence.domain.port.in.FinalizarResidenciaUseCase;
import com.condoflow.residence.domain.port.in.RegistrarResidenciaUseCase;
import com.condoflow.residence.infrastructure.adapter.in.web.dto.CrearResidenciaRequest;
import com.condoflow.residence.infrastructure.adapter.in.web.dto.FinalizarResidenciaRequest;
import com.condoflow.residence.infrastructure.adapter.in.web.dto.ResidenciaResponse;
import com.condoflow.residence.infrastructure.adapter.in.web.mapper.ResidenciaWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Adaptador de entrada HTTP del módulo residence. Sin try/catch: los errores los traduce GlobalExceptionHandler. */
@RestController
@RequestMapping("/api/residencias")
public class ResidenciaController {

    private final RegistrarResidenciaUseCase registrar;
    private final ConsultarResidenciaUseCase consultar;
    private final FinalizarResidenciaUseCase finalizar;

    public ResidenciaController(RegistrarResidenciaUseCase registrar,
                                ConsultarResidenciaUseCase consultar,
                                FinalizarResidenciaUseCase finalizar) {
        this.registrar = registrar;
        this.consultar = consultar;
        this.finalizar = finalizar;
    }

    @PostMapping
    public ResponseEntity<ResidenciaResponse> registrar(@Valid @RequestBody CrearResidenciaRequest request) {
        Residencia creada = registrar.registrar(ResidenciaWebMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ResidenciaWebMapper.toResponse(creada));
    }

    @GetMapping("/{id}")
    public ResidenciaResponse buscarPorId(@PathVariable Long id) {
        return consultar.buscarPorId(id)
                .map(ResidenciaWebMapper::toResponse)
                .orElseThrow(() -> new ResidenciaNoEncontradaException(id));
    }

    @GetMapping("/persona/{personaId}")
    public List<ResidenciaResponse> listarPorPersona(@PathVariable Long personaId) {
        return consultar.listarPorPersona(personaId).stream()
                .map(ResidenciaWebMapper::toResponse)
                .toList();
    }

    /** PATCH: modifica parcialmente el recurso (sólo su estado y fecha de fin). */
    @PatchMapping("/{id}/finalizar")
    public ResidenciaResponse finalizar(@PathVariable Long id,
                                        @Valid @RequestBody FinalizarResidenciaRequest request) {
        return ResidenciaWebMapper.toResponse(finalizar.finalizar(id, request.fechaFin()));
    }
}
