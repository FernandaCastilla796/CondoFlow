package com.condoflow.person.infrastructure.adapter.out.persistence.repository;

import com.condoflow.person.infrastructure.adapter.out.persistence.entity.PersonaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataPersonaRepository extends JpaRepository<PersonaJpaEntity, Long> {

    /** Respalda la regla UNIQUE de persona.correo_electronico antes de insertar. */
    boolean existsByCorreoElectronico(String correoElectronico);

    /** Consulta derivada: el correo existe en una persona con otro id (para el PUT). */
    boolean existsByCorreoElectronicoAndIdNot(String correoElectronico, Long id);

    /** Filtro de GET /api/personas?filtro=... por nombre completo o correo. */
    @Query("""
            select p from PersonaJpaEntity p
            where lower(concat(p.nombre, ' ', p.apellido)) like lower(concat('%', :texto, '%'))
               or lower(p.correoElectronico) like lower(concat('%', :texto, '%'))
            order by p.id
            """)
    List<PersonaJpaEntity> buscar(@Param("texto") String texto);
}
