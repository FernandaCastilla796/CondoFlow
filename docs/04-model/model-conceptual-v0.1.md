\# Modelo conceptual v0.1 — CondoFlow PA-04



\## 1. Objetivo



Este documento identifica los conceptos principales del dominio de CondoFlow, sus atributos conceptuales, relaciones, cardinalidades y primeras reglas de integridad.



El modelo representa la gestión operativa de un condominio mediante unidades, personas, residencias, áreas comunes, reservas, visitas, incidencias, mantenimiento, comunicados y notificaciones.



\## 2. Fuente analizada



\- Proyecto asignado: CondoFlow — Administración Operativa de Condominio (PA-04).

\- Secciones revisadas: problema, objetivo, actores, alcance funcional, reglas de negocio, conceptos del dominio y flujo crítico.

\- Documentos de apoyo: visión v0.1, glosario v0.1 y backlog v0.1.



\## 3. Candidatos analizados



| Concepto | Clasificación | Justificación | Fuente |

|---|---|---|---|

| Unidad | Entidad | Representa un espacio individual del condominio que necesita identidad propia. | Alcance / Glosario |

| Persona | Entidad | Representa a las personas relacionadas con las unidades y con las operaciones del condominio. | Alcance / Backlog |

| Residencia | Entidad | Representa la relación entre una persona y una unidad del condominio. | Glosario / Backlog |

| Área común | Entidad | Representa un espacio compartido que puede estar sujeto a reglas de uso y reserva. | Alcance / Glosario |

| Reserva | Entidad | Representa una solicitud de uso de un área común durante un período determinado. | Alcance / Backlog |

| Visita | Entidad | Representa una persona externa autorizada para ingresar al condominio. | Alcance / Backlog |

| Incidencia | Entidad | Representa una situación o problema que debe registrarse y atenderse. | Alcance / Backlog |

| Tarea de mantenimiento | Entidad | Representa un trabajo específico que debe realizarse para atender una necesidad de mantenimiento. | Alcance / Backlog |

| Comunicado | Entidad | Representa información o avisos publicados por la administración. | Alcance / Backlog |

| Notificación | Entidad | Representa un aviso enviado a un usuario sobre un evento o cambio relevante. | Alcance / Backlog |

| Administración | Actor | Representa el rol encargado de gestionar las operaciones del condominio; no se modela como entidad independiente en esta versión. | Actores |

| Estado | Estado | Representa situaciones del ciclo de vida de reservas, incidencias, visitas y tareas; no se modela como entidad independiente en esta versión. | Reglas de negocio |



\## 4. Entidades núcleo v0.1



\### Unidad



Responsabilidad: Representar cada espacio o propiedad individual que forma parte del condominio.



Atributos conceptuales: identificador, número o código de unidad, descripción y estado.



Identificador de negocio candidato: número o código de unidad.



\### Persona



Responsabilidad: Representar a las personas relacionadas con las unidades y con las operaciones del condominio.



Atributos conceptuales: identificador, nombre, documento de identidad, teléfono, correo y estado.



Identificador de negocio candidato: documento de identidad.



\### Residencia



Responsabilidad: Relacionar una persona con una unidad del condominio.



Atributos conceptuales: identificador, tipo de relación, fecha de inicio, fecha de fin y estado.



Identificador de negocio candidato: relación persona-unidad.



\### Área común



Responsabilidad: Representar los espacios compartidos del condominio que pueden estar sujetos a reglas de uso y reserva.



Atributos conceptuales: identificador, nombre, descripción, capacidad, horario disponible y estado.



Identificador de negocio candidato: nombre o código del área común.



\### Reserva



Responsabilidad: Representar una solicitud de uso de un área común durante un período determinado.



Atributos conceptuales: identificador, fecha, hora de inicio, hora de fin, estado y observaciones.



Identificador de negocio candidato: identificador de reserva.



\### Visita



Responsabilidad: Registrar una persona externa autorizada para ingresar al condominio durante un período determinado.



Atributos conceptuales: identificador, nombre de visitante, documento, fecha de ingreso prevista, fecha de salida prevista, estado y observaciones.



Identificador de negocio candidato: identificador de visita.



\### Incidencia



Responsabilidad: Registrar y permitir el seguimiento de problemas ocurridos dentro del condominio.



Atributos conceptuales: identificador, título, descripción, fecha de registro, prioridad, estado y fecha de resolución.



Identificador de negocio candidato: identificador de incidencia.



\### Tarea de mantenimiento



Responsabilidad: Representar trabajos específicos que deben realizarse para atender necesidades de mantenimiento.



Atributos conceptuales: identificador, descripción, fecha programada, prioridad, estado y fecha de finalización.



Identificador de negocio candidato: identificador de tarea.



