# Capítulo 04 - Spring MVC: endpoints, DTOs y validación

Entidad padre trabajada: **Persona** (`/api/personas`).

## Traducción profesor → proyecto

| Profesor — ParkFlow | CondoFlow |
|---|---|
| `parkflow-backend` | `condoflow-backend` |
| `com.parkflow` | `com.condoflow` |
| Cliente | Persona |
| Vehiculo | Residencia |
| `/api/clientes` | `/api/personas` |
| `CrearClienteRequest` | `CrearPersonaRequest` |
| `ClienteResponse` | `PersonaResponse` |

## Estructura de paquetes (al terminar el capítulo)

```text
com.condoflow.person
├── domain
│   └── Persona.java
├── application
│   └── PersonaService.java                  ← temporal, en memoria
└── infrastructure/adapter/in/web
    ├── PersonaController.java
    └── dto
        ├── CrearPersonaRequest.java
        └── PersonaResponse.java
```

En los capítulos siguientes esta estructura evoluciona: `Persona` pasa a `domain/model` (Cap. 05), el servicio pasa a `application/service` y deja de usar memoria (Cap. 05–06), y se agrega `web/mapper`.

## Request DTO y validaciones

| Campo | Validación | Regla que protege |
|---|---|---|
| `nombre` | `@NotBlank`, `@Size(max=100)` | NOT NULL y longitud de `persona.nombre` |
| `apellido` | `@NotBlank`, `@Size(max=100)` | NOT NULL y longitud de `persona.apellido` |
| `documento` | `@NotBlank`, `@Pattern` 5–50 letras, números o guiones | Documento de identidad legible |
| `telefono` | `@NotBlank`, `@Pattern` 7–30 dígitos, `+` opcional | Teléfono de contacto válido |
| `correoElectronico` | `@NotBlank`, `@Email`, `@Size(max=150)` | Formato y longitud del correo, que es UNIQUE |

**¿Por qué el Request y el Response son distintos?** `personaId` y `estado` los decide el sistema: salen en la respuesta pero no se aceptan en la entrada. El cliente no puede elegir su id ni crear una persona ya inactiva.

## Contrato HTTP

| Verbo | Ruta | Entrada | Salida | Status |
|---|---|---|---|---|
| POST | `/api/personas` | JSON `CrearPersonaRequest` | `PersonaResponse` | 201 / 400 |
| GET | `/api/personas` | Query param opcional `filtro` | Lista de `PersonaResponse` | 200 |
| GET | `/api/personas/{id}` | Path variable `id` | `PersonaResponse` | 200 / 404 |

`filtro` busca por nombre completo o correo, sin distinguir mayúsculas.

## Anotaciones

| Anotación | Explicación |
|---|---|
| `@RestController` | La clase atiende peticiones HTTP y lo que devuelve se convierte en JSON. |
| `@RequestMapping` | Define la ruta base del recurso: `/api/personas`. |
| `@PostMapping` | Asocia el método al verbo POST (crear). |
| `@GetMapping` | Asocia el método al verbo GET (consultar). |
| `@RequestBody` | Convierte el JSON del cuerpo de la petición en `CrearPersonaRequest`. |
| `@Valid` | Ejecuta las validaciones del DTO antes de entrar al método; si fallan, responde 400. |
| `@PathVariable` | Toma un valor que es parte de la ruta: `/api/personas/{id}`. |
| `@RequestParam` | Toma un valor opcional de la query: `/api/personas?filtro=lopez`. |

## Pruebas mínimas obligatorias

`PersonaControllerTest` (MockMvc):

| Prueba | Resultado esperado |
|---|---|
| POST válido | 201 Created + JSON del recurso |
| POST con campo obligatorio vacío | 400 Bad Request |
| POST con formato inválido | 400 Bad Request |
| GET lista | 200 OK + arreglo JSON |
| GET por id existente | 200 OK |
| GET por id inexistente | 404 Not Found |
| GET con parámetro opcional `filtro` | Filtra la consulta |

Peticiones manuales en [`backend/requests.http`](../../backend/requests.http).

## Respuestas de defensa

- **¿Por qué la entidad padre es Persona?** Porque la FK `residencia.persona_id` está en la tabla dependiente: una persona puede tener muchas residencias.
- **¿Por qué `/api/personas`?** Es el recurso en plural y en el lenguaje del dominio.
- **`@PathVariable` vs `@RequestParam`:** el primero identifica el recurso (`/{id}`); el segundo es un criterio opcional (`?filtro=`).
- **¿Por qué POST responde 201?** Porque se creó un recurso nuevo.
- **¿Qué pasa con los datos si reinicio la aplicación?** En este capítulo viven en una lista en memoria y se pierden. En el Capítulo 05 pasan a PostgreSQL.
- **¿Dónde está el adaptador de entrada?** `PersonaController`, en `infrastructure/adapter/in/web`.
