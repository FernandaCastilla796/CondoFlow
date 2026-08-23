\# Convenciones de Base de Datos — CondoFlow v0.1



\## Nombres



\- Tablas: minúsculas y snake\_case.

\- Columnas: minúsculas y snake\_case.

\- PK: `<tabla>\_id`.

\- FK: conservar el nombre de la entidad referenciada seguido de `\_id`.

\- UNIQUE: prefijo `uq\_` seguido de tabla y columna.

\- CHECK: prefijo `ck\_` seguido de tabla y regla.

\- Índices: prefijo `ix\_` seguido de tabla y columna.



\## Tipos candidatos



\- Identificador técnico: BIGINT autogenerado.

\- Texto corto: VARCHAR(n).

\- Texto amplio: TEXT.

\- Importes/decimales exactos: NUMERIC(p,s).

\- Fecha: DATE.

\- Instante con hora: TIMESTAMPTZ.

\- Booleanos: BOOLEAN.

\- Estados: VARCHAR(n) con valores controlados mediante restricciones.



\## Regla de equipo



Toda excepción a estas convenciones debe estar justificada por una regla de negocio o requisito del proyecto.