\### Comunicado



Responsabilidad: Representar información o avisos publicados por la administración para los residentes.



Atributos conceptuales: identificador, título, contenido, fecha de publicación, fecha de vigencia y estado.



Identificador de negocio candidato: identificador de comunicado.



\### Notificación



Responsabilidad: Representar avisos generados para informar a un usuario sobre eventos o cambios relevantes.



Atributos conceptuales: identificador, título, mensaje, fecha de generación, estado de lectura y tipo de evento.



Identificador de negocio candidato: identificador de notificación.



\## 5. Relaciones



\- Una persona puede estar asociada a una o varias residencias.

\- Cada residencia relaciona una persona con una unidad.

\- Una unidad puede tener una o varias residencias registradas.

\- Una unidad puede tener varias personas relacionadas mediante residencias.

\- Un área común puede tener muchas reservas.

\- Una reserva corresponde a un área común.

\- Una reserva es realizada por una persona.

\- Una visita está asociada a una unidad de destino.

\- Una persona puede registrar o consultar visitas relacionadas con una unidad según sus permisos.

\- Una incidencia puede estar relacionada con una unidad, área común u otro espacio del condominio.

\- Una tarea de mantenimiento puede atender una incidencia.

\- Una persona puede recibir varias notificaciones.

\- Un comunicado puede generar notificaciones para los usuarios correspondientes.



\## 6. Cardinalidades



| Relación | Cardinalidad | Justificación |

|---|---|---|

| Persona — Residencia | 1:N | Una persona puede tener varias relaciones de residencia registradas. |

| Unidad — Residencia | 1:N | Una unidad puede tener varias residencias registradas. |

| Área común — Reserva | 1:N | Un área común puede ser reservada muchas veces en diferentes períodos. |

| Persona — Reserva | 1:N | Una persona puede realizar varias reservas. |

| Unidad — Visita | 1:N | Una unidad puede tener múltiples visitas registradas. |

| Persona — Notificación | 1:N | Una persona puede recibir múltiples notificaciones. |

| Incidencia — Tarea de mantenimiento | 1:N | Una incidencia puede requerir una o varias tareas de mantenimiento. |

| Persona — Comunicado | 1:N | Un comunicado puede ser publicado por una persona con responsabilidad administrativa. |



\## 7. Reglas iniciales de integridad



\- RI-01: Una unidad debe estar identificada de forma única dentro del condominio.

\- RI-02: Una residencia debe relacionar una persona con una unidad.

\- RI-03: Una reserva debe indicar el área común y el período de uso solicitado.

\- RI-04: Una reserva no debe permitir conflictos de horario para la misma área común.

\- RI-05: Una visita debe estar asociada a una unidad de destino.

\- RI-06: Una incidencia debe conservar su información principal para permitir su seguimiento.

\- RI-07: Una tarea de mantenimiento debe conservar su estado y la información principal del trabajo.

\- RI-08: Un comunicado publicado debe quedar disponible para su consulta durante su período de vigencia.

\- RI-09: Una notificación debe estar asociada al usuario que debe recibirla.

\- RI-10: Los estados de reservas, incidencias y tareas deben representar su ciclo de vida operativo.



\## 8. Dudas y decisiones



\- D-01: En esta versión se considera Residencia como entidad independiente porque representa la relación entre una persona y una unidad.

\- D-02: Estado se mantiene como concepto de dominio y no como entidad independiente en esta versión.

\- D-03: Las reglas físicas de claves primarias, claves foráneas y restricciones se definirán en el modelo relacional de la Clase 03.

\- D-04: La forma exacta de evitar solapamientos de reservas se definirá posteriormente en el diseño físico.

\- D-05: La asignación detallada de permisos por rol queda pendiente de mayor especificación.



\## 9. Trazabilidad inicial



| Concepto/relación | RN/RF asociado |

|---|---|

| Unidad | Gestión de unidades |

| Persona — Residencia | Gestión de personas y residencias |

| Área común | Gestión de áreas comunes |

| Área común — Reserva | Gestión de reservas |

| Unidad — Visita | Gestión de visitas |

| Incidencia | Gestión y seguimiento de incidencias |

| Incidencia — Tarea de mantenimiento | Gestión de mantenimiento |

| Comunicado | Gestión de comunicados |

| Persona — Notificación | Gestión de notificaciones |



\## 10. Pendientes para Clase 03



\- Revisar los identificadores de las entidades.

\- Transformar el modelo conceptual en modelo relacional.

\- Definir claves primarias.

\- Definir claves foráneas.

\- Definir optionalidad física.

\- Resolver posibles relaciones N:M.

\- Revisar la normalización inicial.

\- Revisar las restricciones derivadas de las reglas de negocio.

