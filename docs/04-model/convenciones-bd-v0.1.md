# Convenciones de Base de Datos — CondoFlow v0.1

## 1. Nombres

- Las tablas se nombran en `snake_case` y en singular.
- Las columnas se nombran en `snake_case`.
- Las claves primarias utilizan el formato `<tabla>_id`.
- Las claves foráneas conservan el mismo nombre de la clave primaria que referencian.
- Las restricciones de unicidad se utilizan para datos que no deben repetirse.
- Los índices utilizan el formato `idx_<tabla>_<columna>`.
- Las restricciones CHECK se utilizan para validar reglas simples de los datos.

## 2. Tablas del proyecto

Las tablas principales de CondoFlow son:

- `unidad`
- `persona`
- `residencia`
- `area_comun`
- `reserva`
- `visita`
- `incidencia`
- `tarea_mantenimiento`

## 3. Claves primarias

Las claves primarias siguen el formato `<tabla>_id`:

- `unidad.unidad_id`
- `persona.persona_id`
- `residencia.residencia_id`
- `area_comun.area_comun_id`
- `reserva.reserva_id`
- `visita.visita_id`
- `incidencia.incidencia_id`
- `tarea_mantenimiento.tarea_mantenimiento_id`

Las claves primarias deben ser únicas y no admitir valores nulos.

## 4. Claves foráneas

Las relaciones entre las tablas utilizan claves foráneas:

- `residencia.persona_id` referencia `persona.persona_id`.
- `residencia.unidad_id` referencia `unidad.unidad_id`.
- `reserva.area_comun_id` referencia `area_comun.area_comun_id`.
- `reserva.persona_id` referencia `persona.persona_id`.
- `visita.unidad_id` referencia `unidad.unidad_id`.
- `incidencia.unidad_id` referencia `unidad.unidad_id`.
- `incidencia.persona_id` referencia `persona.persona_id`.
- `tarea_mantenimiento.incidencia_id` referencia `incidencia.incidencia_id`.

Las claves foráneas obligatorias no deben admitir valores nulos.

## 5. Restricciones de unicidad

Se establecen las siguientes restricciones UNIQUE:

- `persona.correo_electronico`
- `unidad.numero_unidad`
- `area_comun.nombre`

Estas restricciones evitan duplicar información que debe ser única dentro del condominio.

## 6. Tipos de datos candidatos

- Identificadores: `BIGINT` autogenerado.
- Nombres, apellidos y otros textos cortos: `VARCHAR(n)`.
- Descripciones: `TEXT`.
- Capacidad de áreas comunes: tipo numérico entero.
- Fechas: `DATE`.
- Fechas y horas de ingreso, salida, reporte y reservas: `TIMESTAMPTZ`.
- Estados: `VARCHAR` con valores controlados.

## 7. Índices candidatos

Además de las claves primarias y restricciones UNIQUE, podrán utilizarse índices para las columnas utilizadas frecuentemente en búsquedas y relaciones:

- `idx_residencia_persona_id`
- `idx_residencia_unidad_id`
- `idx_reserva_area_comun_id`
- `idx_reserva_persona_id`
- `idx_visita_unidad_id`
- `idx_incidencia_unidad_id`
- `idx_incidencia_persona_id`
- `idx_tarea_mantenimiento_incidencia_id`

## 8. Reglas de integridad

Las restricciones de la base de datos deben mantener la consistencia definida en el modelo relacional y el DER lógico.

- No debe existir una clave foránea que apunte a un registro inexistente.
- Los campos obligatorios no deben admitir valores nulos.
- Los datos únicos no deben repetirse.
- Los estados deben utilizar valores permitidos.
- Las fechas deben respetar las reglas temporales definidas para cada entidad.

## 9. Regla para excepciones

Cualquier excepción a estas convenciones deberá justificarse según las reglas de negocio de CondoFlow y el modelo de datos aprobado.
