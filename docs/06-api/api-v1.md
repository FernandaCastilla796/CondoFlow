# Catálogo de API v1 — CondoFlow

- Base: `http://localhost:8080`
- Documentación interactiva: `http://localhost:8080/swagger-ui.html` (OpenAPI en `/v3/api-docs`)
- Colección de Postman: [`CondoFlow.postman_collection.json`](CondoFlow.postman_collection.json). En Postman: *Import* → elegir el archivo → *Run collection*. Tiene 20 peticiones y cada una verifica su código HTTP esperado.
- Pruebas manuales en IntelliJ: [`backend/requests.http`](../../backend/requests.http)
- Formato: JSON. Fechas en ISO `AAAA-MM-DD`.

## Formato de error (todas las respuestas 4xx y 5xx)

```json
{
  "timestamp": "2026-09-28T15:53:17.253Z",
  "status": 400,
  "error": "Bad Request",
  "message": "La solicitud tiene campos inválidos",
  "path": "/api/residencias",
  "fieldErrors": { "personaId": "La persona es obligatoria" }
}
```

| Status | Cuándo |
|---|---|
| 400 | Falla una validación del DTO, JSON mal formado, valor fuera de un enum, parámetro con formato inválido o invariante del dominio |
| 404 | El recurso o un padre referenciado no existe |
| 409 | Conflicto con una regla de negocio o restricción de integridad |
| 500 | Error no previsto (mensaje genérico; el detalle queda en el log) |

---

## Health

| Método | Ruta | Respuesta |
|---|---|---|
| GET | `/api/health` | `200` `{ "status": "OK", "application": "condoflow-backend", "stage": "...", "timestamp": "..." }` |
| GET | `/api/personas/demo` | `200` persona de ejemplo fija (endpoint demo del Capítulo 03) |

## Personas — `/api/personas`

| Método | Ruta | Entrada | Salida | Status |
|---|---|---|---|---|
| POST | `/api/personas` | `CrearPersonaRequest` | `PersonaResponse` | 201 / 400 / 409 |
| GET | `/api/personas?filtro=texto` | `filtro` opcional (nombre completo o correo) | `PersonaResponse[]` | 200 |
| GET | `/api/personas/{id}` | — | `PersonaResponse` | 200 / 400 / 404 |

`CrearPersonaRequest`

| Campo | Tipo | Reglas |
|---|---|---|
| `nombre` | string | obligatorio, máx. 100 |
| `apellido` | string | obligatorio, máx. 100 |
| `documento` | string | obligatorio, 5–50 letras, números o guiones |
| `telefono` | string | obligatorio, 7–30 dígitos, `+` opcional |
| `correoElectronico` | string | obligatorio, formato email, máx. 150, único (409 si se repite) |

`PersonaResponse`: `personaId`, `nombre`, `apellido`, `documento`, `telefono`, `correoElectronico` (en minúsculas), `estado` (`ACTIVO` \| `INACTIVO`).

## Unidades — `/api/unidades`

| Método | Ruta | Salida | Status |
|---|---|---|---|
| GET | `/api/unidades` | `UnidadResponse[]` ordenadas por número | 200 |
| GET | `/api/unidades/{id}` | `UnidadResponse` | 200 / 404 |

`UnidadResponse`: `unidadId`, `numeroUnidad`, `tipo`, `estado` (`ACTIVA` \| `INACTIVA`).

## Residencias — `/api/residencias`

| Método | Ruta | Entrada | Salida | Status |
|---|---|---|---|---|
| POST | `/api/residencias` | `CrearResidenciaRequest` | `ResidenciaResponse` | 201 / 400 / 404 / 409 |
| GET | `/api/residencias/{id}` | — | `ResidenciaResponse` | 200 / 404 |
| GET | `/api/residencias/persona/{personaId}` | — | `ResidenciaResponse[]` (más recientes primero) | 200 / 404 |

`CrearResidenciaRequest`

| Campo | Tipo | Reglas |
|---|---|---|
| `personaId` | number | obligatorio, positivo, debe existir (404) |
| `unidadId` | number | obligatorio, positivo, debe existir (404) |
| `tipoResidencia` | `PROPIETARIO` \| `INQUILINO` | obligatorio; otro valor → 400 |
| `fechaInicio` | date | obligatoria |

Errores de negocio:

- **404** si la persona o la unidad no existen.
- **409** si la persona ya tiene una residencia `VIGENTE` en esa unidad.

`ResidenciaResponse`: `residenciaId`, `personaId`, `unidadId`, `tipoResidencia`, `fechaInicio`, `fechaFin` (`null` si está vigente), `estado` (`VIGENTE` \| `FINALIZADA`).
