# Catálogo de API v1 — CondoFlow

- Base: `http://localhost:8080`
- Documentación interactiva: `http://localhost:8080/swagger-ui.html` (OpenAPI en `/v3/api-docs`)
- Colección de Postman: [`CondoFlow.postman_collection.json`](CondoFlow.postman_collection.json). En Postman: *Import* → elegir el archivo → *Run collection*. Tiene 33 peticiones y cada una verifica su código HTTP esperado. Al final elimina la residencia y la persona que creó, así que se puede ejecutar varias veces.
- Pruebas manuales en IntelliJ: [`backend/requests.http`](../../backend/requests.http)
- Formato: JSON. Fechas en ISO `AAAA-MM-DD`.
- CORS: el navegador sólo puede leer las respuestas de `/api/**` desde el origen del frontend en desarrollo, `http://localhost:5173` (`shared/web/WebConfig`, Guía 05 del frontend). Otro origen recibe `403 Invalid CORS request`. Postman no aplica CORS porque no es un navegador.

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
| PUT | `/api/personas/{id}` | `ActualizarPersonaRequest` | `PersonaResponse` | 200 / 400 / 404 / 409 |
| DELETE | `/api/personas/{id}` | — | sin cuerpo | 204 / 404 / 409 |

`CrearPersonaRequest`

| Campo | Tipo | Reglas |
|---|---|---|
| `nombre` | string | obligatorio, máx. 100 |
| `apellido` | string | obligatorio, máx. 100 |
| `documento` | string | obligatorio, 5–50 letras, números o guiones |
| `telefono` | string | obligatorio, 7–30 dígitos, `+` opcional |
| `correoElectronico` | string | obligatorio, formato email, máx. 150, único (409 si se repite) |

`ActualizarPersonaRequest`: los mismos campos y reglas de `CrearPersonaRequest` más `estado` (`ACTIVO` \| `INACTIVO`, obligatorio). Es la representación completa de la persona (PUT idempotente); el id va en la URL.

Errores de negocio:

- **404** si la persona del PUT o del DELETE no existe.
- **409** en el PUT si el correo pertenece a **otra** persona (puede conservar el suyo).
- **409** en el DELETE si la persona tiene residencias o reservas (FK `fk_residencia_persona` / `fk_reserva_persona`). Para darla de baja sin perder el historial se usa el PUT con `estado: "INACTIVO"` (baja lógica).

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
| GET | `/api/residencias` | — | `ResidenciaResponse[]` ordenadas por id | 200 |
| GET | `/api/residencias/{id}` | — | `ResidenciaResponse` | 200 / 404 |
| GET | `/api/residencias/persona/{personaId}` | — | `ResidenciaResponse[]` (más recientes primero) | 200 / 404 |
| PUT | `/api/residencias/{id}` | `ActualizarResidenciaRequest` | `ResidenciaResponse` | 200 / 400 / 404 / 409 |
| DELETE | `/api/residencias/{id}` | — | sin cuerpo | 204 / 404 |

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

`ActualizarResidenciaRequest` (representación completa; el id va en la URL)

| Campo | Tipo | Reglas |
|---|---|---|
| `personaId` | number | obligatorio; puede ser **otra persona** (reasigna la residencia); debe existir (404) |
| `unidadId` | number | obligatorio; debe existir (404) |
| `tipoResidencia` | `PROPIETARIO` \| `INQUILINO` | obligatorio |
| `fechaInicio` | date | obligatoria |
| `fechaFin` | date \| null | `null` si está `VIGENTE`; obligatoria si está `FINALIZADA` y no anterior a `fechaInicio` (400) |
| `estado` | `VIGENTE` \| `FINALIZADA` | obligatorio |

Errores de negocio del PUT y el DELETE:

- **400** si el estado y la fecha de fin no son coherentes (regla RN-01 del dominio, igual que el CHECK `ck_residencia_estado_fecha_fin`).
- **404** si la residencia, la persona o la unidad no existen.
- **409** si el cambio deja a la persona con dos residencias `VIGENTE` en la misma unidad.
- El DELETE no tiene conflicto de FK: ninguna tabla referencia a `residencia`.

`ResidenciaResponse`: `residenciaId`, `personaId`, `unidadId`, `tipoResidencia`, `fechaInicio`, `fechaFin` (`null` si está vigente), `estado` (`VIGENTE` \| `FINALIZADA`).
