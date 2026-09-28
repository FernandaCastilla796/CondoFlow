# Requisitos v0.1 — CondoFlow PA-04

Fuente: ficha oficial **PA-04 CondoFlow** del *Banco Oficial de Proyectos Programación Aplicada 2026-2*. Los IDs se conservan tal como aparecen en la ficha para mantener la trazabilidad.

Estados usados en las tablas: ✅ implementado · 🟡 parcial · ⬜ pendiente (corte posterior).

## 1. Matriz de actores

| Actor | Objetivo | Responsabilidades | Canal principal |
|---|---|---|---|
| Administrador | Supervisar la operación completa del condominio | Configura catálogos, usuarios y parámetros; registra unidades, personas y residencias; clasifica y asigna incidencias; publica comunicados | Web |
| Portería/seguridad | Controlar el ingreso al condominio | Registra visitas y consulta las autorizadas; sólo opera visitas y consultas permitidas (RN-06) | Web / móvil |
| Residente | Resolver sus necesidades de autoservicio | Consulta comunicados, reporta incidencias con evidencia, reserva áreas comunes, registra visitas esperadas | Móvil |
| Personal de mantenimiento | Ejecutar el trabajo asignado | Consulta sus tareas, actualiza estados y aporta evidencias/seguimiento | Móvil |

> **Observación:** `vision-v0.1.md` incluye un actor *Supervisor* que no está en la ficha oficial. El equipo debe decidir si se elimina o si se documenta como parte del rol Administrador.

## 2. Requisitos funcionales

| ID | Requisito (ficha PA-04) | Estado | Evidencia / endpoint |
|---|---|---|---|
| RF-01 | Autenticar usuarios y resolver permisos según el rol vigente | ⬜ | Parcial 2 |
| RF-02 | Consultar listados con filtros y mensajes claros cuando no hay resultados | 🟡 | `GET /api/personas?buscar=` |
| RF-03 | Validar datos obligatorios en cliente y servidor; el backend es la autoridad final | 🟡 | Bean Validation + `ApiError.fieldErrors` (backend). Validación en cliente: pendiente |
| RF-04 | Registrar fecha/usuario en operaciones que cambian el estado de un proceso | 🟡 | `residencia.fecha_inicio` / `fecha_fin`. Usuario: pendiente (requiere RF-01) |
| RF-05 | Módulo de unidades y residentes | 🟡 | `/api/personas`, `/api/residencias`, `GET /api/unidades` |
| RF-06 | Módulo de comunicados | ⬜ | |
| RF-07 | Módulo de incidencias | ⬜ | |
| RF-08 | Módulo de áreas comunes | ⬜ | Tabla `area_comun` creada (V1/V3) |
| RF-09 | Módulo de reservas | ⬜ | Tabla `reserva` creada (V1/V3) |
| RF-10 | Módulo de visitas | ⬜ | |
| RF-11 | Módulo de tareas de mantenimiento | ⬜ | |
| RF-12 | Módulo de notificaciones | ⬜ | |
| RF-13 | Módulo de panel operativo | ⬜ | |
| RF-14 | Registrar residencia vigente | ✅ | `POST /api/residencias` |
| RF-15 | Evitar solapamiento de reservas | ⬜ | |
| RF-16 | Reportar incidencia desde móvil con evidencia | ⬜ | |
| RF-17 | Asignar y resolver incidencia | ⬜ | |
| RF-18 | Registrar visita y validar consulta de portería | ⬜ | |
| RF-19 | Filtrar indicadores por categoría/estado | ⬜ | |

## 3. Requisitos no funcionales

