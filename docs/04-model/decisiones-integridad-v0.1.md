\# CondoFlow – Decisiones de integridad v0.1



\## 1. Objetivo



Documentar las principales decisiones de integridad que afectan al modelo relacional de CondoFlow, definiendo las reglas que deben mantenerse para conservar la consistencia de los datos.



Estas decisiones se basan en el modelo relacional v0.1, el DER lógico v0.1 y el diccionario de datos v0.1.



\---



\## 2. Integridad estructural



\### 2.1 Claves primarias



Cada tabla debe contar con una clave primaria que identifique de forma única cada registro.



\- `unidad.unidad\_id`

\- `persona.persona\_id`

\- `residencia.residencia\_id`

\- `area\_comun.area\_comun\_id`

\- `reserva.reserva\_id`

\- `visita.visita\_id`

\- `incidencia.incidencia\_id`

\- `tarea\_mantenimiento.tarea\_mantenimiento\_id`



Las claves primarias no deben admitir valores nulos y no deben repetirse.



\### 2.2 Claves foráneas



Las claves foráneas deben mantener la integridad referencial entre las tablas relacionadas.



\- `residencia.persona\_id` referencia a `persona.persona\_id`.

\- `residencia.unidad\_id` referencia a `unidad.unidad\_id`.

\- `reserva.area\_comun\_id` referencia a `area\_comun.area\_comun\_id`.

\- `reserva.persona\_id` referencia a `persona.persona\_id`.

\- `visita.unidad\_id` referencia a `unidad.unidad\_id`.

\- `incidencia.unidad\_id` referencia a `unidad.unidad\_id`.

\- `incidencia.persona\_id` referencia a `persona.persona\_id`.

\- `tarea\_mantenimiento.incidencia\_id` referencia a `incidencia.incidencia\_id`.



Las claves foráneas indicadas como obligatorias no deben admitir valores nulos.



\### 2.3 Restricciones de unicidad



Se establecen restricciones de unicidad para los identificadores naturales relevantes:



\- `persona.correo\_electronico` debe ser único.

\- `unidad.numero\_unidad` debe ser único dentro del condominio.

\- `area\_comun.nombre` debe ser único.



Estas restricciones evitan la duplicación de datos que deben identificar de manera diferenciada a una persona, una unidad o un área común.



\---



\## 3. Integridad de obligatoriedad



Los atributos definidos como obligatorios deben contener un valor.



Entre los principales atributos obligatorios se encuentran:



\- Identificadores primarios.

\- Claves foráneas definidas como obligatorias.

\- Datos básicos de `unidad`.

\- Datos básicos de `persona`.

\- Datos necesarios para registrar una `residencia`.

\- Datos necesarios para registrar una `reserva`.

\- Datos necesarios para registrar una `visita`.

\- Datos necesarios para registrar una `incidencia`.

\- Datos necesarios para registrar una `tarea\_mantenimiento`.



Los atributos definidos como opcionales pueden admitir valores nulos cuando la ausencia del dato sea válida.



\---



\## 4. Integridad de las relaciones



Las relaciones definidas en el modelo deben conservar sus cardinalidades.



\- Una `persona` puede tener varias `residencias`.

\- Una `unidad` puede tener varias `residencias`.

\- Un `area\_comun` puede tener varias `reservas`.

\- Una `persona` puede realizar varias `reservas`.

\- Una `unidad` puede tener varias `visitas`.

\- Una `unidad` puede tener varias `incidencias`.

\- Una `persona` puede registrar varias `incidencias`.

\- Una `incidencia` puede generar una o varias `tareas\_mantenimiento`.



Las claves foráneas son el mecanismo utilizado para mantener estas relaciones en el modelo relacional.



\---



\## 5. Integridad transaccional



Las operaciones que involucren varias tablas relacionadas deben conservar la consistencia de las relaciones.



\### 5.1 Registro de una residencia



Para registrar una residencia deben existir previamente:



\- La persona correspondiente.

\- La unidad correspondiente.



No debe permitirse una residencia que apunte a una persona o unidad inexistente.



\### 5.2 Registro de una reserva



Para registrar una reserva deben existir previamente:



\- El área común correspondiente.

\- La persona que realiza la reserva.



La reserva debe conservar ambas referencias.



\### 5.3 Registro de una incidencia



Para registrar una incidencia deben existir previamente:



