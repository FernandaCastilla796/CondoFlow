\# CondoFlow — Modelo relacional v0.1



\## 1. Objetivo



Definir el modelo relacional inicial de CondoFlow a partir del modelo conceptual elaborado previamente, identificando las tablas núcleo, sus claves primarias, claves foráneas y relaciones principales.



Este modelo corresponde a una primera versión y podrá ajustarse posteriormente durante el diseño físico de la base de datos.



\## 2. Tablas núcleo



El modelo relacional inicial se centra en las entidades principales necesarias para soportar la operación del condominio.



Las tablas núcleo consideradas en esta versión son:



\- `unidad`: representa las unidades del condominio.

\- `persona`: representa a las personas relacionadas con el condominio.

\- `residencia`: relaciona personas con unidades.

\- `area\_comun`: representa las áreas comunes disponibles.

\- `reserva`: registra las reservas de áreas comunes.

\- `visita`: registra las visitas asociadas a una unidad.

\- `incidencia`: registra problemas o situaciones que requieren atención.

\- `tarea\_mantenimiento`: registra las tareas de mantenimiento del condominio.



\## 3. Criterios de transformación



\- Las relaciones 1:N se transforman colocando la clave foránea en el lado N.

\- Las relaciones N:M se resolverán mediante una tabla puente cuando corresponda.

\- La optionalidad de las relaciones se registrará antes de definir restricciones físicas.

\- Los identificadores naturales relevantes se considerarán como candidatos a restricciones de unicidad.

\- Las claves primarias se definirán para identificar de forma única cada registro.

\- Las claves foráneas mantendrán la integridad referencial entre las tablas relacionadas.



\## 4. Tablas candidatas núcleo



\### unidad



Propósito: representa una unidad habitacional del condominio.



\- unidad\_id \[PK]

\- codigo

\- tipo

\- estado



\### persona



Propósito: representa una persona relacionada con el condominio.



\- persona\_id \[PK]

\- nombre

\- apellido

\- documento

\- telefono

\- correo



\### residencia



Propósito: relaciona personas con unidades del condominio.



\- residencia\_id \[PK]

\- persona\_id \[FK -> persona.persona\_id]

\- unidad\_id \[FK -> unidad.unidad\_id]

\- tipo\_residencia



\### area\_comun



Propósito: representa un área común disponible para los residentes.



\- area\_comun\_id \[PK]

\- nombre

\- descripcion

\- capacidad

\- estado



\### reserva



Propósito: registra las reservas realizadas sobre las áreas comunes.



\- reserva\_id \[PK]

\- area\_comun\_id \[FK -> area\_comun.area\_comun\_id]

\- persona\_id \[FK -> persona.persona\_id]

\- fecha\_inicio

\- fecha\_fin

\- estado



\### visita



Propósito: registra las visitas asociadas a una unidad.



\- visita\_id \[PK]

\- unidad\_id \[FK -> unidad.unidad\_id]

\- nombre\_visitante

\- documento\_visitante

\- fecha\_ingreso

\- fecha\_salida

\- estado



\### incidencia



Propósito: registra problemas o situaciones que requieren atención.



\- incidencia\_id \[PK]

\- unidad\_id \[FK -> unidad.unidad\_id]

\- persona\_id \[FK -> persona.persona\_id]

\- descripcion

\- fecha\_reporte

\- estado



\### tarea\_mantenimiento



Propósito: registra las tareas de mantenimiento del condominio.



\- tarea\_mantenimiento\_id \[PK]

\- incidencia\_id \[FK -> incidencia.incidencia\_id]

\- descripcion

\- fecha\_asignacion

\- fecha\_finalizacion

\- estado



\## 4. Relaciones y claves foráneas



\### 4.1 Persona — Residencia



\- Una persona puede tener una o varias residencias registradas.

\- Cada residencia corresponde a una sola persona.

\- Cardinalidad: 1:N.

