-- CondoFlow - V3: alinear el esquema con el modelo físico v0.1
--
-- V1 creó persona, residencia, area_comun y reserva con menos columnas que las
-- definidas en docs/04-model/modelo-fisico-v0.1.md y sin CHECK de estados.
-- V1 y V2 ya fueron ejecutadas, por eso no se editan: esta migración agrega lo faltante.

-- =====================================================================
-- persona: documento, telefono y estado
-- =====================================================================
ALTER TABLE persona
    ADD COLUMN documento VARCHAR(50),
    ADD COLUMN telefono  VARCHAR(30),
    ADD COLUMN estado    VARCHAR(30) NOT NULL DEFAULT 'ACTIVO';

-- Completar las filas cargadas por V2 antes de exigir NOT NULL.
UPDATE persona SET documento = '4512378', telefono = '+591 70000001'
WHERE correo_electronico = 'maria.lopez@condoflow.com';
UPDATE persona SET documento = '6231457', telefono = '+591 70000002'
WHERE correo_electronico = 'carlos.rojas@condoflow.com';
UPDATE persona SET documento = 'SIN-DOC-' || persona_id WHERE documento IS NULL;
UPDATE persona SET telefono = '0000000' WHERE telefono IS NULL;

ALTER TABLE persona
    ALTER COLUMN documento SET NOT NULL,
    ALTER COLUMN telefono  SET NOT NULL,
    ADD CONSTRAINT ck_persona_estado CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    ADD CONSTRAINT ck_persona_nombre_no_vacio CHECK (BTRIM(nombre) <> '' AND BTRIM(apellido) <> ''),
    ADD CONSTRAINT ck_persona_correo_formato CHECK (correo_electronico LIKE '_%@_%');

-- =====================================================================
-- residencia: tipo, vigencia temporal (RN-01) y estado
-- =====================================================================
ALTER TABLE residencia
    ADD COLUMN tipo_residencia VARCHAR(50) NOT NULL DEFAULT 'PROPIETARIO',
    ADD COLUMN fecha_inicio    DATE        NOT NULL DEFAULT CURRENT_DATE,
    ADD COLUMN fecha_fin       DATE,
    ADD COLUMN estado          VARCHAR(30) NOT NULL DEFAULT 'VIGENTE';

-- Los valores por defecto sólo servían para completar filas existentes.
ALTER TABLE residencia
    ALTER COLUMN tipo_residencia DROP DEFAULT,
    ALTER COLUMN fecha_inicio    DROP DEFAULT;

ALTER TABLE residencia
    ADD CONSTRAINT ck_residencia_tipo   CHECK (tipo_residencia IN ('PROPIETARIO', 'INQUILINO')),
    ADD CONSTRAINT ck_residencia_estado CHECK (estado IN ('VIGENTE', 'FINALIZADA')),
    ADD CONSTRAINT ck_residencia_fechas CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio),
    -- Una residencia VIGENTE no tiene fecha de fin; una FINALIZADA sí.
    ADD CONSTRAINT ck_residencia_estado_fecha_fin CHECK (
        (estado = 'VIGENTE' AND fecha_fin IS NULL)
        OR (estado = 'FINALIZADA' AND fecha_fin IS NOT NULL)
    );

-- Una persona no puede tener dos residencias VIGENTES en la misma unidad.
-- Índice UNIQUE parcial: sólo aplica a las filas con estado VIGENTE, el historial puede repetirse.
CREATE UNIQUE INDEX uq_residencia_vigente_persona_unidad
    ON residencia (persona_id, unidad_id)
    WHERE estado = 'VIGENTE';

-- =====================================================================
-- unidad: estados permitidos
-- =====================================================================
ALTER TABLE unidad
    ADD CONSTRAINT ck_unidad_estado CHECK (estado IN ('ACTIVA', 'INACTIVA'));

-- =====================================================================
-- area_comun: horario disponible y estados permitidos
-- =====================================================================
ALTER TABLE area_comun ADD COLUMN horario_disponible VARCHAR(100);
UPDATE area_comun SET horario_disponible = '08:00-20:00' WHERE horario_disponible IS NULL;
ALTER TABLE area_comun
    ALTER COLUMN horario_disponible SET NOT NULL,
    ADD CONSTRAINT ck_area_comun_estado CHECK (estado IN ('ACTIVA', 'INACTIVA', 'EN_MANTENIMIENTO'));

-- =====================================================================
-- reserva: observaciones y estados permitidos
-- =====================================================================
ALTER TABLE reserva
    ADD COLUMN observaciones TEXT,
    ADD CONSTRAINT ck_reserva_estado CHECK (estado IN ('PENDIENTE', 'CONFIRMADA', 'CANCELADA', 'FINALIZADA'));

-- =====================================================================
-- Índices sobre FK usadas en consultas y JOIN frecuentes
-- =====================================================================
CREATE INDEX idx_residencia_persona ON residencia (persona_id);
CREATE INDEX idx_residencia_unidad  ON residencia (unidad_id);
CREATE INDEX idx_reserva_area_comun ON reserva (area_comun_id);
CREATE INDEX idx_reserva_persona    ON reserva (persona_id);
