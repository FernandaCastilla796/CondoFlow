package com.condoflow.unit.infrastructure.adapter.out.persistence.repository;

import com.condoflow.unit.infrastructure.adapter.out.persistence.entity.UnidadJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUnidadRepository extends JpaRepository<UnidadJpaEntity, Long> {
}
