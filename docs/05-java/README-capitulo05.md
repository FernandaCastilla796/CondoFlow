# Capítulo 05 - Spring Boot + PostgreSQL + JPA/Hibernate

## Ficha

| Dato | Valor |
|---|---|
| Nombre del proyecto | CondoFlow (`condoflow-backend`) |
| Base de datos | `condoflow` |
| Usuario | `condoflow_admin` (creado como indica la Guía DataGrip) |
| Schema | `condoflow` |
| Tabla padre | `persona` (PK `persona_id`, BIGINT IDENTITY) |
| Tabla dependiente | `residencia` |
| FK hacia la tabla padre | `residencia.persona_id → persona.persona_id` |
| UNIQUE importante | `uq_persona_correo (correo_electronico)` |

## Comprobación de la base desde DataGrip

```sql
SELECT current_database() AS base_actual,
       current_schema()   AS schema_actual,
       current_user       AS usuario_actual;
```

```text
 base_actual | schema_actual | usuario_actual
-------------+---------------+-----------------
 condoflow   | condoflow     | condoflow_admin
```

## Conceptos

| Concepto | Significado |
|---|---|
| JDBC | API Java de bajo nivel para hablar con bases relacionales; el driver `org.postgresql` la implementa. |
| JPA | Especificación estándar para mapear objetos a tablas (`@Entity`, `@Table`, `@Column`). |
| Hibernate | Implementación de JPA que usa Spring Boot; genera el SQL. |
| Spring Data JPA | Crea automáticamente la implementación de `SpringDataPersonaRepository` a partir de la interface. |
| ORM | Mapeo entre objetos Java y estructuras relacionales. |
| Flyway | Aplica `db/migration/V*.sql` en orden al arrancar y registra lo aplicado en `flyway_schema_history`. |

## Configuración

[`application.yml`](../../backend/src/main/resources/application.yml):

- `ddl-auto: validate`: Hibernate **no crea tablas**, sólo verifica que las entidades coincidan con el esquema. Las tablas las crea Flyway desde nuestras migraciones.
- `default_schema: condoflow` y `spring.flyway.schemas: condoflow`.
- `open-in-view: false`: no se mantiene la sesión de BD abierta durante la vista HTTP.
- Credenciales por variables de entorno (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) o `backend/.env` (ignorado por Git). Plantilla en [`backend/.env.example`](../../backend/.env.example).

## Estructura

```text
person
├── domain/model/Persona.java                     ← sin @Entity
└── infrastructure/adapter/out/persistence
    ├── entity/PersonaJpaEntity.java              ← @Entity @Table(name="persona", schema="condoflow")
    ├── mapper/PersonaPersistenceMapper.java      ← dominio ↔ JPA
    └── repository/SpringDataPersonaRepository.java
```

## Columnas reales de la tabla

```sql
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_schema = 'condoflow' AND table_name = 'persona'
ORDER BY ordinal_position;
```

| column_name | data_type | is_nullable | Campo JPA |
|---|---|---|---|
| persona_id | bigint | NO | `id` (`@Id`, `IDENTITY`) |
| nombre | character varying(100) | NO | `nombre` |
| apellido | character varying(100) | NO | `apellido` |
| correo_electronico | character varying(150) | NO | `correoElectronico` (`unique = true`) |
| documento | character varying(50) | NO | `documento` |
| telefono | character varying(30) | NO | `telefono` |
| estado | character varying(30) | NO | `estado` |

## Decisiones

- **¿Por qué el dominio no tiene `@Entity`?** `Persona` representa el negocio y no debe depender de JPA. `PersonaJpaEntity` es un detalle de persistencia.
- **¿Por qué `validate` y no `update`?** El grupo diseñó la base físicamente; Java se adapta a ese contrato y no al revés. Si un `@Column` no coincide, la aplicación no arranca.
- **Relación 1:N:** la FK está en `residencia`. No agregamos `@OneToMany` en `PersonaJpaEntity` porque no necesitamos navegar desde la persona a todas sus residencias (Capítulo 07).

## Evidencia

Arranque (Flyway + validación de Hibernate):

```text
Migrating schema "condoflow" to version "1 - init core"
Migrating schema "condoflow" to version "2 - seed core"
Migrating schema "condoflow" to version "3 - alinear modelo fisico"
Successfully applied 3 migrations to schema "condoflow", now at version v3
```

Petición real:

```text
POST /api/personas -> 201 {"personaId":3,"nombre":"Ana","apellido":"Suarez",...,"estado":"ACTIVO"}
```

Comprobación en PostgreSQL:

```sql
SELECT persona_id, nombre, correo_electronico, estado
FROM condoflow.persona
ORDER BY persona_id DESC;
```

```text
 persona_id | nombre |     correo_electronico     | estado
------------+--------+----------------------------+--------
          3 | Ana    | ana.suarez@condoflow.com   | ACTIVO
          2 | Carlos | carlos.rojas@condoflow.com | ACTIVO
          1 | Maria  | maria.lopez@condoflow.com  | ACTIVO
```

## Pruebas negativas

| Prueba | Resultado |
|---|---|
| Correo duplicado | PostgreSQL rechaza con `uq_persona_correo` (en este capítulo llega como 500; el Capítulo 08 lo convierte en 409). |
| Campo NOT NULL omitido | Lo rechaza antes `@NotBlank` del DTO (400). |
| Schema incorrecto en `application.yml` | Hibernate falla al validar: `Schema-validation: missing table [persona]`. |
| Nombre de columna incorrecto en `@Column` | Hibernate falla al validar: `missing column`. |
