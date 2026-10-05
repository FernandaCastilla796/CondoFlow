package com.condoflow.residence.infrastructure.adapter.out.persistence.repository;

import com.condoflow.residence.infrastructure.adapter.out.persistence.entity.ResidenciaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataResidenciaRepository extends JpaRepository<ResidenciaJpaEntity, Long> {

    boolean existsByPersona_IdAndUnidad_IdAndEstado(Long personaId, Long unidadId, String estado);

    boolean existsByPersona_IdAndUnidad_IdAndEstadoAndIdNot(Long personaId, Long unidadId, String estado, Long id);

    List<ResidenciaJpaEntity> findByPersona_IdOrderByFechaInicioDesc(Long personaId);
}
