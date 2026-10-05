# CondoFlow — Administración Operativa de Condominio

Sistema full-stack para la administración operativa de condominios, orientado a centralizar la gestión de unidades, residentes, áreas comunes, reservas, incidencias, visitas, comunicados, mantenimiento y notificaciones.

## 1. Problema

La administración de un condominio puede depender de registros manuales, hojas de cálculo y diferentes medios de comunicación para controlar información de residentes, unidades, reservas, visitas, incidencias y tareas de mantenimiento. Esto puede generar información dispersa, dificultades para realizar seguimiento y poca visibilidad sobre el estado operativo del condominio.

## 2. Objetivo del MVP

Construir una plataforma web y móvil que permita centralizar la administración operativa del condominio, gestionando unidades, personas y residencias, áreas comunes, reservas, incidencias, visitas, comunicados, tareas de mantenimiento y notificaciones.

## 3. Actores principales

- Administrador
- Residente
- Personal de mantenimiento
- Personal de seguridad
- Supervisor

## 4. Alcance inicial

- Gestión de unidades del condominio.
- Gestión de personas y residencias.
- Gestión de áreas comunes.
- Gestión de reservas.
- Registro y seguimiento de incidencias.
- Gestión de visitas.
- Gestión de comunicados.
- Gestión de tareas de mantenimiento.
- Gestión de notificaciones.
- Panel operativo para consultar información relevante.

## 5. Fuera de alcance

- Contabilidad, expensas y pagos reales (RN-08 de la ficha PA-04).
- Integración con sistemas físicos de control de acceso.
- Automatización mediante dispositivos IoT.
- Integraciones bancarias reales.
- Facturación fiscal.
- Funcionalidades que no estén contempladas en la ficha oficial del proyecto.

## 6. Stack objetivo del semestre

- Backend: Java 21 + Spring Boot
- Base de datos: PostgreSQL + Flyway
- Web: React + TypeScript
- Móvil: React Native + TypeScript
- Pruebas API: Postman
- Contenedores: Docker / Docker Compose
- Versionado: Git + GitHub
- CI: GitHub Actions
- IA: Spring AI, únicamente como capacidad complementaria

## 7. Estado actual

| Avance | Estado |
|---|---|
| Clases 01–05: problema, alcance, backlog, modelo conceptual, relacional, DER lógico, diccionario, modelo físico y plan de migración | ✅ |
| Clases 06–08: PostgreSQL con migraciones V1 (núcleo), V2 (datos) y V3 (alineación con el modelo físico); consultas y JOIN | ✅ |
| Capítulos 01–02: Java 21 esencial en `condoflow-backend-lab/` | ✅ |
| Capítulos 03–08: backend Spring Boot con Persona → Residencia, arquitectura hexagonal, errores y transacciones | ✅ |
| Frontend Guía 01: estructura React + TypeScript + Vite en `web/` | ✅ |
| Frontend Guía 02: React Router, layout principal y menú | ✅ |
| Móvil, autenticación y resto de módulos | ⬜ próximas clases |

## 8. Documentación

- `docs/01-vision/vision-v0.1.md`
- `docs/01-vision/glossary-v0.1.md`
- `docs/02-requirements/backlog-v0.1.md`
- `docs/02-requirements/requisitos-v0.1.md`
- `docs/03-decisions/`
- `docs/04-model/` (modelo conceptual, relacional, DER lógico, diccionario de datos, decisiones de integridad, convenciones, modelo físico, plan de migración)
- `docs/05-database/` (evidencia de la Clase 06 y consultas de las Clases 07 y 08)
- `docs/05-java/` (README de los capítulos 01 a 08)
- `docs/06-api/` (catálogo de la API y colección de Postman)
- `web/README-front.md`

## 9. Regla de trabajo

Cada cambio importante debe ser comprensible, trazable y defendible. El repositorio es la fuente de verdad del proyecto.

## 10. Cómo levantar cada componente

### Base de datos (Guía DataGrip)

Conectado como `postgres` en DataGrip:

```sql
CREATE ROLE condoflow_admin
WITH
    LOGIN
    PASSWORD 'Cambia_Esta_Clave'
    SUPERUSER
    CREATEDB
    CREATEROLE
    INHERIT;

CREATE DATABASE condoflow
    WITH
    OWNER = condoflow_admin
    ENCODING = 'UTF8';
```

Cambiar la contraseña de ejemplo y no subirla a GitHub. Las tablas no se crean a mano: las crea Flyway con las migraciones de `backend/src/main/resources/db/migration` en el schema `condoflow` al arrancar el backend.

### Backend

1. Copiar `backend/.env.example` como `backend/.env` y poner la contraseña de `condoflow_admin`.
2. Abrir `backend/` en IntelliJ IDEA (JDK 21) y ejecutar `CondoflowBackendApplication`, o desde la terminal:

   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

3. Probar con `backend/requests.http`, con la colección de Postman de `docs/06-api/` o con Swagger UI en http://localhost:8080/swagger-ui.html.

### Web

```bash
cd web
npm install
npm run dev
```

Abrir http://localhost:5173.

### Docker Compose (alternativa)

```bash
cp .env.example .env
docker compose up --build
```

Levanta PostgreSQL (puerto 5433 del host) y el backend (puerto 8080).

### Pruebas

```bash
cd backend
./mvnw test
```

## 11. Estructura del repositorio

```text
CondoFlow/
├── backend/                  Spring Boot (condoflow-backend) · migraciones en src/main/resources/db/migration
├── condoflow-backend-lab/    Java 21 puro de los Capítulos 01 y 02
├── web/                      React + TypeScript + Vite (condoflow-frontend)
├── docs/                     documentación del proyecto
├── docker-compose.yml
└── .github/workflows/ci.yml
```

La carpeta `mobile/` se agregará cuando empiece React Native.
