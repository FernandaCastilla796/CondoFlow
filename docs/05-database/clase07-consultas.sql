-- Q01. ¿Qué personas están registradas en CondoFlow?
SELECT persona_id, nombre, apellido, correo_electronico
FROM persona
ORDER BY apellido, nombre;

-- Q02. ¿Qué unidades del condominio están activas?
SELECT unidad_id, numero_unidad, tipo, estado
FROM unidad
WHERE estado = 'ACTIVA';

-- Q03. ¿Qué unidades activas son departamentos?
SELECT unidad_id, numero_unidad, tipo, estado
FROM unidad
WHERE estado = 'ACTIVA'
  AND tipo = 'Departamento';

-- Q04. ¿Qué áreas comunes son piscina o gimnasio?
SELECT area_comun_id, nombre, capacidad, estado
FROM area_comun
WHERE nombre = 'Piscina'
   OR nombre = 'Gimnasio';

-- Q05. ¿Cuáles son las áreas comunes de tipo Piscina o Gimnasio?
SELECT area_comun_id, nombre, capacidad, estado
FROM area_comun
WHERE nombre IN ('Piscina', 'Gimnasio');

-- Q06. ¿Qué personas tienen un correo de CondoFlow?
SELECT persona_id, nombre, apellido, correo_electronico
FROM persona
WHERE correo_electronico ILIKE '%@condoflow.com';

-- Q07. ¿Qué áreas comunes tienen una capacidad entre 15 y 30 personas?
SELECT area_comun_id, nombre, capacidad, estado
FROM area_comun
WHERE capacidad BETWEEN 15 AND 30;

-- Q08. ¿Qué reservas están confirmadas?
SELECT reserva_id, area_comun_id, persona_id, fecha_inicio, fecha_fin, estado
FROM reserva
WHERE estado = 'CONFIRMADA';

-- UPDATE: verificar los estados actuales antes de modificar
SELECT reserva_id, estado
FROM reserva
ORDER BY reserva_id;

-- UPDATE: cancelar la reserva 3
UPDATE reserva
SET estado = 'CANCELADA'
WHERE reserva_id = 3;

-- UPDATE: comprobar el cambio realizado
SELECT reserva_id, estado
FROM reserva
WHERE reserva_id = 3;

-- DELETE: decisión
-- No se elimina la reserva 3 porque una reserva cancelada
-- debe conservarse como parte del historial de CondoFlow.

-- ERROR DE INTEGRIDAD REFERENCIAL
-- Se intentó insertar una reserva con area_comun_id = 999.
-- PostgreSQL rechazó el INSERT por la FK fk_reserva_area_comun.
-- El área común 999 no existe en la tabla area_comun.
-- La restricción evita crear reservas asociadas a áreas inexistentes.