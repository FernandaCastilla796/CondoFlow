# Decisiones iniciales — CondoFlow PA-04

## 1. Propósito

Este documento registra las decisiones iniciales tomadas durante la Clase 01 para mantener trazabilidad sobre el proyecto.

## 2. Decisiones

### D-01 — Nombre del proyecto

**Decisión:** El proyecto se denomina CondoFlow y corresponde al código PA-04.

**Motivo:** Es el proyecto asignado para el desarrollo del sistema de administración operativa de condominio.

### D-02 — Alcance inicial

**Decisión:** El MVP se enfocará en la administración operativa del condominio.

**Incluye inicialmente:**

- Unidades.
- Personas y residencias.
- Áreas comunes.
- Reservas.
- Visitas.
- Incidencias.
- Comunicados.
- Mantenimiento.
- Notificaciones.

### D-03 — Flujo crítico inicial

**Decisión:** Se considerarán como flujos críticos iniciales el registro de residencia, el control de visitas y la reserva de áreas comunes.

**Motivo:** Estos procesos representan operaciones centrales del dominio que deberán ser consideradas durante el modelado posterior.

### D-04 — Tecnología

**Decisión:** El proyecto utilizará Java 21, Spring Boot, PostgreSQL, React + TypeScript, React Native + TypeScript, Docker y GitHub Actions.

**Motivo:** Estas tecnologías forman parte del stack definido para el proyecto.

### D-05 — Diseño de base de datos

**Decisión:** En la Clase 01 no se definirá todavía el modelo físico de la base de datos.

**Motivo:** El modelado conceptual, relacional y las decisiones de integridad se realizarán en clases posteriores.

### D-06 — Historias de usuario

**Decisión:** El backlog inicial tendrá historias priorizadas mediante P0, P1 y P2.

**Motivo:** Permite identificar qué funcionalidades pertenecen al núcleo del MVP y cuáles tienen menor prioridad.

## 3. Decisiones pendientes

Las siguientes decisiones se resolverán en las clases posteriores:

- Definir el modelo conceptual.
- Identificar entidades y relaciones.
- Definir cardinalidades.
- Definir claves primarias y foráneas.
- Definir restricciones de integridad.
- Diseñar el modelo relacional.
- Diseñar el DER.
- Definir la implementación técnica de los módulos.

> Las decisiones pendientes de arriba se resolvieron en `docs/04-model/` (Clases 02–05) y en las decisiones D-07 a D-14.

## 4. Decisiones de arquitectura e implementación

### D-07 — Monolito modular con arquitectura hexagonal simplificada

**Decisión:** El backend es una sola aplicación Spring Boot dividida en módulos de negocio (`person`, `unit`, `residence`). Cada módulo tiene `domain` (modelo, excepciones, puertos IN/OUT), `application` (casos de uso) e `infrastructure` (adaptadores web y de persistencia).

**Motivo:** Es la arquitectura exigida por la asignatura (RNF-04). Separar puertos y adaptadores permite probar las reglas sin base de datos (`PersonaServiceTest`, `ResidenciaServiceTest`) y evita mezclar controllers, JPA y reglas de negocio.

**Alternativa descartada:** paquetes globales `controller/`, `service/`, `repository/` para todo el sistema, porque mezclan módulos y dificultan ver las dependencias.

### D-08 — Los módulos se comunican por sus Port IN

**Decisión:** `residence` valida que la persona y la unidad existan usando `ConsultarPersonaUseCase` y `ConsultarUnidadUseCase`, nunca los `JpaRepository` de otros módulos.

**Motivo:** Cada módulo expone un contrato público y oculta su persistencia. El único lugar que usa las entidades JPA de otro módulo es el adaptador de persistencia (para `getReferenceById` y el `@ManyToOne`).

### D-09 — Esquema versionado con Flyway y `ddl-auto=validate`

**Decisión:** Las tablas se crean sólo con migraciones `db/migration/V*.sql` en el schema `condoflow`. Hibernate valida el mapeo pero no crea tablas. Una migración ya aplicada nunca se edita: se agrega una nueva (V3).

**Motivo:** Base reproducible en cualquier máquina y en CI; el modelo físico diseñado por el equipo es el contrato.

### D-10 — Validación en tres niveles

**Decisión:** Formato en el Request DTO (Bean Validation), reglas de negocio en el caso de uso y restricciones de integridad en PostgreSQL (FK, UNIQUE, CHECK).

**Motivo:** El backend es la autoridad final (RF-03) y la base es la última línea de defensa si dos peticiones concurrentes pasan la validación del caso de uso.

### D-11 — Contrato único de errores

**Decisión:** Todas las respuestas de error usan `ApiError` a través de `GlobalExceptionHandler` (400/404/409/500). Nunca se devuelven mensajes internos de PostgreSQL ni stack traces.

**Motivo:** Web y móvil muestran errores de forma uniforme; se evita filtrar detalles internos.

### D-12 — Credenciales fuera del repositorio

**Decisión:** Usuario y contraseña de la base se leen de variables de entorno (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) o de archivos `.env` ignorados por Git. Sólo se versionan plantillas `.env.example`.

**Motivo:** RNF-11 y regla del curso: no almacenar contraseñas ni `.env` reales en Git.

### D-13 — Estados como VARCHAR + CHECK + enum en Java

**Decisión:** Los estados se guardan como texto controlado por CHECK en PostgreSQL y se representan como `enum` en Java y como unión de strings en TypeScript.

**Motivo:** Legibles en SQL, protegidos en la base y verificados en compilación en Java y TypeScript. Valores definidos en V3: persona `ACTIVO/INACTIVO`; residencia `VIGENTE/FINALIZADA` y tipo `PROPIETARIO/INQUILINO`; unidad `ACTIVA/INACTIVA`; área común `ACTIVA/INACTIVA/EN_MANTENIMIENTO`; reserva `PENDIENTE/CONFIRMADA/CANCELADA/FINALIZADA`.

### D-14 — Flujo de trabajo con Git

**Decisión:**

- `main` es la rama estable y siempre debe compilar (CI verde).
- Cada cambio se hace en una rama corta: `feat/...`, `fix/...`, `docs/...`.
- Se integra a `main` sólo mediante pull request revisado por otro integrante.
- Commits pequeños con prefijo convencional: `feat:`, `fix:`, `docs:`, `test:`, `chore:`.

**Motivo:** Evidencia de colaboración y trazabilidad exigida por la rúbrica (15% del componente grupal).

## 5. Deuda técnica conocida

- Autenticación y roles (RF-01, RN-06) todavía no implementados.
- Los listados no tienen paginación (RNF-13).
- `visita`, `incidencia` y `tarea_mantenimiento` aún no tienen tabla ni módulo.
- La aplicación móvil (React Native) todavía no se inició.
