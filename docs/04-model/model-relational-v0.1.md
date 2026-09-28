# CondoFlow — Modelo relacional v0.1

## 1. Objetivo

Definir el modelo relacional inicial de CondoFlow a partir del modelo conceptual elaborado previamente, identificando las tablas núcleo, sus claves primarias, claves foráneas y relaciones principales.

Este modelo corresponde a una primera versión y podrá ajustarse posteriormente durante el diseño físico de la base de datos.

## 2. Alcance del modelo relacional

El modelo relacional v0.1 se concentra en las tablas núcleo necesarias para soportar los flujos principales de habitabilidad, portería, reservas, incidencias y mantenimiento.

Las entidades `Comunicado` y `Notificación` se mantienen identificadas en el modelo conceptual, pero quedan fuera del núcleo relacional de esta versión y podrán incorporarse en una versión posterior.

## 3. Tablas núcleo

- `unidad`: representa las unidades del condominio.
- `persona`: representa a las personas relacionadas con el condominio.
- `residencia`: relaciona personas con unidades.
- `area_comun`: representa las áreas comunes disponibles.
- `reserva`: registra las reservas de áreas comunes.
- `visita`: registra las visitas asociadas a una unidad.
- `incidencia`: registra problemas o situaciones que requieren atención.
- `tarea_mantenimiento`: registra las tareas de mantenimiento del condominio.

## 4. Criterios de transformación

- Las relaciones 1:N se transforman colocando la clave foránea en el lado N.
- Las relaciones N:M se resuelven mediante una tabla puente cuando corresponda.
- La optionalidad de las relaciones se registra antes de definir restricciones físicas.
- Los identificadores naturales relevantes se consideran candidatos a restricciones de unicidad.
- Las claves primarias identifican de forma única cada registro.
- Las claves foráneas mantienen la integridad referencial entre las tablas relacionadas.

## 5. Tablas candidatas núcleo

### unidad

Propósito: representa una unidad habitacional del condominio.

- `unidad_id` [PK]
- `numero_unidad`
- `tipo`
- `estado`

### persona

Propósito: representa a una persona relacionada con el condominio.

- `persona_id` [PK]
- `nombre`
- `apellido`
- `documento`
- `telefono`
- `correo`
- `estado`

### residencia

Propósito: relaciona una persona con una unidad del condominio.

- `residencia_id` [PK]
- `persona_id` [FK -> persona.persona_id]
- `unidad_id` [FK -> unidad.unidad_id]
- `tipo_residencia`
- `fecha_inicio`
- `fecha_fin`
- `estado`

### area_comun

Propósito: representa un área común disponible para los residentes.

- `area_comun_id` [PK]
- `nombre`
- `descripcion`
- `capacidad`
- `horario_disponible`
- `estado`

### reserva

Propósito: registra las reservas realizadas sobre las áreas comunes.

- `reserva_id` [PK]
- `area_comun_id` [FK -> area_comun.area_comun_id]
- `persona_id` [FK -> persona.persona_id]
- `fecha_inicio`
- `fecha_fin`
- `estado`
- `observaciones`

### visita

Propósito: registra las visitas asociadas a una unidad.

- `visita_id` [PK]
- `unidad_id` [FK -> unidad.unidad_id]
- `nombre_visitante`
- `documento_visitante`
- `fecha_ingreso`
- `fecha_salida`
- `estado`
- `observaciones`

### incidencia

Propósito: registra problemas o situaciones que requieren atención.

- `incidencia_id` [PK]
- `unidad_id` [FK -> unidad.unidad_id]
- `persona_id` [FK -> persona.persona_id]
- `titulo`
- `descripcion`
- `fecha_reporte`
- `prioridad`
- `estado`
- `fecha_resolucion`

### tarea_mantenimiento

Propósito: registra las tareas de mantenimiento del condominio.

- `tarea_mantenimiento_id` [PK]
- `incidencia_id` [FK -> incidencia.incidencia_id]
- `descripcion`
- `fecha_asignacion`
- `fecha_finalizacion`
- `prioridad`
- `estado`

## 6. Relaciones

1. `persona` 1 ---- N `residencia`
2. `unidad` 1 ---- N `residencia`
3. `area_comun` 1 ---- N `reserva`
4. `persona` 1 ---- N `reserva`
5. `unidad` 1 ---- N `visita`
6. `unidad` 1 ---- N `incidencia`
7. `persona` 1 ---- N `incidencia`
8. `incidencia` 1 ---- N `tarea_mantenimiento`

## 7. Optionalidad

- `residencia.persona_id`: obligatoria.
- `residencia.unidad_id`: obligatoria.
- `reserva.area_comun_id`: obligatoria.
- `reserva.persona_id`: obligatoria.
- `visita.unidad_id`: obligatoria.
- `incidencia.unidad_id`: obligatoria.
- `incidencia.persona_id`: obligatoria.
- `tarea_mantenimiento.incidencia_id`: obligatoria.

## 8. Restricciones de unicidad

- `unidad.numero_unidad` se considera único dentro del condominio.
- `persona.correo` se considera único.
- `area_comun.nombre` se considera único.

## 9. Normalización inicial

- Cada tabla representa una entidad o relación con una responsabilidad específica.
- Los atributos contienen valores simples y no grupos repetitivos.
- Los atributos dependen de la clave primaria de su propia tabla.
- Las relaciones entre entidades se representan mediante claves foráneas.
- `residencia` representa la relación entre `persona` y `unidad`.
- No se mantienen listas ni grupos multivaluados dentro de una misma columna.

## 10. Reglas relevantes

- Una residencia debe relacionar una persona con una unidad.
- Una reserva debe indicar el área común y el período de uso solicitado.
- Una reserva no debe permitir conflictos de horario para la misma área común.
- Una visita debe estar asociada a una unidad de destino.
- Una incidencia debe conservar la información necesaria para su seguimiento.
- Una tarea de mantenimiento debe estar asociada a una incidencia.

## 11. Pendientes

- Incorporar `comunicado` y `notificacion` cuando formen parte del siguiente núcleo funcional del modelo.
- Definir restricciones físicas definitivas durante el diseño de la base de datos.
- Revisar los estados y sus transiciones según las reglas de negocio.
