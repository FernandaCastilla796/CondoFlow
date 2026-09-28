package com.condoflow.unit.application.service;

import com.condoflow.unit.domain.model.Unidad;
import com.condoflow.unit.domain.port.in.ConsultarUnidadUseCase;
import com.condoflow.unit.domain.port.out.UnidadRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UnidadService implements ConsultarUnidadUseCase {

    private final UnidadRepositoryPort repositoryPort;

    public UnidadService(UnidadRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Optional<Unidad> buscarPorId(Long id) {
        return repositoryPort.buscarPorId(id);
    }

    @Override
    public List<Unidad> listar() {
        return repositoryPort.listar();
    }
}
