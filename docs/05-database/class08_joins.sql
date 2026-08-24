-- CLASE 08 - JOIN Y CONSULTAS RELACIONALES
-- Proyecto: CondoFlow

-- Q1. ¿Qué área común corresponde a cada reserva?
-- Relación: reserva.area_comun_id -> area_comun.area_comun_id
-- Cada fila representa: una reserva asociada a un área común.

SELECT
    r.reserva_id,
    r.area_comun_id,
    a.nombre
FROM reserva r
INNER JOIN area_comun a
    ON a.area_comun_id = r.area_comun_id;


-- Q2. ¿Qué persona realizó cada reserva y qué área común reservó?
-- Relaciones:
-- reserva.persona_id -> persona.persona_id
-- reserva.area_comun_id -> area_comun.area_comun_id
-- Cada fila representa: una reserva con su persona y área común.

SELECT
    r.reserva_id,
    p.nombre,
    a.nombre AS area_comun
FROM reserva r
INNER JOIN persona p
    ON p.persona_id = r.persona_id
INNER JOIN area_comun a
    ON a.area_comun_id = r.area_comun_id;


-- Q3. ¿Qué reservas corresponden al área común "Piscina"?
-- Relación: reserva.area_comun_id -> area_comun.area_comun_id
-- Cada fila representa: una reserva realizada en la Piscina.

SELECT
    r.reserva_id,
    a.nombre AS area_comun,
    r.estado
FROM reserva r
INNER JOIN area_comun a
    ON a.area_comun_id = r.area_comun_id
WHERE a.nombre = 'Piscina';


-- Q4. ¿Qué áreas comunes existen, incluso si no tienen reservas?
-- Relación: reserva.area_comun_id -> area_comun.area_comun_id
-- Cada fila representa: un área común y, si existe, una de sus reservas.

SELECT
    a.area_comun_id,
    a.nombre,
    r.reserva_id
FROM area_comun a
LEFT JOIN reserva r
    ON r.area_comun_id = a.area_comun_id;


-- Q5. ¿Qué áreas comunes todavía no tienen ninguna reserva?
-- Relación: reserva.area_comun_id -> area_comun.area_comun_id
-- Cada fila representa: un área común que no tiene reservas.

SELECT
    a.area_comun_id,
    a.nombre
FROM area_comun a
LEFT JOIN reserva r
    ON r.area_comun_id = a.area_comun_id
WHERE r.reserva_id IS NULL;


-- Q6. ¿Qué unidad corresponde a cada residencia?
-- Relación: residencia.unidad_id -> unidad.unidad_id
-- Cada fila representa: una residencia asociada a una unidad.

SELECT
    r.residencia_id,
    u.numero_unidad,
    u.tipo,
    u.estado
FROM residencia r
INNER JOIN unidad u
    ON u.unidad_id = r.unidad_id;