\- FK: `residencia.persona\_id` → `persona.persona\_id`.



\### 4.2 Unidad — Residencia



\- Una unidad puede tener uno o varios registros de residencia.

\- Cada residencia corresponde a una sola unidad.

\- Cardinalidad: 1:N.

\- FK: `residencia.unidad\_id` → `unidad.unidad\_id`.



\### 4.3 Área común — Reserva



\- Un área común puede tener varias reservas.

\- Cada reserva corresponde a una sola área común.

\- Cardinalidad: 1:N.

\- FK: `reserva.area\_comun\_id` → `area\_comun.area\_comun\_id`.



\### 4.4 Persona — Reserva



\- Una persona puede realizar varias reservas.

\- Cada reserva corresponde a una sola persona.

\- Cardinalidad: 1:N.

\- FK: `reserva.persona\_id` → `persona.persona\_id`.



\### 4.5 Unidad — Visita



\- Una unidad puede tener varias visitas.

\- Cada visita está asociada a una sola unidad.

\- Cardinalidad: 1:N.

\- FK: `visita.unidad\_id` → `unidad.unidad\_id`.



\### 4.6 Unidad — Incidencia



\- Una unidad puede tener varias incidencias.

\- Cada incidencia corresponde a una sola unidad.

\- Cardinalidad: 1:N.

\- FK: `incidencia.unidad\_id` → `unidad.unidad\_id`.



\### 4.7 Persona — Incidencia



\- Una persona puede registrar varias incidencias.

\- Cada incidencia corresponde a una sola persona.

\- Cardinalidad: 1:N.

\- FK: `incidencia.persona\_id` → `persona.persona\_id`.



\### 4.8 Incidencia — Tarea de mantenimiento



\- Una incidencia puede generar una o varias tareas de mantenimiento.

\- Cada tarea de mantenimiento corresponde a una incidencia.

\- Cardinalidad: 1:N.

\- FK: `tarea\_mantenimiento.incidencia\_id` → `incidencia.incidencia\_id`.





\## 5. Optionalidad de las claves foráneas



\- `residencia.persona\_id`: obligatoria. Una residencia debe estar asociada a una persona.

\- `residencia.unidad\_id`: obligatoria. Una residencia debe estar asociada a una unidad.

\- `reserva.area\_comun\_id`: obligatoria. Una reserva debe corresponder a un área común.

\- `reserva.persona\_id`: obligatoria. Una reserva debe estar asociada a una persona.

\- `visita.unidad\_id`: obligatoria. Una visita debe estar asociada a una unidad.

\- `incidencia.unidad\_id`: obligatoria. Una incidencia debe estar asociada a una unidad.

\- `incidencia.persona\_id`: obligatoria. Una incidencia debe estar asociada a una persona.

\- `tarea\_mantenimiento.incidencia\_id`: obligatoria. Una tarea de mantenimiento debe estar asociada a una incidencia.





\## 6. Restricciones de unicidad



Las siguientes claves naturales se consideran candidatas a restricciones de unicidad:



\- `persona.correo\_electronico`: debe ser único para evitar que dos personas utilicen el mismo correo.

\- `unidad.numero\_unidad`: debe ser único dentro del condominio.

\- `area\_comun.nombre`: debe ser único para evitar duplicar el nombre de un área común.



\## 7. Normalización inicial



El modelo relacional se plantea siguiendo criterios iniciales de normalización:



\- Cada tabla representa una entidad o relación con una responsabilidad específica.

\- Los atributos contienen valores simples y no se mantienen grupos repetitivos.

\- Los atributos dependen de la clave primaria de su propia tabla.

\- Las relaciones entre entidades se representan mediante claves foráneas.

\- La relación entre `persona` y `unidad` se resuelve mediante la tabla intermedia `residencia`.

\- No se incluyen datos derivados cuando pueden obtenerse a partir de otras relaciones o atributos.





