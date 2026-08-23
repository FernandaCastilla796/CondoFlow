\# CondoFlow — Modelo relacional v0.1



\## 1. Objetivo



Definir el modelo relacional inicial de CondoFlow a partir del modelo conceptual elaborado previamente, identificando las tablas núcleo, sus claves primarias, claves foráneas y relaciones principales.



Este modelo corresponde a una primera versión y podrá ajustarse posteriormente durante el diseño físico de la base de datos.



\## 2. Alcance del modelo relacional



El modelo relacional v0.1 se concentra en las tablas núcleo necesarias para soportar los flujos principales de habitabilidad, portería, reservas, incidencias y mantenimiento.



Las entidades `Comunicado` y `Notificación` se mantienen identificadas en el modelo conceptual, pero quedan fuera del núcleo relacional de esta versión y podrán incorporarse en una versión posterior.



\## 3. Tablas núcleo



\- `unidad`: representa las unidades del condominio.

\- `persona`: representa a las personas relacionadas con el condominio.

\- `residencia`: relaciona personas con unidades.

\- `area\_comun`: representa las áreas comunes disponibles.

\- `reserva`: registra las reservas de áreas comunes.

\- `visita`: registra las visitas asociadas a una unidad.

\- `incidencia`: registra problemas o situaciones que requieren atención.

\- `tarea\_mantenimiento`: registra las tareas de mantenimiento del condominio.



\## 4. Criterios de transformación



\- Las relaciones 1:N se transforman colocando la clave foránea en el lado N.

\- Las relaciones N:M se resuelven mediante una tabla puente cuando corresponda.

\- La optionalidad de las relaciones se registra antes de definir restricciones físicas.

\- Los identificadores naturales relevantes se consideran candidatos a restricciones de unicidad.

\- Las claves primarias identifican de forma única cada registro.

\- Las claves foráneas mantienen la integridad referencial entre las tablas relacionadas.



\## 5. Tablas candidatas núcleo



\### unidad



Propósito: representa una unidad habitacional del condominio.



\- `unidad\_id` \[PK]

\- `numero\_unidad`

\- `tipo`

\- `estado`



\### persona



Propósito: representa a una persona relacionada con el condominio.



\- `persona\_id` \[PK]

\- `nombre`

\- `apellido`

\- `documento`

\- `telefono`

\- `correo`

\- `estado`



\### residencia



Propósito: relaciona una persona con una unidad del condominio.



\- `residencia\_id` \[PK]

\- `persona\_id` \[FK -> persona.persona\_id]

\- `unidad\_id` \[FK -> unidad.unidad\_id]

\- `tipo\_residencia`

\- `fecha\_inicio`

\- `fecha\_fin`

\- `estado`



\### area\_comun



Propósito: representa un área común disponible para los residentes.



\- `area\_comun\_id` \[PK]

\- `nombre`

\- `descripcion`

\- `capacidad`

\- `horario\_disponible`

\- `estado`



\### reserva



Propósito: registra las reservas realizadas sobre las áreas comunes.



\- `reserva\_id` \[PK]

\- `area\_comun\_id` \[FK -> area\_comun.area\_comun\_id]

\- `persona\_id` \[FK -> persona.persona\_id]

\- `fecha\_inicio`

\- `fecha\_fin`

\- `estado`

\- `observaciones`



\### visita



Propósito: registra las visitas asociadas a una unidad.



\- `visita\_id` \[PK]

\- `unidad\_id` \[FK -> unidad.unidad\_id]

\- `nombre\_visitante`

\- `documento\_visitante`

\- `fecha\_ingreso`

\- `fecha\_salida`

\- `estado`

\- `observaciones`



\### incidencia



Propósito: registra problemas o situaciones que requieren atención.



\- `incidencia\_id` \[PK]

\- `unidad\_id` \[FK -> unidad.unidad\_id]

\- `persona\_id` \[FK -> persona.persona\_id]

\- `titulo`

\- `descripcion`

\- `fecha\_reporte`

\- `prioridad`

\- `estado`

\- `fecha\_resolucion`



\### tarea\_mantenimiento



Propósito: registra las tareas de mantenimiento del condominio.



\- `tarea\_mantenimiento\_id` \[PK]

\- `incidencia\_id` \[FK -> incidencia.incidencia\_id]

\- `descripcion`

\- `fecha\_asignacion`

\- `fecha\_finalizacion`

\- `prioridad`

\- `estado`



\## 6. Relaciones



1\. `persona` 1 ---- N `residencia`

2\. `unidad` 1 ---- N `residencia`

3\. `area\_comun` 1 ---- N `reserva`

4\. `persona` 1 ---- N `reserva`

5\. `unidad` 1 ---- N `visita`

6\. `unidad` 1 ---- N `incidencia`

7\. `persona` 1 ---- N `incidencia`

8\. `incidencia` 1 ---- N `tarea\_mantenimiento`



\## 7. Optionalidad



\- `residencia.persona\_id`: obligatoria.

\- `residencia.unidad\_id`: obligatoria.

\- `reserva.area\_comun\_id`: obligatoria.

\- `reserva.persona\_id`: obligatoria.

\- `visita.unidad\_id`: obligatoria.

\- `incidencia.unidad\_id`: obligatoria.

\- `incidencia.persona\_id`: obligatoria.

\- `tarea\_mantenimiento.incidencia\_id`: obligatoria.



\## 8. Restricciones de unicidad



\- `unidad.numero\_unidad` se considera único dentro del condominio.

\- `persona.correo` se considera único.

\- `area\_comun.nombre` se considera único.



\## 9. Normalización inicial



\- Cada tabla representa una entidad o relación con una responsabilidad específica.

\- Los atributos contienen valores simples y no grupos repetitivos.

\- Los atributos dependen de la clave primaria de su propia tabla.

\- Las relaciones entre entidades se representan mediante claves foráneas.

\- `residencia` representa la relación entre `persona` y `unidad`.

\- No se mantienen listas ni grupos multivaluados dentro de una misma columna.



\## 10. Reglas relevantes



\- Una residencia debe relacionar una persona con una unidad.

\- Una reserva debe indicar el área común y el período de uso solicitado.

\- Una reserva no debe permitir conflictos de horario para la misma área común.

\- Una visita debe estar asociada a una unidad de destino.

\- Una incidencia debe conservar la información necesaria para su seguimiento.

\- Una tarea de mantenimiento debe estar asociada a una incidencia.



\## 11. Pendientes



\- Incorporar `comunicado` y `notificacion` cuando formen parte del siguiente núcleo funcional del modelo.

\- Definir restricciones físicas definitivas durante el diseño de la base de datos.

\- Revisar los estados y sus transiciones según las reglas de negocio.