\- La unidad relacionada.

\- La persona que registra la incidencia.



La incidencia debe mantener ambas referencias.



\### 5.4 Registro de una tarea de mantenimiento



Para registrar una tarea de mantenimiento debe existir previamente la incidencia a la que pertenece.



No debe existir una tarea de mantenimiento que apunte a una incidencia inexistente.



\---



\## 6. Reglas de consistencia de datos



Los datos registrados deben respetar las reglas definidas en el modelo y diccionario de datos.



\- Los estados deben pertenecer a los valores permitidos para cada entidad.

\- Las fechas deben contener valores válidos.

\- `visita.fecha\_salida`, cuando exista, debe ser posterior a `visita.fecha\_ingreso`.

\- `tarea\_mantenimiento.fecha\_finalizacion`, cuando exista, debe ser posterior a `tarea\_mantenimiento.fecha\_asignacion`.

\- Los campos de texto obligatorios no deben almacenarse vacíos.

\- Las claves foráneas deben corresponder a registros existentes.



\---



\## 7. Prueba de contradicción



Se considera inválida cualquier operación que genere una referencia hacia un registro inexistente.



Ejemplos:



\- Crear una `residencia` con un `persona\_id` inexistente.

\- Crear una `residencia` con un `unidad\_id` inexistente.

\- Crear una `reserva` con un `area\_comun\_id` inexistente.

\- Crear una `reserva` con un `persona\_id` inexistente.

\- Crear una `visita` con un `unidad\_id` inexistente.

\- Crear una `incidencia` con un `unidad\_id` inexistente.

\- Crear una `incidencia` con un `persona\_id` inexistente.

\- Crear una `tarea\_mantenimiento` con un `incidencia\_id` inexistente.



Estas situaciones deben ser rechazadas para preservar la integridad referencial del modelo.



\---



\## 8. Decisiones sobre eliminación y actualización



En esta versión del modelo no se definen todavía políticas físicas específicas de `ON DELETE` ni `ON UPDATE`.



Estas decisiones se establecerán durante el diseño físico de la base de datos, considerando las necesidades de conservación histórica y las reglas de negocio de CondoFlow.



Por lo tanto, en esta versión se mantiene como requisito principal la integridad referencial entre las tablas relacionadas.



\---



\## 9. Resumen de decisiones



| Decisión | Regla |

|---|---|

| Claves primarias | Cada registro debe identificarse de forma única. |

| Claves foráneas | Deben referenciar registros existentes. |

| Campos obligatorios | No deben admitir valores nulos. |

| Unicidad | Correo, número de unidad y nombre de área común no deben repetirse. |

| Fechas | Deben respetar las relaciones temporales definidas. |

| Estados | Deben pertenecer a los valores permitidos. |

| Relaciones | Deben mantener las cardinalidades definidas en el DER. |

| Eliminación/actualización | Se definirán posteriormente en el diseño físico. |



\---



\## 10. Alcance



Este documento corresponde a la versión 0.1 de las decisiones de integridad de CondoFlow.



Las restricciones podrán ajustarse durante las siguientes etapas del diseño de la base de datos, especialmente al definir el modelo físico, las restricciones SQL y las reglas de implementación.



\---



\## 11. Trazabilidad de reglas de negocio



| RN/RF | Regla | Protección prevista | Justificación |

|---|---|---|---|

| RN-01 | Una residencia debe estar asociada a una persona y a una unidad. | FK + NN | Garantiza que toda residencia tenga referencias válidas a una persona y una unidad existentes. |

| RN-02 | Una reserva debe estar asociada a un área común y a una persona. | FK + NN | Garantiza que toda reserva corresponda a un área común y a una persona existentes. |

| RN-03 | Una incidencia debe estar asociada a una unidad y a una persona. | FK + NN | Garantiza que toda incidencia tenga una unidad y una persona válidas. |



\## 12. Prueba de contradicción



| Regla del proyecto | Estado inválido posible | Protección prevista |

|---|---|---|

| RN-01 | Registrar una residencia con un `persona\_id` o `unidad\_id` inexistente. | FK + NN |

| RN-02 | Registrar una reserva con un `area\_comun\_id` o `persona\_id` inexistente. | FK + NN |

| RN-03 | Registrar una incidencia con un `unidad\_id` o `persona\_id` inexistente. | FK + NN |

