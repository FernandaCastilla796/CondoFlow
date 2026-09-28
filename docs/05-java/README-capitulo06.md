# Capítulo 06 - Arquitectura hexagonal aplicada a Persona

## Equivalencia

| Elemento | CondoFlow |
|---|---|
| Proyecto | `condoflow-backend` |
| Package base | `com.condoflow` |
| Entidad padre | `Persona` |
| Tabla PostgreSQL | `condoflow.persona` |
| PK | `persona_id` |
| Campo UNIQUE | `correo_electronico` (`uq_persona_correo`) |
| Schema | `condoflow` |

## Flujo

```text
POST /api/personas
   ↓
PersonaController                  ADAPTER IN   (infrastructure/adapter/in/web)
   ↓
RegistrarPersonaUseCase            PORT IN      (domain/port/in)
   ↓
PersonaService                     APPLICATION  (application/service)
   ↓  regla: correo no duplicado
Persona                            DOMAIN       (domain/model)
   ↓
PersonaRepositoryPort              PORT OUT     (domain/port/out)
   ↓
PersonaPersistenceAdapter          ADAPTER OUT  (infrastructure/adapter/out/persistence)
   ↓
SpringDataPersonaRepository → PersonaJpaEntity → PostgreSQL
```

## Estructura del módulo

```text
person
├── domain
│   ├── model          Persona, EstadoPersona
│   ├── exception      CorreoPersonaDuplicadoException, PersonaNoEncontradaException
│   └── port
│       ├── in         RegistrarPersonaUseCase, ConsultarPersonaUseCase
│       └── out        PersonaRepositoryPort
├── application
│   └── service        PersonaService
└── infrastructure/adapter
    ├── in/web         PersonaController, dto/, mapper/PersonaWebMapper
    └── out/persistence
        ├── PersonaPersistenceAdapter
        ├── entity     PersonaJpaEntity
        ├── mapper     PersonaPersistenceMapper
        └── repository SpringDataPersonaRepository
```

## Checklist de arquitectura

- [x] El controller **no** inyecta `JpaRepository`: depende de `RegistrarPersonaUseCase` y `ConsultarPersonaUseCase`.
- [x] El caso de uso depende de un Port OUT (`PersonaRepositoryPort`).
- [x] El Port OUT **no** extiende `JpaRepository`.
- [x] La entidad de dominio no tiene `@Entity`.
- [x] La entidad JPA está dentro de `infrastructure`.
- [x] Hay mapper web (`PersonaWebMapper`) y de persistencia (`PersonaPersistenceMapper`).

## Respuestas de defensa

| Pregunta | Respuesta |
|---|---|
| ¿Cuál es el Port IN? | `RegistrarPersonaUseCase` (registrar) y `ConsultarPersonaUseCase` (buscar por id y listar). Expresan **qué** puede hacer el sistema. |
| ¿Cuál es el Port OUT y por qué está en el núcleo? | `PersonaRepositoryPort`. Lo define el núcleo según lo que **necesita**; así la dependencia apunta hacia adentro (inversión de dependencias). |
| ¿Qué clase implementa el Port OUT? | `PersonaPersistenceAdapter`. |
| ¿Qué clase conoce `JpaRepository`? | Sólo `PersonaPersistenceAdapter` (a través de `SpringDataPersonaRepository`). |
| ¿Qué clase conoce el nombre físico de la tabla? | Sólo `PersonaJpaEntity` (`@Table(name = "persona", schema = "condoflow")`). |
| Si cambiamos PostgreSQL por otra tecnología, ¿qué cambia? | Sólo el adaptador de salida (entity, repository, mapper, adapter). Dominio, casos de uso y controller no cambian. |
| ¿Qué constraint respeta el caso de uso? | `uq_persona_correo`: `PersonaService.registrar` consulta `existePorCorreoElectronico` antes de guardar y lanza `CorreoPersonaDuplicadoException`. |

## Evidencia

- `PersonaServiceTest`: prueba la regla de correo duplicado reemplazando el Port OUT por una implementación en memoria, **sin Spring ni PostgreSQL**. Es la ventaja concreta de la arquitectura hexagonal.
- `PersonaControllerTest`: el controller se prueba simulando los Port IN.

| Prueba | Resultado |
|---|---|
| POST válido | 201 Created y fila real en PostgreSQL |
| GET id existente | 200 OK |
| GET id inexistente | 404 Not Found |
| POST con validación rota | 400 Bad Request |
| POST con correo duplicado | `CorreoPersonaDuplicadoException` (pasa a 409 con el handler del Capítulo 08) |
