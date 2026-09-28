# Capítulo 08 - Errores profesionales, validaciones y transacciones

## Traducción al proyecto

| Profesor | CondoFlow |
|---|---|
| Cliente | `Persona` |
| Vehiculo | `Residencia` |
| clienteId | `personaId` (FK `residencia.persona_id`) |
| placa UNIQUE | Residencia VIGENTE única por persona y unidad; correo de persona UNIQUE |
| ClienteNoEncontradoException | `PersonaNoEncontradaException` (también `UnidadNoEncontradaException`, `ResidenciaNoEncontradaException`) |
| PlacaDuplicadaException | `ResidenciaVigenteDuplicadaException`, `CorreoPersonaDuplicadoException` |

| Elemento | Respuesta del equipo |
|---|---|
| Entidad padre | Persona |
| Entidad dependiente | Residencia |
| FK | `residencia.persona_id` |
| Campo UNIQUE o conflicto | Una persona sólo puede tener una residencia VIGENTE por unidad |
| Regla de validación de formato | `tipoResidencia` ∈ {PROPIETARIO, INQUILINO}; campos obligatorios; formato de correo |
| Regla de negocio | La persona y la unidad deben existir; no duplicar residencia vigente; sólo se finaliza una residencia VIGENTE con `fechaFin >= fechaInicio` |

## Contrato único de error

[`ApiError`](../../backend/src/main/java/com/condoflow/shared/web/ApiError.java):

```json
{
  "timestamp": "2026-09-28T15:53:17.297Z",
  "status": 404,
  "error": "Not Found",
  "message": "No existe una persona con id 999",
  "path": "/api/residencias",
  "fieldErrors": {}
}
```

## GlobalExceptionHandler

| Excepción | Status | Motivo |
|---|---|---|
| `PersonaNoEncontradaException`, `UnidadNoEncontradaException`, `ResidenciaNoEncontradaException` | 404 | El recurso o un padre referenciado no existe |
| `CorreoPersonaDuplicadoException`, `ResidenciaVigenteDuplicadaException`, `ResidenciaYaFinalizadaException` | 409 | Conflicto con el estado actual de los datos |
| `MethodArgumentNotValidException` | 400 | Falla `@Valid` del DTO; incluye `fieldErrors` por campo |
| `HttpMessageNotReadableException` | 400 | JSON mal formado o valor fuera del enum |
| `MethodArgumentTypeMismatchException` | 400 | `/api/personas/abc` |
| `IllegalArgumentException` | 400 | Invariante del dominio (fecha de fin anterior a la de inicio) |
| `DataIntegrityViolationException` | 409 | Última línea de defensa si PostgreSQL rechaza un UNIQUE/FK/CHECK (por ejemplo, dos peticiones simultáneas). No se devuelve el mensaje interno de la base. |
| `Exception` | 500 | Error no previsto: se registra en el log y el cliente recibe un mensaje genérico |

## Dónde vive cada validación

| Regla | Tipo | Lugar |
|---|---|---|
| Campo obligatorio (`personaId`, `fechaInicio`...) | Formato | Request DTO (`@NotNull`, `@NotBlank`) |
| Formato de correo, documento, teléfono | Formato | Request DTO (`@Email`, `@Pattern`) |
| `tipoResidencia` válido | Formato | Tipo enum en el DTO |
| Correo duplicado | Negocio | `PersonaService` |
| Residencia vigente duplicada | Negocio | `ResidenciaService` |
| Persona o unidad inexistente | Negocio/existencia | `ResidenciaService` (vía `ConsultarPersonaUseCase` / `ConsultarUnidadUseCase`) |
| Fecha de fin coherente, no finalizar dos veces | Invariante del dominio | `Residencia.finalizar()` |
| FK / UNIQUE / CHECK | Integridad | PostgreSQL (V1, V3) |

## @Transactional

- `ResidenciaService.registrar`: verifica persona, unidad y residencia vigente, y luego inserta. Es el **límite transaccional** porque todo eso forma una sola operación de negocio: si falla cualquier paso no queda nada a medias.
- `ResidenciaService.finalizar`: lee, cambia el estado y guarda (*read-modify-write*); debe ser atómico.
- `PersonaService.registrar`: verificación del correo + INSERT.
- Las consultas usan `@Transactional(readOnly = true)`.
- `@Transactional` se pone en la capa de aplicación (el caso de uso), no en el controller ni en el repositorio.

## Controller limpio

```java
@PostMapping
public ResponseEntity<ResidenciaResponse> registrar(@Valid @RequestBody CrearResidenciaRequest request) {
    Residencia creada = registrar.registrar(ResidenciaWebMapper.toCommand(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(ResidenciaWebMapper.toResponse(creada));
}
```

Sin `try/catch`. El request se convierte en `RegistrarResidenciaCommand`, que vive junto al Port IN.

## Nuevo endpoint: finalizar residencia (RN-01)

`PATCH /api/residencias/{id}/finalizar` con `{"fechaFin": "2026-12-31"}` → 200 / 400 / 404 / 409.

## Evidencia (backend real + PostgreSQL)

| Caso | Resultado |
|---|---|
| Registro válido | `201` `{"residenciaId":4,...,"estado":"VIGENTE"}` |
| Campo inválido | `400` `fieldErrors: {personaId: "La persona es obligatoria", fechaInicio: "La fecha de inicio es obligatoria"}` |
| Padre inexistente | `404` `"No existe una persona con id 999"` |
| Duplicado o conflicto | `409` `"La persona 2 ya tiene una residencia vigente en la unidad 1"` |
| Error no controlado simulado | `500` `"Ocurrió un error interno..."` (`ResidenciaControllerTest`) |

Estado final en PostgreSQL después de los errores (no quedaron filas parciales):

```text
 residencia_id | persona_id | unidad_id | fecha_inicio | fecha_fin  |   estado
---------------+------------+-----------+--------------+------------+------------
             1 |          1 |         1 | 2026-09-28   |            | VIGENTE
             2 |          2 |         2 | 2026-09-28   |            | VIGENTE
             3 |          3 |         1 | 2026-09-01   |            | VIGENTE
             4 |          2 |         1 | 2026-09-10   | 2026-12-31 | FINALIZADA
```

Pruebas automáticas: `ResidenciaControllerTest` (9), `ResidenciaServiceTest` (8), `PersonaControllerTest` (9), `PersonaServiceTest` (3).