| ID | Área | Requisito | Estado | Evidencia |
|---|---|---|---|---|
| RNF-01 | Usabilidad | Navegación consistente, mensajes claros, estados de carga y validaciones visibles | ⬜ | Frontend en Guía 01 (estructura) |
| RNF-02 | Seguridad | Autenticación y autorización por roles; contraseñas nunca en texto plano | ⬜ | Parcial 2 |
| RNF-03 | Integridad | PK, FK, UNIQUE, NOT NULL, CHECK e índices razonables | ✅ | V1 + V3 (CHECK de estados, UNIQUE parcial, índices sobre FK) |
| RNF-04 | Mantenibilidad | Monolito modular con arquitectura hexagonal simplificada | ✅ | Módulos `person`, `unit`, `residence` con puertos y adaptadores |
| RNF-05 | Calidad | Pruebas unitarias de reglas críticas e integración de repositorios/endpoints | 🟡 | 30 pruebas: unitarias de servicios, MockMvc de controllers, integración con Testcontainers |
| RNF-06 | Trazabilidad | Registrar usuario, fecha y cambio de estado | ⬜ | Requiere RF-01 |
| RNF-07 | API | API prefijada, códigos HTTP correctos, DTOs y errores consistentes | ✅ | Prefijo `/api`, `ApiError`, Swagger en `/swagger-ui.html` |
| RNF-08 | Web | React + TypeScript sin `any`, formularios tipados, consumo de API centralizado | 🟡 | Estructura y tipos (Guía 01) |
| RNF-09 | Móvil | React Native + TypeScript con navegación y un flujo transaccional | ⬜ | Bloque de clases 31–35 |
| RNF-10 | DevOps | Docker Compose con PostgreSQL y backend; CI que compile y pruebe | ✅ | `docker-compose.yml`, `.github/workflows/ci.yml` |
| RNF-11 | Configuración | Secretos por variables de entorno; sin credenciales versionadas | ✅ | `.env.example`, `backend/.env.example`, `.gitignore` |
| RNF-12 | Documentación | README ejecutable, decisiones, modelo, API, pruebas y despliegue | 🟡 | `README.md`, `docs/` |
| RNF-13 | Rendimiento | Paginación y consultas sin N+1 en flujos críticos | 🟡 | `@ManyToOne(LAZY)` + `getReferenceById`; paginación pendiente |
| RNF-14 | Observabilidad | Logging básico, manejo global de errores | 🟡 | `GlobalExceptionHandler` con log de errores 500 |
| RNF-15 | IA responsable | Spring AI complementario, auditable, con fallback | ⬜ | Examen final |

## 4. Reglas de negocio

| ID | Regla (ficha PA-04) | Estado | Dónde se implementa |
|---|---|---|---|
| RN-01 | Una unidad puede tener varios residentes con vigencia temporal | ✅ | `residencia.fecha_inicio/fecha_fin/estado`; `Residencia.finalizar()`; UNIQUE parcial `uq_residencia_vigente_persona_unidad`; `ResidenciaService` → 409 si ya existe una vigente |
| RN-02 | Las reservas deben respetar aforo, horario y no solapamiento | ⬜ | CHECK `fecha_fin > fecha_inicio` ya existe; aforo y solapamiento en el caso de uso de reservas |
| RN-03 | Estados de incidencia: REPORTADA, ASIGNADA, EN_PROCESO, RESUELTA, CERRADA, RECHAZADA | ⬜ | |
| RN-04 | Una incidencia registra categoría, prioridad, descripción, unidad/área y responsable | ⬜ | |
| RN-05 | Una visita se vincula a una unidad y tiene fecha prevista | ⬜ | |
| RN-06 | Portería sólo opera visitas y consultas autorizadas | ⬜ | Requiere RF-01 |
| RN-07 | Los comunicados pueden dirigirse a todos o a segmentos | ⬜ | |
| RN-08 | No se implementan contabilidad, expensas ni pagos reales | ✅ | Fuera de alcance (ninguna tabla ni endpoint de pagos) |

Reglas internas agregadas por el equipo:

| ID | Regla | Dónde se implementa |
|---|---|---|
| RI-01 | El correo electrónico identifica a una sola persona (sin distinguir mayúsculas) | `uq_persona_correo` + `PersonaService` → 409 |
| RI-02 | Una residencia sólo se registra si la persona y la unidad existen | FK + `ResidenciaService` → 404 |
| RI-03 | Sólo una residencia VIGENTE puede finalizarse, con `fecha_fin >= fecha_inicio` | `ck_residencia_fechas`, `ck_residencia_estado_fecha_fin` + dominio → 400 / 409 |

## 5. Mapa de casos de uso

| Actor | Casos de uso | Estado |
|---|---|---|
| Administrador | Registrar persona · Consultar/buscar personas · Registrar residencia · Finalizar residencia · Consultar unidades | ✅ |
| Administrador | Gestionar unidades · Clasificar/asignar incidencia · Publicar comunicado · Ver panel operativo | ⬜ |
| Residente | Reportar incidencia con evidencia · Consultar avance · Reservar área común · Registrar visita esperada · Ver comunicados | ⬜ |
| Portería/seguridad | Registrar visita · Validar visita autorizada | ⬜ |
| Mantenimiento | Ver mis tareas · Actualizar estado de tarea | ⬜ |

## 6. Flujo crítico oficial

Según la ficha PA-04, el flujo que debe demostrarse de extremo a extremo es:

> Residente reporta incidencia → administración clasifica/asigna → mantenimiento trabaja → residente consulta avance → incidencia se resuelve y cierra.

Es obligatorio en la **Parte II** (hasta el punto central, con persistencia real y vistas web y móvil conectadas). Para la **Parte I** la ficha pide dejar modelados Unidad, Persona, Residencia, AreaComun y ReservaArea, demostrar el alta/consulta de dos recursos principales (**Persona** y **Residencia**) y el **esqueleto** de este flujo.

> **Observación:** `vision-v0.1.md` y la decisión D-03 definieron como flujos críticos la residencia, las visitas y las reservas. Conviene alinearlos con el flujo oficial de incidencias.
