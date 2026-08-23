\# Modelo físico v0.1 — CondoFlow



\## 1. Estrategia de identificadores



CondoFlow utilizará BIGINT autogenerado para las claves primarias técnicas.



Las claves primarias serán independientes de los datos visibles del negocio.



Las claves naturales que requieren UNIQUE son:



\- `unidad.numero\_unidad`

\- `persona.correo`

\- `area\_comun.nombre`



\---



\## 2. unidad



Propósito: Representar cada unidad habitacional del condominio.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| unidad\_id | BIGINT | NO | PK | Modelo relacional |

| numero\_unidad | VARCHAR(20) | NO | UNIQUE | Modelo relacional |

| tipo | VARCHAR(50) | NO | — | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |



\### Decisiones



\- `unidad\_id` es la PK técnica.

\- `numero\_unidad` debe ser único dentro del condominio.

\- `tipo` utiliza texto corto porque representa una clasificación.

\- `estado` utiliza valores controlados.



\### Regla que NO se resuelve sólo con constraint simple



Las transiciones de estado de una unidad pueden requerir lógica de negocio.



\---



\## 3. persona



Propósito: Representar a las personas relacionadas con el condominio.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| persona\_id | BIGINT | NO | PK | Modelo relacional |

| nombre | VARCHAR(100) | NO | — | Modelo relacional |

| apellido | VARCHAR(100) | NO | — | Modelo relacional |

| documento | VARCHAR(50) | NO | — | Modelo relacional |

| telefono | VARCHAR(30) | NO | — | Modelo relacional |

| correo | VARCHAR(150) | NO | UNIQUE | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |



\### Decisiones



\- `persona\_id` es la PK técnica.

\- `correo` debe ser único.

\- `documento` se conserva como dato de identificación de la persona.

\- `telefono` se maneja como texto porque no se utiliza para operaciones matemáticas.

\- `estado` utiliza valores controlados.



\---



\## 4. residencia



Propósito: Relacionar una persona con una unidad del condominio.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| residencia\_id | BIGINT | NO | PK | Modelo relacional |

| persona\_id | BIGINT | NO | FK → persona.persona\_id | Modelo relacional |

| unidad\_id | BIGINT | NO | FK → unidad.unidad\_id | Modelo relacional |

| tipo\_residencia | VARCHAR(50) | NO | — | Modelo relacional |

| fecha\_inicio | DATE | NO | — | Modelo relacional |

| fecha\_fin | DATE | SÍ | — | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |



\### Decisiones



\- `residencia\_id` es la PK técnica.

\- `persona\_id` y `unidad\_id` son obligatorios.

\- `fecha\_inicio` representa el inicio de la relación y sólo requiere día calendario.

\- `fecha\_fin` puede ser NULL mientras la residencia continúe vigente.

\- `estado` utiliza valores controlados.



\### Regla que NO se resuelve sólo con constraint simple



El control de residencia vigente e histórica depende del estado y de la combinación de registros existentes.



\---



\## 5. area\_comun



Propósito: Representar las áreas comunes disponibles para los residentes.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| area\_comun\_id | BIGINT | NO | PK | Modelo relacional |

| nombre | VARCHAR(100) | NO | UNIQUE | Modelo relacional |

| descripcion | TEXT | NO | — | Modelo relacional |

| capacidad | INTEGER | NO | CHECK > 0 | Modelo relacional |

| horario\_disponible | VARCHAR(100) | NO | — | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |



\### Decisiones



\- `area\_comun\_id` es la PK técnica.

\- `nombre` debe ser único.

\- `descripcion` utiliza TEXT por su posible extensión.

\- `capacidad` es INTEGER y debe ser mayor que cero.

\- `horario\_disponible` conserva la información del horario del área.

\- `estado` utiliza valores controlados.



\---



\## 6. reserva



Propósito: Registrar las reservas de áreas comunes realizadas por personas.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| reserva\_id | BIGINT | NO | PK | Modelo relacional |

| area\_comun\_id | BIGINT | NO | FK → area\_comun.area\_comun\_id | Modelo relacional |

| persona\_id | BIGINT | NO | FK → persona.persona\_id | Modelo relacional |

| fecha\_inicio | TIMESTAMPTZ | NO | — | Modelo relacional |

| fecha\_fin | TIMESTAMPTZ | NO | CHECK fin > inicio | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |

| observaciones | TEXT | SÍ | — | Modelo relacional |



\### Decisiones



\- `reserva\_id` es la PK técnica.

\- `area\_comun\_id` y `persona\_id` son obligatorios.

\- `fecha\_inicio` y `fecha\_fin` utilizan TIMESTAMPTZ porque importa la fecha y hora.

\- `fecha\_fin` debe ser posterior a `fecha\_inicio`.

\- `observaciones` puede quedar NULL cuando no existen observaciones.



\### Regla que NO se resuelve sólo con constraint simple



No se debe permitir el solapamiento de reservas de una misma área común. Esta validación requiere comparar diferentes filas.



\---



\## 7. visita



Propósito: Registrar las visitas asociadas a una unidad del condominio.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| visita\_id | BIGINT | NO | PK | Modelo relacional |

| unidad\_id | BIGINT | NO | FK → unidad.unidad\_id | Modelo relacional |

| nombre\_visitante | VARCHAR(150) | NO | — | Modelo relacional |

| documento\_visitante | VARCHAR(50) | NO | — | Modelo relacional |

