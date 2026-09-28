# Modelo físico v0.1 — CondoFlow

## 1. Estrategia de identificadores

CondoFlow utilizará BIGINT autogenerado para las claves primarias técnicas.

Las claves primarias serán independientes de los datos visibles del negocio.

Las claves naturales que requieren UNIQUE son:

- `unidad.numero_unidad`
- `persona.correo`
- `area_comun.nombre`

---

## 2. unidad

Propósito: Representar cada unidad habitacional del condominio.

| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |
|---|---|---|---|---|
| unidad_id | BIGINT | NO | PK | Modelo relacional |
| numero_unidad | VARCHAR(20) | NO | UNIQUE | Modelo relacional |
| tipo | VARCHAR(50) | NO | — | Modelo relacional |
| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |

### Decisiones

- `unidad_id` es la PK técnica.
- `numero_unidad` debe ser único dentro del condominio.
- `tipo` utiliza texto corto porque representa una clasificación.
- `estado` utiliza valores controlados.

### Regla que NO se resuelve sólo con constraint simple

Las transiciones de estado de una unidad pueden requerir lógica de negocio.

---

## 3. persona

Propósito: Representar a las personas relacionadas con el condominio.

| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |
|---|---|---|---|---|
| persona_id | BIGINT | NO | PK | Modelo relacional |
| nombre | VARCHAR(100) | NO | — | Modelo relacional |
| apellido | VARCHAR(100) | NO | — | Modelo relacional |
| documento | VARCHAR(50) | NO | — | Modelo relacional |
| telefono | VARCHAR(30) | NO | — | Modelo relacional |
| correo | VARCHAR(150) | NO | UNIQUE | Modelo relacional |
| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |

### Decisiones

- `persona_id` es la PK técnica.
- `correo` debe ser único.
- `documento` se conserva como dato de identificación de la persona.
- `telefono` se maneja como texto porque no se utiliza para operaciones matemáticas.
- `estado` utiliza valores controlados.

---

## 4. residencia

Propósito: Relacionar una persona con una unidad del condominio.

| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |
|---|---|---|---|---|
| residencia_id | BIGINT | NO | PK | Modelo relacional |
| persona_id | BIGINT | NO | FK → persona.persona_id | Modelo relacional |
| unidad_id | BIGINT | NO | FK → unidad.unidad_id | Modelo relacional |
| tipo_residencia | VARCHAR(50) | NO | — | Modelo relacional |
| fecha_inicio | DATE | NO | — | Modelo relacional |
| fecha_fin | DATE | SÍ | — | Modelo relacional |
| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |

### Decisiones

- `residencia_id` es la PK técnica.
- `persona_id` y `unidad_id` son obligatorios.
- `fecha_inicio` representa el inicio de la relación y sólo requiere día calendario.
- `fecha_fin` puede ser NULL mientras la residencia continúe vigente.
- `estado` utiliza valores controlados.

### Regla que NO se resuelve sólo con constraint simple

El control de residencia vigente e histórica depende del estado y de la combinación de registros existentes.

---

## 5. area_comun

Propósito: Representar las áreas comunes disponibles para los residentes.

| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |
|---|---|---|---|---|
| area_comun_id | BIGINT | NO | PK | Modelo relacional |
| nombre | VARCHAR(100) | NO | UNIQUE | Modelo relacional |
| descripcion | TEXT | NO | — | Modelo relacional |
| capacidad | INTEGER | NO | CHECK > 0 | Modelo relacional |
| horario_disponible | VARCHAR(100) | NO | — | Modelo relacional |
| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |

### Decisiones

- `area_comun_id` es la PK técnica.
- `nombre` debe ser único.
- `descripcion` utiliza TEXT por su posible extensión.
- `capacidad` es INTEGER y debe ser mayor que cero.
- `horario_disponible` conserva la información del horario del área.
- `estado` utiliza valores controlados.

---

## 6. reserva

Propósito: Registrar las reservas de áreas comunes realizadas por personas.

| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |
|---|---|---|---|---|
| reserva_id | BIGINT | NO | PK | Modelo relacional |
| area_comun_id | BIGINT | NO | FK → area_comun.area_comun_id | Modelo relacional |
| persona_id | BIGINT | NO | FK → persona.persona_id | Modelo relacional |
| fecha_inicio | TIMESTAMPTZ | NO | — | Modelo relacional |
| fecha_fin | TIMESTAMPTZ | NO | CHECK fin > inicio | Modelo relacional |
| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |
| observaciones | TEXT | SÍ | — | Modelo relacional |

### Decisiones

- `reserva_id` es la PK técnica.
- `area_comun_id` y `persona_id` son obligatorios.
- `fecha_inicio` y `fecha_fin` utilizan TIMESTAMPTZ porque importa la fecha y hora.
- `fecha_fin` debe ser posterior a `fecha_inicio`.
- `observaciones` puede quedar NULL cuando no existen observaciones.

### Regla que NO se resuelve sólo con constraint simple

No se debe permitir el solapamiento de reservas de una misma área común. Esta validación requiere comparar diferentes filas.

---

## 7. visita

Propósito: Registrar las visitas asociadas a una unidad del condominio.

| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |
|---|---|---|---|---|
| visita_id | BIGINT | NO | PK | Modelo relacional |
| unidad_id | BIGINT | NO | FK → unidad.unidad_id | Modelo relacional |
| nombre_visitante | VARCHAR(150) | NO | — | Modelo relacional |
| documento_visitante | VARCHAR(50) | NO | — | Modelo relacional |
| fecha_ingreso | TIMESTAMPTZ | NO | — | Modelo relacional |
| fecha_salida | TIMESTAMPTZ | SÍ | CHECK salida > ingreso | Modelo relacional |
| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |
| observaciones | TEXT | SÍ | — | Modelo relacional |

### Decisiones

- `visita_id` es la PK técnica.
- `unidad_id` es obligatorio.
- `fecha_ingreso` es obligatorio.
- `fecha_salida` puede ser NULL mientras la visita esté activa.
- `observaciones` puede ser NULL.
- Las fechas utilizan TIMESTAMPTZ porque representan eventos con hora.

---

## 8. incidencia

Propósito: Registrar problemas que requieren atención dentro del condominio.

| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |
|---|---|---|---|---|
| incidencia_id | BIGINT | NO | PK | Modelo relacional |
| unidad_id | BIGINT | NO | FK → unidad.unidad_id | Modelo relacional |
| persona_id | BIGINT | NO | FK → persona.persona_id | Modelo relacional |
| titulo | VARCHAR(150) | NO | — | Modelo relacional |
| descripcion | TEXT | NO | — | Modelo relacional |
| fecha_reporte | TIMESTAMPTZ | NO | — | Modelo relacional |
| prioridad | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |
| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |
| fecha_resolucion | TIMESTAMPTZ | SÍ | — | Modelo relacional |

### Decisiones

- `incidencia_id` es la PK técnica.
- `unidad_id` y `persona_id` son obligatorios.
- `titulo` utiliza VARCHAR porque es un texto corto.
- `descripcion` utiliza TEXT por su extensión variable.
- `fecha_reporte` utiliza TIMESTAMPTZ.
- `fecha_resolucion` puede ser NULL mientras la incidencia no esté resuelta.
- `prioridad` y `estado` utilizan valores controlados.

### Regla que NO se resuelve sólo con constraint simple

Las transiciones entre estados de una incidencia dependen del proceso de atención.

---

## 9. tarea_mantenimiento

Propósito: Registrar las tareas de mantenimiento asociadas a una incidencia.

| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |
|---|---|---|---|---|
| tarea_mantenimiento_id | BIGINT | NO | PK | Modelo relacional |
| incidencia_id | BIGINT | NO | FK → incidencia.incidencia_id | Modelo relacional |
| descripcion | TEXT | NO | — | Modelo relacional |
| fecha_asignacion | TIMESTAMPTZ | NO | — | Modelo relacional |
| fecha_finalizacion | TIMESTAMPTZ | SÍ | CHECK finalización > asignación | Modelo relacional |
| prioridad | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |
| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |

### Decisiones

- `tarea_mantenimiento_id` es la PK técnica.
- `incidencia_id` es obligatorio.
- `descripcion` utiliza TEXT.
- `fecha_asignacion` es obligatoria.
- `fecha_finalizacion` puede ser NULL mientras la tarea esté pendiente.
- `prioridad` y `estado` utilizan valores controlados.

### Regla que NO se resuelve sólo con constraint simple

Las transiciones de estado de la tarea dependen del proceso de mantenimiento.

---

## 10. Restricciones principales

### PRIMARY KEY

Todas las tablas utilizan una PK BIGINT autogenerada.

### FOREIGN KEY

- `residencia.persona_id` → `persona.persona_id`
- `residencia.unidad_id` → `unidad.unidad_id`
- `reserva.area_comun_id` → `area_comun.area_comun_id`
- `reserva.persona_id` → `persona.persona_id`
- `visita.unidad_id` → `unidad.unidad_id`
- `incidencia.unidad_id` → `unidad.unidad_id`
- `incidencia.persona_id` → `persona.persona_id`
- `tarea_mantenimiento.incidencia_id` → `incidencia.incidencia_id`

### UNIQUE

- `unidad.numero_unidad`
- `persona.correo`
- `area_comun.nombre`

### CHECK candidatos

- `area_comun.capacidad > 0`
- `reserva.fecha_fin > reserva.fecha_inicio`
- `visita.fecha_salida > visita.fecha_ingreso`, cuando exista
- `tarea_mantenimiento.fecha_finalizacion > tarea_mantenimiento.fecha_asignacion`, cuando exista

---

## 11. Reglas transaccionales

Las siguientes reglas no se resuelven con un CHECK simple:

- Evitar reservas solapadas para una misma área común.
- Controlar las transiciones de estado de las reservas.
- Controlar las transiciones de estado de las visitas.
- Controlar las transiciones de estado de las incidencias.
- Controlar las transiciones de estado de las tareas de mantenimiento.

Estas reglas requerirán lógica transaccional o validación en backend.

---

## 12. Auditoría

No se agregan campos de auditoría adicionales en esta versión porque no están definidos en el modelo lógico actual.

La necesidad de `created_at`, `updated_at`, `created_by` o `updated_by` podrá evaluarse posteriormente si los requisitos del proyecto lo justifican.

---

## 13. Orden de dependencias

1. `unidad`
2. `persona`
3. `area_comun`
4. `residencia`
5. `reserva`
6. `visita`
7. `incidencia`
8. `tarea_mantenimiento`

Las tablas que contienen claves foráneas se crearán después de las tablas que referencian.
