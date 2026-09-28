# Capítulo 04 - Spring MVC: endpoints, DTOs y validación

Entidad padre trabajada: **Persona** (`/api/personas`).

## Estructura

```text
com.condoflow.person
├── domain/model
│   ├── Persona.java
│   └── EstadoPersona.java
├── application
│   └── PersonaService.java                  ← temporal, en memoria
└── infrastructure/adapter/in/web
    ├── PersonaController.java               ← adaptador de entrada
    ├── dto
    │   ├── CrearPersonaRequest.java
    │   └── PersonaResponse.java
    └── mapper
        └── PersonaWebMapper.java
```

## Request DTO y validaciones

| Campo | Validación | Regla que protege |
|---|---|---|
| `nombre` | `@NotBlank`, `@Size(max=100)` | NOT NULL y longitud de `persona.nombre` |
| `apellido` | `@NotBlank`, `@Size(max=100)` | NOT NULL y longitud de `persona.apellido` |
| `documento` | `@NotBlank`, `@Pattern` 5–50 letras, números o guiones | Documento de identidad legible |
| `telefono` | `@NotBlank`, `@Pattern` 7–30 dígitos, `+` opcional | Teléfono de contacto válido |
| `correoElectronico` | `@NotBlank`, `@Email`, `@Size(max=150)` | Formato y longitud de la clave natural UNIQUE |

**¿Por qué Request y Response son distintos?** `personaId` y `estado` los decide el sistema: salen en la respuesta pero no se aceptan en la entrada. Así el cliente no puede elegir su propio id ni crear una persona ya inactiva.

## Contrato HTTP

| Verbo | Ruta | Entrada | Salida | Status |
|---|---|---|---|---|
| POST | `/api/personas` | JSON `CrearPersonaRequest` (`@RequestBody`) | `PersonaResponse` | 201 / 400 |
| GET | `/api/personas` | Query param opcional `buscar` (`@RequestParam`) | Lista de `PersonaResponse` | 200 |
| GET | `/api/personas/{id}` | Path variable `id` (`@PathVariable`) | `PersonaResponse` | 200 / 404 |

`buscar` filtra por nombre completo o correo, sin distinguir mayúsculas.

## Anotaciones

| Anotación | Qué hace |
|---|---|
| `@RestController` | La clase atiende HTTP y sus retornos se convierten a JSON. |
| `@RequestMapping` | Ruta base del recurso: `/api/personas`. |
| `@PostMapping` / `@GetMapping` | Asocian cada método a un verbo HTTP. |
| `@RequestBody` | Convierte el JSON del cuerpo en `CrearPersonaRequest`. |
| `@Valid` | Ejecuta las validaciones del DTO antes de entrar al método; si fallan, responde 400. |
| `@PathVariable` | Toma un valor de la ruta: `/api/personas/{id}`. |
| `@RequestParam` | Toma un valor opcional de la query: `?buscar=lopez`. |

## Evidencia

`PersonaControllerTest` (MockMvc) cubre las 7 pruebas obligatorias de la guía:

| Prueba | Resultado |
|---|---|
| POST válido | 201 + JSON con `personaId`, correo normalizado y `estado=ACTIVO` |
| POST con nombre vacío | 400 |
| POST con correo inválido | 400 |
| GET lista | 200 + arreglo |
| GET por id existente | 200 |
| GET por id inexistente | 404 |
| GET con `buscar` | Devuelve sólo las coincidencias |

Peticiones manuales en [`backend/requests.http`](../../backend/requests.http).

**¿Qué pasa si reinicio la aplicación?** En este capítulo los datos viven en una lista en memoria y se pierden. En el Capítulo 05 pasan a PostgreSQL.