| fecha\_ingreso | TIMESTAMPTZ | NO | — | Modelo relacional |

| fecha\_salida | TIMESTAMPTZ | SÍ | CHECK salida > ingreso | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |

| observaciones | TEXT | SÍ | — | Modelo relacional |



\### Decisiones



\- `visita\_id` es la PK técnica.

\- `unidad\_id` es obligatorio.

\- `fecha\_ingreso` es obligatorio.

\- `fecha\_salida` puede ser NULL mientras la visita esté activa.

\- `observaciones` puede ser NULL.

\- Las fechas utilizan TIMESTAMPTZ porque representan eventos con hora.



\---



\## 8. incidencia



Propósito: Registrar problemas que requieren atención dentro del condominio.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| incidencia\_id | BIGINT | NO | PK | Modelo relacional |

| unidad\_id | BIGINT | NO | FK → unidad.unidad\_id | Modelo relacional |

| persona\_id | BIGINT | NO | FK → persona.persona\_id | Modelo relacional |

| titulo | VARCHAR(150) | NO | — | Modelo relacional |

| descripcion | TEXT | NO | — | Modelo relacional |

| fecha\_reporte | TIMESTAMPTZ | NO | — | Modelo relacional |

| prioridad | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |

| fecha\_resolucion | TIMESTAMPTZ | SÍ | — | Modelo relacional |



\### Decisiones



\- `incidencia\_id` es la PK técnica.

\- `unidad\_id` y `persona\_id` son obligatorios.

\- `titulo` utiliza VARCHAR porque es un texto corto.

\- `descripcion` utiliza TEXT por su extensión variable.

\- `fecha\_reporte` utiliza TIMESTAMPTZ.

\- `fecha\_resolucion` puede ser NULL mientras la incidencia no esté resuelta.

\- `prioridad` y `estado` utilizan valores controlados.



\### Regla que NO se resuelve sólo con constraint simple



Las transiciones entre estados de una incidencia dependen del proceso de atención.



\---



\## 9. tarea\_mantenimiento



Propósito: Registrar las tareas de mantenimiento asociadas a una incidencia.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| tarea\_mantenimiento\_id | BIGINT | NO | PK | Modelo relacional |

| incidencia\_id | BIGINT | NO | FK → incidencia.incidencia\_id | Modelo relacional |

| descripcion | TEXT | NO | — | Modelo relacional |

| fecha\_asignacion | TIMESTAMPTZ | NO | — | Modelo relacional |

| fecha\_finalizacion | TIMESTAMPTZ | SÍ | CHECK finalización > asignación | Modelo relacional |

| prioridad | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |



\### Decisiones



\- `tarea\_mantenimiento\_id` es la PK técnica.

\- `incidencia\_id` es obligatorio.

\- `descripcion` utiliza TEXT.

\- `fecha\_asignacion` es obligatoria.

\- `fecha\_finalizacion` puede ser NULL mientras la tarea esté pendiente.

\- `prioridad` y `estado` utilizan valores controlados.



\### Regla que NO se resuelve sólo con constraint simple



Las transiciones de estado de la tarea dependen del proceso de mantenimiento.



\---



\## 10. Restricciones principales



\### PRIMARY KEY



Todas las tablas utilizan una PK BIGINT autogenerada.



\### FOREIGN KEY



\- `residencia.persona\_id` → `persona.persona\_id`

\- `residencia.unidad\_id` → `unidad.unidad\_id`

\- `reserva.area\_comun\_id` → `area\_comun.area\_comun\_id`

\- `reserva.persona\_id` → `persona.persona\_id`

\- `visita.unidad\_id` → `unidad.unidad\_id`

\- `incidencia.unidad\_id` → `unidad.unidad\_id`

\- `incidencia.persona\_id` → `persona.persona\_id`

\- `tarea\_mantenimiento.incidencia\_id` → `incidencia.incidencia\_id`



\### UNIQUE



\- `unidad.numero\_unidad`

\- `persona.correo`

\- `area\_comun.nombre`



\### CHECK candidatos



\- `area\_comun.capacidad > 0`

\- `reserva.fecha\_fin > reserva.fecha\_inicio`

\- `visita.fecha\_salida > visita.fecha\_ingreso`, cuando exista

\- `tarea\_mantenimiento.fecha\_finalizacion > tarea\_mantenimiento.fecha\_asignacion`, cuando exista



\---



\## 11. Reglas transaccionales



Las siguientes reglas no se resuelven con un CHECK simple:



\- Evitar reservas solapadas para una misma área común.

\- Controlar las transiciones de estado de las reservas.

\- Controlar las transiciones de estado de las visitas.

\- Controlar las transiciones de estado de las incidencias.

\- Controlar las transiciones de estado de las tareas de mantenimiento.



Estas reglas requerirán lógica transaccional o validación en backend.



\---



\## 12. Auditoría



No se agregan campos de auditoría adicionales en esta versión porque no están definidos en el modelo lógico actual.



La necesidad de `created\_at`, `updated\_at`, `created\_by` o `updated\_by` podrá evaluarse posteriormente si los requisitos del proyecto lo justifican.



\---



\## 13. Orden de dependencias



1\. `unidad`

2\. `persona`

3\. `area\_comun`

4\. `residencia`

5\. `reserva`

6\. `visita`

7\. `incidencia`

8\. `tarea\_mantenimiento`



Las tablas que contienen claves foráneas se crearán después de las tablas que referencian.

