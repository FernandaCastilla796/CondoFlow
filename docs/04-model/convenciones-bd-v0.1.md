\# Convenciones de Base de Datos — CondoFlow v0.1



\## 1. Nombres



\- Las tablas se nombran en `snake\_case` y en singular.

\- Las columnas se nombran en `snake\_case`.

\- Las claves primarias utilizan el formato `<tabla>\_id`.

\- Las claves foráneas conservan el mismo nombre de la clave primaria que referencian.

\- Las restricciones de unicidad se utilizan para datos que no deben repetirse.

\- Los índices utilizan el formato `idx\_<tabla>\_<columna>`.

\- Las restricciones CHECK se utilizan para validar reglas simples de los datos.



\## 2. Tablas del proyecto



Las tablas principales de CondoFlow son:



\- `unidad`

\- `persona`

\- `residencia`

\- `area\_comun`

\- `reserva`

\- `visita`

\- `incidencia`

\- `tarea\_mantenimiento`



\## 3. Claves primarias



Las claves primarias siguen el formato `<tabla>\_id`:



\- `unidad.unidad\_id`

\- `persona.persona\_id`

\- `residencia.residencia\_id`

\- `area\_comun.area\_comun\_id`

\- `reserva.reserva\_id`

\- `visita.visita\_id`

\- `incidencia.incidencia\_id`

\- `tarea\_mantenimiento.tarea\_mantenimiento\_id`



Las claves primarias deben ser únicas y no admitir valores nulos.



\## 4. Claves foráneas



Las relaciones entre las tablas utilizan claves foráneas:



\- `residencia.persona\_id` referencia `persona.persona\_id`.

\- `residencia.unidad\_id` referencia `unidad.unidad\_id`.

\- `reserva.area\_comun\_id` referencia `area\_comun.area\_comun\_id`.

\- `reserva.persona\_id` referencia `persona.persona\_id`.

\- `visita.unidad\_id` referencia `unidad.unidad\_id`.

\- `incidencia.unidad\_id` referencia `unidad.unidad\_id`.

\- `incidencia.persona\_id` referencia `persona.persona\_id`.

\- `tarea\_mantenimiento.incidencia\_id` referencia `incidencia.incidencia\_id`.



Las claves foráneas obligatorias no deben admitir valores nulos.



\## 5. Restricciones de unicidad



Se establecen las siguientes restricciones UNIQUE:



\- `persona.correo\_electronico`

\- `unidad.numero\_unidad`

\- `area\_comun.nombre`



Estas restricciones evitan duplicar información que debe ser única dentro del condominio.



\## 6. Tipos de datos candidatos



\- Identificadores: `BIGINT` autogenerado.

\- Nombres, apellidos y otros textos cortos: `VARCHAR(n)`.

\- Descripciones: `TEXT`.

\- Capacidad de áreas comunes: tipo numérico entero.

\- Fechas: `DATE`.

\- Fechas y horas de ingreso, salida, reporte y reservas: `TIMESTAMPTZ`.

\- Estados: `VARCHAR` con valores controlados.



\## 7. Índices candidatos



Además de las claves primarias y restricciones UNIQUE, podrán utilizarse índices para las columnas utilizadas frecuentemente en búsquedas y relaciones:



\- `idx\_residencia\_persona\_id`

\- `idx\_residencia\_unidad\_id`

\- `idx\_reserva\_area\_comun\_id`

\- `idx\_reserva\_persona\_id`

\- `idx\_visita\_unidad\_id`

\- `idx\_incidencia\_unidad\_id`

\- `idx\_incidencia\_persona\_id`

\- `idx\_tarea\_mantenimiento\_incidencia\_id`



\## 8. Reglas de integridad



Las restricciones de la base de datos deben mantener la consistencia definida en el modelo relacional y el DER lógico.



\- No debe existir una clave foránea que apunte a un registro inexistente.

\- Los campos obligatorios no deben admitir valores nulos.

\- Los datos únicos no deben repetirse.

\- Los estados deben utilizar valores permitidos.

\- Las fechas deben respetar las reglas temporales definidas para cada entidad.



\## 9. Regla para excepciones



Cualquier excepción a estas convenciones deberá justificarse según las reglas de negocio de CondoFlow y el modelo de datos aprobado.

