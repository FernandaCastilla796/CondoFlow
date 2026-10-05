# Decisiones iniciales — CondoFlow PA-04

## 1. Propósito

Este documento registra las decisiones iniciales tomadas durante la Clase 01 para mantener trazabilidad sobre el proyecto.

A partir de D-07, cada decisión sigue el formato indicado en la guía de la Clase 01:

1. contexto;
2. problema o decisión;
3. alternativas consideradas;
4. decisión tomada;
5. consecuencias conocidas.

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

> Estas decisiones se resolvieron en `docs/04-model/` (Clases 02–05) y en las decisiones D-07 a D-14.

## 4. Decisiones técnicas (Clases 06–08 y Capítulos 01–08)

### D-07 — Monolito modular con arquitectura hexagonal simplificada

1. **Contexto:** La Guía Formal exige un backend Java 21 + Spring Boot organizado como monolito modular con arquitectura hexagonal simplificada (Capítulos 03 a 08).
2. **Problema:** Cómo organizar paquetes para que HTTP, reglas de negocio y persistencia no queden mezclados.
3. **Alternativas consideradas:** paquetes globales `controller/`, `service/`, `repository/` para todo el sistema; microservicios; módulos por funcionalidad con capas internas.
4. **Decisión tomada:** módulos por funcionalidad bajo `com.condoflow` (`person`, `residence`, `unit`, `shared`), cada uno con `domain` (model, exception, port/in, port/out), `application` (service, command) e `infrastructure/adapter` (in/web, out/persistence).
5. **Consecuencias:** las reglas se prueban sin base de datos (`PersonaServiceTest`, `ResidenciaServiceTest`); hay más archivos (mappers, puertos) que en un diseño por capas plano.

### D-08 — Validar los padres mediante Port IN de consulta

1. **Contexto:** `residencia` tiene dos FK: `persona_id` y `unidad_id`.
2. **Problema:** Cómo verifica el módulo `residence` que la persona y la unidad existan sin acoplarse a la persistencia de otros módulos.
3. **Alternativas consideradas:** usar `SpringDataPersonaRepository` desde `ResidenciaService` (prohibido por el Capítulo 07); dejar que sólo la FK de PostgreSQL lo detecte; usar el Port IN de consulta de cada módulo padre.
4. **Decisión tomada:** `ResidenciaService` usa `ConsultarPersonaUseCase` y `ConsultarUnidadUseCase`. Para la segunda FK se creó el módulo `unit` con el mismo patrón del Capítulo 07.
5. **Consecuencias:** el usuario recibe un 404 claro en lugar de un error de la base; la FK sigue en PostgreSQL como última defensa.

### D-09 — Migraciones con Flyway y `ddl-auto=validate`

1. **Contexto:** Clase 06: el DDL debe estar en `backend/src/main/resources/db/migration/V1__init_core.sql` y ser reproducible. Capítulo 05: usar `ddl-auto=validate`.
2. **Problema:** V1 quedó con menos columnas que el modelo físico y ya había sido ejecutada por los integrantes.
3. **Alternativas consideradas:** editar V1; crear tablas a mano en DataGrip; agregar una nueva migración.
4. **Decisión tomada:** Flyway aplica V1, V2 y V3 al arrancar en el schema `condoflow`. V3 agrega lo que faltaba sin modificar V1 ni V2.
5. **Consecuencias:** cualquier integrante recrea la base desde cero; si alguien creó las tablas a mano antes de Flyway, debe borrar el schema `condoflow` y dejar que Flyway lo cree.

### D-10 — Validación en tres niveles

1. **Contexto:** Capítulo 08, sección "Clasifica tus validaciones".
2. **Problema:** Dónde vive cada regla.
3. **Alternativas consideradas:** validar todo en el controller; validar sólo en la base; separar por tipo de regla.
4. **Decisión tomada:** formato en el Request DTO (Bean Validation), negocio en el caso de uso, integridad en PostgreSQL (FK, UNIQUE, CHECK).
5. **Consecuencias:** errores claros (400/404/409) y la base protegida aunque falle una validación previa.

### D-11 — Contrato único de errores

1. **Contexto:** Capítulo 08: `ApiError` y `GlobalExceptionHandler` en `shared/web`.
2. **Problema:** Cada controller podía responder errores con formatos distintos.
3. **Alternativas consideradas:** `try/catch` en cada controller; `@RestControllerAdvice` centralizado.
4. **Decisión tomada:** `ApiError(timestamp, status, error, message, path, fieldErrors)` para todos los errores 400, 404, 409 y 500.
5. **Consecuencias:** controllers sin `try/catch`; nunca se devuelven mensajes internos de PostgreSQL.

