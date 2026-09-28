# Plan de Migración V1 — CondoFlow

## Objetivo

Crear la estructura inicial de la base de datos de CondoFlow para soportar el registro de unidades, personas, residencias, áreas comunes, reservas, visitas, incidencias y tareas de mantenimiento.

La migración V1 debe implementar el núcleo definido en el modelo físico v0.1 sin introducir nuevas entidades ni reglas de negocio no documentadas.

## Tablas incluidas y orden

1. `unidad`
   - No depende de otras tablas del núcleo.

2. `persona`
   - No depende de otras tablas del núcleo.

3. `area_comun`
   - No depende de otras tablas del núcleo.

4. `residencia`
   - Depende de `persona`.
   - Depende de `unidad`.

5. `reserva`
   - Depende de `area_comun`.
   - Depende de `persona`.

6. `visita`
   - Depende de `unidad`.

7. `incidencia`
   - Depende de `unidad`.
   - Depende de `persona`.

8. `tarea_mantenimiento`
   - Depende de `incidencia`.

## Restricciones previstas

- PK: todas las tablas tendrán una clave primaria BIGINT autogenerada.
- FK: se crearán las claves foráneas definidas en el modelo físico.
- NOT NULL: se aplicará a los atributos definidos como obligatorios.
- UNIQUE:
  - `unidad.numero_unidad`
  - `persona.correo_electronico`
  - `area_comun.nombre`
- CHECK:
  - `area_comun.capacidad > 0`
  - `reserva.fecha_fin > reserva.fecha_inicio`
  - `visita.fecha_salida > visita.fecha_ingreso`, cuando exista.
  - `tarea_mantenimiento.fecha_finalizacion > tarea_mantenimiento.fecha_asignacion`, cuando exista.
- Estados: se mantendrán como valores controlados.

## Reglas que requerirán lógica posterior

- Evitar que una misma área común tenga reservas con horarios solapados.
- Controlar las transiciones de estado de las reservas.
- Controlar las transiciones de estado de las visitas.
- Controlar las transiciones de estado de las incidencias.
- Controlar las transiciones de estado de las tareas de mantenimiento.

Estas reglas dependen del estado de otras filas o de la operación realizada y no se resuelven únicamente mediante PK, FK, NOT NULL, UNIQUE o CHECK simples.

## Fuera de V1

- No se crearán tablas adicionales que no estén definidas en el modelo físico v0.1.
- No se implementarán entidades JPA.
- No se ejecutará el DDL en PostgreSQL durante esta etapa.
- No se agregarán módulos nuevos fuera del núcleo definido.
- No se modificarán las reglas de negocio establecidas para CondoFlow.

## Criterio de salida

La migración V1 estará preparada cuando:

- Todas las tablas del núcleo estén identificadas.
- El orden de creación respete las dependencias entre tablas.
- Las PK y FK estén definidas.
- Las restricciones NOT NULL, UNIQUE y CHECK estén identificadas.
- Las reglas transaccionales estén diferenciadas de las restricciones simples.
- No sea necesario tomar decisiones importantes adicionales para escribir el DDL de V1.

## Migraciones ejecutadas

| Versión | Archivo | Contenido |
|---|---|---|
| V1 | `V1__init_core.sql` | Núcleo inicial: `unidad`, `persona`, `area_comun`, `residencia`, `reserva`. |
| V2 | `V2__seed_core.sql` | Dataset mínimo de la Clase 07. |
| V3 | `V3__alinear_modelo_fisico.sql` | Completa las columnas y CHECK que V1 dejó pendientes respecto del modelo físico. |

`visita`, `incidencia` y `tarea_mantenimiento` quedan para migraciones posteriores, cuando se implementen sus módulos.

### Por qué V3 y no editar V1

V1 y V2 ya se ejecutaron en las bases de los integrantes. Flyway guarda un checksum de cada migración aplicada: si se edita V1, la aplicación deja de arrancar en esas bases. La regla es **no modificar una migración ya aplicada; agregar una nueva**.

### Cambios de V3

- `persona`: agrega `documento`, `telefono` y `estado` (`ACTIVO`, `INACTIVO`), más CHECK de nombre no vacío y formato básico de correo.
- `residencia`: agrega `tipo_residencia` (`PROPIETARIO`, `INQUILINO`), `fecha_inicio`, `fecha_fin` y `estado` (`VIGENTE`, `FINALIZADA`) para cumplir RN-01 (vigencia temporal).
  - `ck_residencia_fechas`: `fecha_fin >= fecha_inicio`.
  - `ck_residencia_estado_fecha_fin`: una residencia VIGENTE no tiene `fecha_fin`; una FINALIZADA sí.
  - `uq_residencia_vigente_persona_unidad`: índice UNIQUE **parcial** que impide dos residencias VIGENTES de la misma persona en la misma unidad, pero permite conservar el historial.
- `unidad`: CHECK de estado (`ACTIVA`, `INACTIVA`).
- `area_comun`: agrega `horario_disponible`; CHECK de estado (`ACTIVA`, `INACTIVA`, `EN_MANTENIMIENTO`).
- `reserva`: agrega `observaciones`; CHECK de estado (`PENDIENTE`, `CONFIRMADA`, `CANCELADA`, `FINALIZADA`).
- Índices sobre las FK de `residencia` y `reserva`, usadas en los JOIN de la Clase 08.

### Violaciones de integridad probadas

| Intento | Constraint que lo rechaza |
|---|---|
| Segunda residencia VIGENTE de la misma persona en la misma unidad | `uq_residencia_vigente_persona_unidad` |
| `tipo_residencia = 'ALQUILER'` | `ck_residencia_tipo` |
| Finalizar con `fecha_fin` anterior a `fecha_inicio` | `ck_residencia_fechas` |
| Marcar FINALIZADA sin `fecha_fin` | `ck_residencia_estado_fecha_fin` |
| `persona.estado = 'BORRADO'` | `ck_persona_estado` |
