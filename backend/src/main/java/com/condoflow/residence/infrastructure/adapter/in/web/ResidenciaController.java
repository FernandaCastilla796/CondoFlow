package com.condoflow.residence.infrastructure.adapter.in.web;

import com.condoflow.residence.domain.exception.ResidenciaNoEncontradaException;
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

/**
 * Adaptador de entrada HTTP del módulo residence (Capítulo 07).
 * Sin try/catch: los errores los traduce GlobalExceptionHandler (Capítulo 08).
 */
@RestController
@RequestMapping("/api/residencias")
public class ResidenciaController {

    private final RegistrarResidenciaUseCase registrarUseCase;
    private final ConsultarResidenciaUseCase consultarUseCase;

    public ResidenciaController(RegistrarResidenciaUseCase registrarUseCase,
                                ConsultarResidenciaUseCase consultarUseCase) {
        this.registrarUseCase = registrarUseCase;
        this.consultarUseCase = consultarUseCase;
    }

    @PostMapping
    public ResponseEntity<ResidenciaResponse> registrar(@Valid @RequestBody CrearResidenciaRequest request) {
        Residencia creada = registrarUseCase.registrar(ResidenciaWebMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ResidenciaWebMapper.toResponse(creada));
    }

    @GetMapping
    public List<ResidenciaResponse> listar() {
        return consultarUseCase.listar().stream()
                .map(ResidenciaWebMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResidenciaResponse buscarPorId(@PathVariable Long id) {
        return consultarUseCase.buscarPorId(id)
                .map(ResidenciaWebMapper::toResponse)
                .orElseThrow(() -> new ResidenciaNoEncontradaException(id));
    }

    @GetMapping("/persona/{personaId}")
    public List<ResidenciaResponse> listarPorPersona(@PathVariable Long personaId) {
        return consultarUseCase.listarPorPersona(personaId).stream()
                .map(ResidenciaWebMapper::toResponse)
                .toList();
    }
}