### D-12 — Base, usuario y credenciales

1. **Contexto:** Guía DataGrip (usuario `<proyecto>_admin` dueño de la base) y Capítulo 05 ("`application.yml` sin publicar credenciales reales").
2. **Problema:** Conectar Spring Boot sin subir contraseñas a GitHub.
3. **Alternativas consideradas:** escribir la contraseña en `application.yml`; usar variables de entorno.
4. **Decisión tomada:** base `condoflow`, usuario `condoflow_admin`, schema `condoflow`. La contraseña se lee de `DB_PASSWORD` (variable de entorno o `backend/.env`, ignorado por Git). Sólo se versiona `backend/.env.example`.
5. **Consecuencias:** cada integrante crea su propio `backend/.env` antes de ejecutar.

### D-13 — Estados como VARCHAR + CHECK + enum

1. **Contexto:** Clase 06 (CHECK para estados) y Capítulos 01, 02 y 07 (enum cuando el conjunto es cerrado).
2. **Problema:** El modelo físico decía "dominio controlado" pero no definía los valores.
3. **Alternativas consideradas:** texto libre; tabla catálogo de estados; VARCHAR con CHECK.
4. **Decisión tomada:** VARCHAR + CHECK en V3 y `enum` en Java. Valores: persona `ACTIVO/INACTIVO`; residencia `VIGENTE/FINALIZADA` y tipo `PROPIETARIO/INQUILINO`; unidad `ACTIVA/INACTIVA`; área común `ACTIVA/INACTIVA/EN_MANTENIMIENTO`; reserva `PENDIENTE/CONFIRMADA/CANCELADA/FINALIZADA`.
5. **Consecuencias:** un valor inválido se rechaza en la base y en Java. Los valores de unidad, área común y reserva deben ser confirmados por el equipo.

### D-14 — Flujo de trabajo con Git

1. **Contexto:** Manual de Git y GitHub, secciones 9, 11 y 16.
2. **Problema:** Cómo integrar cambios sin romper `main`.
3. **Alternativas consideradas:** trabajar directo en `main`; ramas por funcionalidad con Pull Request.
4. **Decisión tomada:**
   - `main` es la rama estable y debe mantenerse ejecutable.
   - Ramas cortas con los prefijos del manual: `feature/`, `fix/`, `refactor/`, `docs/`, `test/`.
   - Integración mediante Pull Request con título, descripción, requisito que resuelve, cómo probar y checklist.
   - Commits con prefijos `feat`, `fix`, `docs`, `refactor`, `test`, `chore`.
5. **Consecuencias:** el historial de Git sirve como evidencia de colaboración.

### D-15 — Conexión del frontend con la API (CORS y URL base)

1. **Contexto:** Guía 05 del frontend: React (Vite, `http://localhost:5173`) consume la API de Spring Boot (`http://localhost:8080`).
2. **Problema:** son orígenes distintos (cambia el puerto), así que el navegador bloquea las respuestas si el backend no lo autoriza; y la URL de la API no debe repetirse en cada componente.
3. **Alternativas consideradas:** `@CrossOrigin` en cada controller; configuración global de CORS; proxy de Vite.
4. **Decisión tomada:**
   - CORS global en `shared/web/WebConfig` para `/api/**`, sólo para el origen `http://localhost:5173` y los métodos GET, POST, PUT, DELETE y OPTIONS.
   - La URL base vive en `web/.env.development` (`VITE_API_URL`) y la usa un único `apiClient`; los componentes llaman a services, nunca a `fetch` directamente.
   - `GET /api/residencias` lista todas las residencias para la pantalla del frontend.
5. **Consecuencias:** en producción hay que autorizar el dominio real del frontend. CORS no autentica usuarios: la autenticación sigue pendiente. Las variables `VITE_` llegan al navegador, por eso no guardan secretos.

## 5. Deuda técnica conocida

- Autenticación y roles (RF-01, RN-06) todavía no implementados.
- No existe todavía el caso de uso para cerrar la vigencia de una residencia (RN-01).
- Los listados no tienen paginación (RNF-13).
- `visita`, `incidencia` y `tarea_mantenimiento` aún no tienen tabla ni módulo.
- La aplicación móvil (React Native) todavía no se inició.
