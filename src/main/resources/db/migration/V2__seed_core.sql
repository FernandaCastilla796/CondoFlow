-- CondoFlow - Dataset Clase 07

-- Personas
INSERT INTO persona (nombre, apellido, correo_electronico)
VALUES
    ('Maria', 'Lopez', 'maria.lopez@condoflow.com'),
    ('Carlos', 'Rojas', 'carlos.rojas@condoflow.com');

-- Unidades
INSERT INTO unidad (numero_unidad, tipo, estado)
VALUES
    ('A-102', 'Departamento', 'ACTIVA'),
    ('B-201', 'Departamento', 'ACTIVA');

-- Areas comunes
INSERT INTO area_comun (nombre, descripcion, capacidad, estado)
VALUES
    ('Piscina', 'Area recreativa para residentes', 20, 'ACTIVA'),
    ('Gimnasio', 'Area para actividad fisica de los residentes', 15, 'ACTIVA');

-- Residencias
INSERT INTO residencia (persona_id, unidad_id)
SELECT p.persona_id, u.unidad_id
FROM persona p
CROSS JOIN unidad u
WHERE p.correo_electronico = 'maria.lopez@condoflow.com'
  AND u.numero_unidad = 'A-102';

INSERT INTO residencia (persona_id, unidad_id)
SELECT p.persona_id, u.unidad_id
FROM persona p
CROSS JOIN unidad u
WHERE p.correo_electronico = 'carlos.rojas@condoflow.com'
  AND u.numero_unidad = 'B-201';

-- Reservas
INSERT INTO reserva (
    area_comun_id,
    persona_id,
    fecha_inicio,
    fecha_fin,
    estado
)
SELECT
    a.area_comun_id,
    p.persona_id,
    '2026-09-02 18:00:00-04',
    '2026-09-02 20:00:00-04',
    'CONFIRMADA'
FROM area_comun a
CROSS JOIN persona p
WHERE a.nombre = 'Piscina'
  AND p.correo_electronico = 'maria.lopez@condoflow.com';

INSERT INTO reserva (
    area_comun_id,
    persona_id,
    fecha_inicio,
    fecha_fin,
    estado
)
SELECT
    a.area_comun_id,
    p.persona_id,
    '2026-09-03 17:00:00-04',
    '2026-09-03 19:00:00-04',
    'CONFIRMADA'
FROM area_comun a
CROSS JOIN persona p
WHERE a.nombre = 'Gimnasio'
  AND p.correo_electronico = 'carlos.rojas@condoflow.com';