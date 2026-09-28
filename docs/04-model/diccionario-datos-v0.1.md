# Diccionario de datos v0.1 — CondoFlow

## Convenciones

- PK = clave primaria
- FK = clave foránea
- UQ = unicidad
- NN = obligatorio
- NULL = valor permitido cuando el dato puede faltar legítimamente

## Tabla: unidad

| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |
|---|---|---|---|---|---|
| unidad_id | Identificador único de la unidad | Sí | PK | Identificador único | Gestión de unidades |
| numero_unidad | Número o código de la unidad | Sí | UQ | No debe repetirse dentro del condominio | Gestión de unidades |
| tipo | Tipo de unidad | Sí | — | Valor correspondiente a un tipo de unidad permitido | Gestión de unidades |
| estado | Estado actual de la unidad | Sí | — | Debe pertenecer a los estados definidos para una unidad | Gestión de unidades |

## Tabla: persona

| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |
|---|---|---|---|---|---|
| persona_id | Identificador único de la persona | Sí | PK | Identificador único | Gestión de personas y residencias |
| nombre | Nombre de la persona | Sí | — | Texto no vacío | Gestión de personas y residencias |
| apellido | Apellido de la persona | Sí | — | Texto no vacío | Gestión de personas y residencias |
| correo_electronico | Correo electrónico de la persona | Sí | UQ | No debe repetirse | Gestión de personas y residencias |

## Tabla: residencia

| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |
|---|---|---|---|---|---|
| residencia_id | Identificador único de la residencia | Sí | PK | Identificador único | Gestión de personas y residencias |
| persona_id | Persona asociada a la residencia | Sí | FK | Debe existir en persona.persona_id | Gestión de personas y residencias |
| unidad_id | Unidad asociada a la residencia | Sí | FK | Debe existir en unidad.unidad_id | Gestión de personas y residencias |

## Tabla: area_comun

| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |
|---|---|---|---|---|---|
| area_comun_id | Identificador único del área común | Sí | PK | Identificador único | Gestión de áreas comunes |
| nombre | Nombre del área común | Sí | UQ | No debe repetirse | Gestión de áreas comunes |
| descripcion | Descripción del área común | No | — | Puede quedar sin descripción | Gestión de áreas comunes |
| capacidad | Capacidad máxima del área común | Sí | — | Valor mayor que 0 | Gestión de áreas comunes |
| estado | Estado actual del área común | Sí | — | Debe pertenecer a los estados permitidos | Gestión de áreas comunes |


## Tabla: reserva

| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |
|---|---|---|---|---|---|
| reserva_id | Identificador único de la reserva | Sí | PK | Identificador único | Gestión de reservas |
| area_comun_id | Área común reservada | Sí | FK | Debe existir en area_comun.area_comun_id | Gestión de reservas |
| persona_id | Persona que realiza la reserva | Sí | FK | Debe existir en persona.persona_id | Gestión de reservas |
| fecha_inicio | Fecha y hora de inicio de la reserva | Sí | — | Debe ser anterior a fecha_fin | Gestión de reservas |
| fecha_fin | Fecha y hora de finalización de la reserva | Sí | — | Debe ser posterior a fecha_inicio | Gestión de reservas |
| estado | Estado de la reserva | Sí | — | Debe pertenecer a los estados permitidos | Gestión de reservas |

## Tabla: visita

| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |
|---|---|---|---|---|---|
| visita_id | Identificador único de la visita | Sí | PK | Identificador único | Gestión de visitas |
| unidad_id | Unidad que recibe la visita | Sí | FK | Debe existir en unidad.unidad_id | Gestión de visitas |
| nombre_visitante | Nombre del visitante | Sí | — | Texto no vacío | Gestión de visitas |
| documento_visitante | Documento del visitante | Sí | — | Identificador del visitante | Gestión de visitas |
| fecha_ingreso | Fecha y hora de ingreso | Sí | — | Fecha válida | Gestión de visitas |
| fecha_salida | Fecha y hora de salida | No | — | Debe ser posterior a fecha_ingreso | Gestión de visitas |
| estado | Estado de la visita | Sí | — | Debe pertenecer a los estados permitidos | Gestión de visitas |

## Tabla: incidencia

| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |
|---|---|---|---|---|---|
| incidencia_id | Identificador único de la incidencia | Sí | PK | Identificador único | Gestión y seguimiento de incidencias |
| unidad_id | Unidad relacionada con la incidencia | Sí | FK | Debe existir en unidad.unidad_id | Gestión y seguimiento de incidencias |
| persona_id | Persona que registra la incidencia | Sí | FK | Debe existir en persona.persona_id | Gestión y seguimiento de incidencias |
| descripcion | Descripción del problema | Sí | — | Texto no vacío | Gestión y seguimiento de incidencias |
| fecha_reporte | Fecha y hora del reporte | Sí | — | Fecha válida | Gestión y seguimiento de incidencias |
| estado | Estado de la incidencia | Sí | — | Debe representar su ciclo de vida | Gestión y seguimiento de incidencias |

## Tabla: tarea_mantenimiento

| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |
|---|---|---|---|---|---|
| tarea_mantenimiento_id | Identificador único de la tarea | Sí | PK | Identificador único | Gestión de mantenimiento |
| incidencia_id | Incidencia que origina la tarea | Sí | FK | Debe existir en incidencia.incidencia_id | Gestión de mantenimiento |
| descripcion | Descripción de la tarea | Sí | — | Texto no vacío | Gestión de mantenimiento |
| fecha_asignacion | Fecha de asignación de la tarea | Sí | — | Fecha válida | Gestión de mantenimiento |
| fecha_finalizacion | Fecha de finalización de la tarea | No | — | Debe ser posterior a fecha_asignacion | Gestión de mantenimiento |
| estado | Estado de la tarea | Sí | — | Debe representar su ciclo de vida | Gestión de mantenimiento |
