\# Modelo físico v0.1 — CondoFlow



\## 1. Estrategia de identificadores



CondoFlow utilizará BIGINT autogenerado para las claves primarias técnicas.



Las claves primarias serán independientes de los datos visibles del negocio para mantener estables las relaciones entre las tablas.



Los datos de negocio que requieren unicidad son:



\- `unidad.numero\_unidad`

\- `persona.correo\_electronico`

\- `area\_comun.nombre`



Estos atributos no serán utilizados como claves primarias.



\---



\## 2. unidad



Propósito: Representar cada unidad habitacional del condominio.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| unidad\_id | BIGINT | NO | PK | Diseño |

| numero\_unidad | VARCHAR(20) | NO | UNIQUE | Modelo relacional |

| tipo | VARCHAR(50) | NO | — | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |



\### Decisiones



\- `unidad\_id` será la identificación técnica de la unidad.

\- `numero\_unidad` será UNIQUE porque identifica la unidad dentro del condominio.

\- `tipo` será VARCHAR porque representa una clasificación de la unidad.

\- `estado` será VARCHAR con valores controlados.



\### Regla que NO se resuelve sólo con constraint simple



La transición de estado de una unidad puede requerir lógica de negocio adicional.



\---



\## 3. persona



Propósito: Representar a las personas relacionadas con las unidades, reservas e incidencias del condominio.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| persona\_id | BIGINT | NO | PK | Diseño |

| nombre | VARCHAR(100) | NO | — | Modelo relacional |

| apellido | VARCHAR(100) | NO | — | Modelo relacional |

| correo\_electronico | VARCHAR(150) | NO | UNIQUE | Modelo relacional |



\### Decisiones



\- `persona\_id` será la identificación técnica de la persona.

\- `correo\_electronico` será UNIQUE para evitar duplicar personas mediante el mismo correo.

\- `nombre` y `apellido` serán obligatorios.



\### Regla que NO se resuelve sólo con constraint simple



La validación de una persona y su relación con una residencia depende de las reglas de negocio del sistema.



\---



\## 4. residencia



Propósito: Representar la relación entre una persona y una unidad del condominio.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| residencia\_id | BIGINT | NO | PK | Diseño |

| persona\_id | BIGINT | NO | FK → persona.persona\_id | Modelo relacional |

| unidad\_id | BIGINT | NO | FK → unidad.unidad\_id | Modelo relacional |



\### Decisiones



\- `residencia\_id` será la identificación técnica del registro.

\- `persona\_id` y `unidad\_id` serán obligatorios.

\- Las claves foráneas garantizarán que la persona y la unidad existan.



\### Regla que NO se resuelve sólo con constraint simple



Si el sistema necesita controlar la vigencia o historial de una residencia, esa lógica requerirá reglas adicionales.



\---



\## 5. area\_comun



Propósito: Representar las áreas comunes disponibles en el condominio.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| area\_comun\_id | BIGINT | NO | PK | Diseño |

| nombre | VARCHAR(100) | NO | UNIQUE | Modelo relacional |

| descripcion | TEXT | NO | — | Modelo relacional |

| capacidad | INTEGER | NO | CHECK / valor positivo | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |



\### Decisiones



\- `area\_comun\_id` será la identificación técnica del área.

\- `nombre` será UNIQUE.

\- `capacidad` será INTEGER porque representa una cantidad de personas.

\- `estado` tendrá valores controlados.



\### Regla que NO se resuelve sólo con constraint simple



La disponibilidad de un área común para una reserva depende de otras reservas existentes.



\---



\## 6. reserva



Propósito: Representar la reserva de un área común realizada por una persona.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| reserva\_id | BIGINT | NO | PK | Diseño |

| area\_comun\_id | BIGINT | NO | FK → area\_comun.area\_comun\_id | Modelo relacional |

| persona\_id | BIGINT | NO | FK → persona.persona\_id | Modelo relacional |

| fecha\_inicio | TIMESTAMPTZ | NO | — | Modelo relacional |

| fecha\_fin | TIMESTAMPTZ | NO | CHECK fin > inicio | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |



\### Decisiones



\- `reserva\_id` será la identificación técnica de la reserva.

\- `area\_comun\_id` y `persona\_id` serán obligatorios.

\- `fecha\_inicio` y `fecha\_fin` serán TIMESTAMPTZ porque la reserva depende de fecha y hora.

\- `fecha\_fin` debe ser posterior a `fecha\_inicio`.

\- `estado` utilizará valores controlados.



\### Regla que NO se resuelve sólo con constraint simple



No se debe permitir que existan reservas de la misma área común con horarios solapados. Esta regla requiere consultar otras filas y deberá resolverse mediante lógica transaccional o una restricción avanzada.



\---



\## 7. visita



Propósito: Representar el registro de visitas asociadas a una unidad del condominio.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| visita\_id | BIGINT | NO | PK | Diseño |

| unidad\_id | BIGINT | NO | FK → unidad.unidad\_id | Modelo relacional |

| nombre\_visitante | VARCHAR(150) | NO | — | Modelo relacional |

| documento\_visitante | VARCHAR(50) | NO | — | Modelo relacional |

| fecha\_ingreso | TIMESTAMPTZ | NO | — | Modelo relacional |

| fecha\_salida | TIMESTAMPTZ | SÍ | CHECK salida > ingreso | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |



\### Decisiones



\- `visita\_id` será la identificación técnica de la visita.

\- `unidad\_id` será obligatorio.

\- `nombre\_visitante` y `documento\_visitante` serán obligatorios.

\- `fecha\_ingreso` será obligatoria.

\- `fecha\_salida` podrá ser NULL mientras la visita se encuentre activa.

\- Las fechas utilizarán TIMESTAMPTZ porque representan eventos con fecha y hora.

\- `estado` tendrá valores controlados.



\### Regla que NO se resuelve sólo con constraint simple



El estado de una visita y su transición entre ingreso y salida requiere lógica de negocio.



\---



\## 8. incidencia



Propósito: Representar incidencias reportadas en una unidad por una persona.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| incidencia\_id | BIGINT | NO | PK | Diseño |

| unidad\_id | BIGINT | NO | FK → unidad.unidad\_id | Modelo relacional |

| persona\_id | BIGINT | NO | FK → persona.persona\_id | Modelo relacional |

| descripcion | TEXT | NO | — | Modelo relacional |

| fecha\_reporte | TIMESTAMPTZ | NO | — | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |



\### Decisiones



\- `incidencia\_id` será la identificación técnica.

\- `unidad\_id` y `persona\_id` serán obligatorios.

\- `descripcion` será TEXT porque puede contener una explicación extensa.

\- `fecha\_reporte` será TIMESTAMPTZ porque registra el momento del reporte.

\- `estado` tendrá valores controlados.



\### Regla que NO se resuelve sólo con constraint simple



Las transiciones entre estados de una incidencia dependen del proceso de atención y requieren lógica de negocio.



\---



\## 9. tarea\_mantenimiento



Propósito: Representar las tareas de mantenimiento asociadas a una incidencia.



| Columna | Tipo candidato | NULL | Rol/Restricción | Fuente |

|---|---|---|---|---|

| tarea\_mantenimiento\_id | BIGINT | NO | PK | Diseño |

| incidencia\_id | BIGINT | NO | FK → incidencia.incidencia\_id | Modelo relacional |

| descripcion | TEXT | NO | — | Modelo relacional |

| fecha\_asignacion | TIMESTAMPTZ | NO | — | Modelo relacional |

| fecha\_finalizacion | TIMESTAMPTZ | SÍ | CHECK finalización > asignación | Modelo relacional |

| estado | VARCHAR(30) | NO | CHECK / dominio controlado | Modelo relacional |



\### Decisiones



\- `tarea\_mantenimiento\_id` será la identificación técnica.

\- `incidencia\_id` será obligatorio y funcionará como FK.

\- `descripcion` será TEXT.

\- `fecha\_asignacion` será obligatoria.

\- `fecha\_finalizacion` podrá ser NULL mientras la tarea no haya finalizado.

\- Las fechas utilizarán TIMESTAMPTZ.

\- `estado` tendrá valores controlados.



\### Regla que NO se resuelve sólo con constraint simple



La transición de una tarea entre sus estados depende del proceso de mantenimiento y requiere lógica de negocio.



\---



\## 10. Resumen de restricciones



\### PRIMARY KEY



Cada tabla tendrá una clave primaria BIGINT autogenerada.



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

\- `persona.correo\_electronico`

\- `area\_comun.nombre`



\### NOT NULL



Los atributos marcados como obligatorios en el modelo lógico serán NOT NULL.



\### CHECK



Se consideran candidatos a CHECK las reglas que pueden evaluarse dentro de una misma fila:



\- `area\_comun.capacidad > 0`

\- `reserva.fecha\_fin > reserva.fecha\_inicio`

\- `visita.fecha\_salida > visita.fecha\_ingreso`, cuando exista

\- `tarea\_mantenimiento.fecha\_finalizacion > tarea\_mantenimiento.fecha\_asignacion`, cuando exista



\### Reglas transaccionales



El solapamiento de reservas para una misma área común no se resolverá con un CHECK simple porque requiere comparar diferentes filas.



\---



\## 11. Tipos de fecha y hora



CondoFlow utilizará `TIMESTAMPTZ` para los eventos en los que importa conservar fecha y hora:



\- `reserva.fecha\_inicio`

\- `reserva.fecha\_fin`

\- `visita.fecha\_ingreso`

\- `visita.fecha\_salida`

\- `incidencia.fecha\_reporte`

\- `tarea\_mantenimiento.fecha\_asignacion`

\- `tarea\_mantenimiento.fecha\_finalizacion`



No se utilizan tipos de dinero porque las tablas definidas en este modelo no contienen importes monetarios.



\---



\## 12. Auditoría



No se agregan `created\_at`, `updated\_at`, `created\_by` ni `updated\_by` en esta versión porque no forman parte del modelo lógico definido.



Estas columnas podrán evaluarse posteriormente si las reglas de negocio o los requisitos de auditoría del sistema las justifican.



\---



\## 13. Dependencias principales



El orden de dependencia para la creación de las tablas es:



1\. `unidad`

2\. `persona`

3\. `area\_comun`

4\. `residencia` depende de `persona` y `unidad`

5\. `reserva` depende de `area\_comun` y `persona`

6\. `visita` depende de `unidad`

7\. `incidencia` depende de `unidad` y `persona`

8\. `tarea\_mantenimiento` depende de `incidencia`

