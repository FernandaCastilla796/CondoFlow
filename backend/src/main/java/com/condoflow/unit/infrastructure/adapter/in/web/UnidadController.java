package com.condoflow.unit.infrastructure.adapter.in.web;

import com.condoflow.unit.domain.model.Unidad;
import com.condoflow.unit.domain.port.in.ConsultarUnidadUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Consulta de unidades (catálogo que usan los formularios de residencias). */
@RestController
@RequestMapping("/api/unidades")
public class UnidadController {

    private final ConsultarUnidadUseCase consultar;

    public UnidadController(ConsultarUnidadUseCase consultar) {
        this.consultar = consultar;
    }

    @GetMapping
    public List<UnidadResponse> listar() {
        return consultar.listar().stream().map(UnidadController::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadResponse> buscarPorId(@PathVariable Long id) {
        return consultar.buscarPorId(id)
                .map(UnidadController::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static UnidadResponse toResponse(Unidad u) {
        return new UnidadResponse(u.getUnidadId(), u.getNumeroUnidad(), u.getTipo(), u.getEstado().name());
    }

    public record UnidadResponse(Long unidadId, String numeroUnidad, String tipo, String estado) {
    }
}
