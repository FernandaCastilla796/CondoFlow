# CondoFlow — Administración Operativa de Condominio

Proyecto integrador **PA-04** de Programación Aplicada 2026-2 (UPSA).

Sistema full-stack para la administración operativa de condominios: unidades, residentes, áreas comunes, reservas, incidencias, visitas, comunicados, mantenimiento y notificaciones. No incluye contabilidad, expensas ni pagos reales (RN-08).

## 1. Problema

La administración de un condominio suele depender de registros manuales, hojas de cálculo y distintos medios de comunicación para controlar residentes, unidades, reservas, visitas, incidencias y tareas de mantenimiento. La información queda dispersa, es difícil hacer seguimiento y no hay visibilidad del estado operativo.

## 2. Actores

| Actor | Canal principal |
|---|---|
| Administrador | Web |
| Portería/seguridad | Web / móvil |
| Residente | Móvil |
| Personal de mantenimiento | Móvil |

Detalle en [`docs/02-requirements/requisitos-v0.1.md`](docs/02-requirements/requisitos-v0.1.md).

## 3. Stack

| Capa | Tecnología |
|---|---|
| Backend | Java 21 · Spring Boot 4 · Spring Data JPA/Hibernate · Bean Validation |
| Base de datos | PostgreSQL 16 · Flyway |
| Web | React 19 · TypeScript · Vite |
| Móvil | React Native + TypeScript *(pendiente)* |
| DevOps | Docker Compose · GitHub Actions |
| Documentación de API | OpenAPI / Swagger UI |

## 4. Estado actual

| Área | Estado |
|---|---|
| Documentación de análisis y modelo de datos (Clases 01–05) | ✅ |
| PostgreSQL: V1 núcleo, V2 datos de prueba, V3 alineación con el modelo físico (Clases 06–08) | ✅ |
| Java esencial, capítulos 01–02 (`backend-lab/`) | ✅ |
| Backend Spring Boot, capítulos 03–08: Persona → Residencia con arquitectura hexagonal, errores y transacciones | ✅ |
| Frontend: estructura React + TypeScript + Vite (Guía 01) | ✅ |
| Docker Compose + CI | ✅ |
| Autenticación/roles, resto de módulos, móvil, Spring AI | ⬜ próximos cortes |

## 5. Cómo ejecutar

### Opción A — Docker Compose (PostgreSQL + backend)

```bash
cp .env.example .env
```

Editar `.env` y cambiar `POSTGRES_PASSWORD`. Luego:

```bash
docker compose up --build
```

- API: http://localhost:8080/api/health
- Swagger UI: http://localhost:8080/swagger-ui.html
- PostgreSQL (DataGrip): `localhost:5433`, base `condoflow`, schema `condoflow`

Flyway aplica automáticamente las migraciones de `backend/src/main/resources/db/migration`.

### Opción B — Backend local con PostgreSQL instalado

1. Crear la base y el usuario en DataGrip/psql:

   ```sql
   CREATE USER condoflow WITH PASSWORD 'tu_clave';
   CREATE DATABASE condoflow OWNER condoflow;
   ```

2. Copiar `backend/.env.example` como `backend/.env` y completar `DB_PASSWORD`.
3. Ejecutar `CondoflowBackendApplication` desde IntelliJ (JDK 21), o bien:

   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Abrir http://localhost:5173.

### Pruebas

```bash
cd backend
./mvnw test
```

Las pruebas de integración usan Testcontainers: corren si hay Docker disponible (siempre en GitHub Actions) y se omiten si no lo hay.

## 6. Estructura del repositorio

```text
CondoFlow/
├── backend/                 Spring Boot (condoflow-backend)
│   ├── src/main/java/com/condoflow/
│   │   ├── person/          módulo Persona (domain · application · infrastructure)
│   │   ├── unit/            módulo Unidad (consulta)
│   │   ├── residence/       módulo Residencia (relación 1:N con Persona)
│   │   └── shared/          health, ApiError, GlobalExceptionHandler, CORS/OpenAPI
│   ├── src/main/resources/db/migration/   V1, V2, V3 (Flyway)
│   ├── requests.http        pruebas manuales de la API
│   └── Dockerfile
├── backend-lab/             Java puro de los capítulos 01–02
├── frontend/                React + TypeScript + Vite (condoflow-frontend)
├── docs/
│   ├── 01-vision/           visión y glosario
│   ├── 02-requirements/     backlog, requisitos RF/RNF/RN y actores
│   ├── 03-decisions/        registro de decisiones (D-01…D-14) y deuda técnica
│   ├── 04-model/            modelo conceptual, relacional, DER, diccionario, físico, plan de migración
│   ├── 05-database/         consultas de las Clases 07 y 08
│   ├── 05-java/             README de los capítulos 01–08
│   └── 06-api/              catálogo de la API
├── docker-compose.yml
└── .github/workflows/ci.yml
```

## 7. Documentación

| Tema | Documento |
|---|---|
| Visión y alcance | [`docs/01-vision/vision-v0.1.md`](docs/01-vision/vision-v0.1.md) |
| Glosario | [`docs/01-vision/glossary-v0.1.md`](docs/01-vision/glossary-v0.1.md) |
| Backlog (historias de usuario) | [`docs/02-requirements/backlog-v0.1.md`](docs/02-requirements/backlog-v0.1.md) |
| Requisitos, reglas de negocio y trazabilidad | [`docs/02-requirements/requisitos-v0.1.md`](docs/02-requirements/requisitos-v0.1.md) |
| Decisiones técnicas | [`docs/03-decisions/README.md`](docs/03-decisions/README.md) |
| Modelo de datos | [`docs/04-model/`](docs/04-model) |
| Migraciones y restricciones | [`docs/04-model/plan-migracion-v1.md`](docs/04-model/plan-migracion-v1.md) |
| Backend capítulo por capítulo | [`docs/05-java/`](docs/05-java) |
| API | [`docs/06-api/api-v1.md`](docs/06-api/api-v1.md) |
| Frontend | [`frontend/README-front.md`](frontend/README-front.md) |

## 8. Flujo de trabajo

`main` es la rama estable. Cada cambio se hace en una rama `feat/...`, `fix/...` o `docs/...` y entra a `main` mediante pull request revisado por otro integrante, con el CI en verde (decisión D-14).
