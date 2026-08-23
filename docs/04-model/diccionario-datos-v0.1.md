\# Diccionario de datos v0.1 — CondoFlow



\## Convenciones



\- PK = clave primaria

\- FK = clave foránea

\- UQ = unicidad

\- NN = obligatorio

\- NULL = valor permitido cuando el dato puede faltar legítimamente



\## Tabla: unidad



| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |

|---|---|---|---|---|---|

| unidad\_id | Identificador único de la unidad | Sí | PK | Identificador único | Gestión de unidades |

| numero\_unidad | Número o código de la unidad | Sí | UQ | No debe repetirse dentro del condominio | Gestión de unidades |

| tipo | Tipo de unidad | Sí | — | Valor correspondiente a un tipo de unidad permitido | Gestión de unidades |

| estado | Estado actual de la unidad | Sí | — | Debe pertenecer a los estados definidos para una unidad | Gestión de unidades |



\## Tabla: persona



| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |

|---|---|---|---|---|---|

| persona\_id | Identificador único de la persona | Sí | PK | Identificador único | Gestión de personas y residencias |

| nombre | Nombre de la persona | Sí | — | Texto no vacío | Gestión de personas y residencias |

| apellido | Apellido de la persona | Sí | — | Texto no vacío | Gestión de personas y residencias |

| correo\_electronico | Correo electrónico de la persona | Sí | UQ | No debe repetirse | Gestión de personas y residencias |



\## Tabla: residencia



| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |

|---|---|---|---|---|---|

| residencia\_id | Identificador único de la residencia | Sí | PK | Identificador único | Gestión de personas y residencias |

| persona\_id | Persona asociada a la residencia | Sí | FK | Debe existir en persona.persona\_id | Gestión de personas y residencias |

| unidad\_id | Unidad asociada a la residencia | Sí | FK | Debe existir en unidad.unidad\_id | Gestión de personas y residencias |



\## Tabla: area\_comun



| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |

|---|---|---|---|---|---|

| area\_comun\_id | Identificador único del área común | Sí | PK | Identificador único | Gestión de áreas comunes |

| nombre | Nombre del área común | Sí | UQ | No debe repetirse | Gestión de áreas comunes |

| descripcion | Descripción del área común | No | — | Puede quedar sin descripción | Gestión de áreas comunes |

| capacidad | Capacidad máxima del área común | Sí | — | Valor mayor que 0 | Gestión de áreas comunes |

| estado | Estado actual del área común | Sí | — | Debe pertenecer a los estados permitidos | Gestión de áreas comunes |





\## Tabla: reserva



| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |

|---|---|---|---|---|---|

| reserva\_id | Identificador único de la reserva | Sí | PK | Identificador único | Gestión de reservas |

| area\_comun\_id | Área común reservada | Sí | FK | Debe existir en area\_comun.area\_comun\_id | Gestión de reservas |

| persona\_id | Persona que realiza la reserva | Sí | FK | Debe existir en persona.persona\_id | Gestión de reservas |

| fecha\_inicio | Fecha y hora de inicio de la reserva | Sí | — | Debe ser anterior a fecha\_fin | Gestión de reservas |

| fecha\_fin | Fecha y hora de finalización de la reserva | Sí | — | Debe ser posterior a fecha\_inicio | Gestión de reservas |

| estado | Estado de la reserva | Sí | — | Debe pertenecer a los estados permitidos | Gestión de reservas |



\## Tabla: visita



| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |

|---|---|---|---|---|---|

| visita\_id | Identificador único de la visita | Sí | PK | Identificador único | Gestión de visitas |

| unidad\_id | Unidad que recibe la visita | Sí | FK | Debe existir en unidad.unidad\_id | Gestión de visitas |

| nombre\_visitante | Nombre del visitante | Sí | — | Texto no vacío | Gestión de visitas |

| documento\_visitante | Documento del visitante | Sí | — | Identificador del visitante | Gestión de visitas |

| fecha\_ingreso | Fecha y hora de ingreso | Sí | — | Fecha válida | Gestión de visitas |

| fecha\_salida | Fecha y hora de salida | No | — | Debe ser posterior a fecha\_ingreso | Gestión de visitas |

| estado | Estado de la visita | Sí | — | Debe pertenecer a los estados permitidos | Gestión de visitas |



\## Tabla: incidencia



| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |

|---|---|---|---|---|---|

| incidencia\_id | Identificador único de la incidencia | Sí | PK | Identificador único | Gestión y seguimiento de incidencias |

| unidad\_id | Unidad relacionada con la incidencia | Sí | FK | Debe existir en unidad.unidad\_id | Gestión y seguimiento de incidencias |

| persona\_id | Persona que registra la incidencia | Sí | FK | Debe existir en persona.persona\_id | Gestión y seguimiento de incidencias |

| descripcion | Descripción del problema | Sí | — | Texto no vacío | Gestión y seguimiento de incidencias |

| fecha\_reporte | Fecha y hora del reporte | Sí | — | Fecha válida | Gestión y seguimiento de incidencias |

| estado | Estado de la incidencia | Sí | — | Debe representar su ciclo de vida | Gestión y seguimiento de incidencias |



\## Tabla: tarea\_mantenimiento



| Campo | Significado | Obligatorio | PK/FK/UQ | Dominio/regla | Origen |

|---|---|---|---|---|---|

| tarea\_mantenimiento\_id | Identificador único de la tarea | Sí | PK | Identificador único | Gestión de mantenimiento |

| incidencia\_id | Incidencia que origina la tarea | Sí | FK | Debe existir en incidencia.incidencia\_id | Gestión de mantenimiento |

| descripcion | Descripción de la tarea | Sí | — | Texto no vacío | Gestión de mantenimiento |

| fecha\_asignacion | Fecha de asignación de la tarea | Sí | — | Fecha válida | Gestión de mantenimiento |

| fecha\_finalizacion | Fecha de finalización de la tarea | No | — | Debe ser posterior a fecha\_asignacion | Gestión de mantenimiento |

| estado | Estado de la tarea | Sí | — | Debe representar su ciclo de vida | Gestión de mantenimiento |

