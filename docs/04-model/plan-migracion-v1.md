\# Plan de Migración V1 — CondoFlow



\## Objetivo



Crear la estructura inicial de la base de datos de CondoFlow para soportar el registro de unidades, personas, residencias, áreas comunes, reservas, visitas, incidencias y tareas de mantenimiento.



La migración V1 debe implementar el núcleo definido en el modelo físico v0.1 sin introducir nuevas entidades ni reglas de negocio no documentadas.



\## Tablas incluidas y orden



1\. `unidad`

&#x20;  - No depende de otras tablas del núcleo.



2\. `persona`

&#x20;  - No depende de otras tablas del núcleo.



3\. `area\_comun`

&#x20;  - No depende de otras tablas del núcleo.



4\. `residencia`

&#x20;  - Depende de `persona`.

&#x20;  - Depende de `unidad`.



5\. `reserva`

&#x20;  - Depende de `area\_comun`.

&#x20;  - Depende de `persona`.



6\. `visita`

&#x20;  - Depende de `unidad`.



7\. `incidencia`

&#x20;  - Depende de `unidad`.

&#x20;  - Depende de `persona`.



8\. `tarea\_mantenimiento`

&#x20;  - Depende de `incidencia`.



\## Restricciones previstas



\- PK: todas las tablas tendrán una clave primaria BIGINT autogenerada.

\- FK: se crearán las claves foráneas definidas en el modelo físico.

\- NOT NULL: se aplicará a los atributos definidos como obligatorios.

\- UNIQUE:

&#x20; - `unidad.numero\_unidad`

&#x20; - `persona.correo\_electronico`

&#x20; - `area\_comun.nombre`

\- CHECK:

&#x20; - `area\_comun.capacidad > 0`

&#x20; - `reserva.fecha\_fin > reserva.fecha\_inicio`

&#x20; - `visita.fecha\_salida > visita.fecha\_ingreso`, cuando exista.

&#x20; - `tarea\_mantenimiento.fecha\_finalizacion > tarea\_mantenimiento.fecha\_asignacion`, cuando exista.

\- Estados: se mantendrán como valores controlados.



\## Reglas que requerirán lógica posterior



\- Evitar que una misma área común tenga reservas con horarios solapados.

\- Controlar las transiciones de estado de las reservas.

\- Controlar las transiciones de estado de las visitas.

\- Controlar las transiciones de estado de las incidencias.

\- Controlar las transiciones de estado de las tareas de mantenimiento.



Estas reglas dependen del estado de otras filas o de la operación realizada y no se resuelven únicamente mediante PK, FK, NOT NULL, UNIQUE o CHECK simples.



\## Fuera de V1



\- No se crearán tablas adicionales que no estén definidas en el modelo físico v0.1.

\- No se implementarán entidades JPA.

\- No se ejecutará el DDL en PostgreSQL durante esta etapa.

\- No se agregarán módulos nuevos fuera del núcleo definido.

\- No se modificarán las reglas de negocio establecidas para CondoFlow.



\## Criterio de salida



La migración V1 estará preparada cuando:



\- Todas las tablas del núcleo estén identificadas.

\- El orden de creación respete las dependencias entre tablas.

\- Las PK y FK estén definidas.

\- Las restricciones NOT NULL, UNIQUE y CHECK estén identificadas.

\- Las reglas transaccionales estén diferenciadas de las restricciones simples.

\- No sea necesario tomar decisiones importantes adicionales para escribir el DDL de V1.

