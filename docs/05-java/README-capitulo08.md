# Capítulo 08 - Errores profesionales, validaciones y transacciones

## Traducción del ejemplo del profesor

| Profesor | CondoFlow |
|---|---|
| Cliente | `Persona` (entidad padre) |
| Vehiculo | `Residencia` (entidad dependiente) |
| clienteId | `personaId` (FK `residencia.persona_id`) |
| placa UNIQUE | Una persona sólo puede tener una residencia VIGENTE por unidad (`uq_residencia_vigente_persona_unidad`) |
| ClienteNoEncontradoException | `PersonaNoEncontradaException` |
| PlacaDuplicadaException | `ResidenciaVigenteDuplicadaException` |

## Completado antes de programar

| Elemento | Respuesta del equipo |
|---|---|
| Entidad padre | Persona |
| Entidad dependiente | Residencia |
| FK | `residencia.persona_id` |
| Campo UNIQUE o conflicto | Residencia VIGENTE duplicada para la misma persona y unidad |
| Regla de validación de formato | `tipoResidencia` ∈ {PROPIETARIO, INQUILINO}; `personaId`, `unidadId` y `fechaInicio` obligatorios |
| Regla de negocio | La persona y la unidad deben existir; no se registra una segunda residencia VIGENTE en la misma unidad |

## Packages agregados

```text
com.condoflow
├── shared
│   └── web
│       ├── ApiError.java
│       └── GlobalExceptionHandler.java
├── person
│   └── domain
│       └── exception        PersonaNoEncontradaException, CorreoPersonaDuplicadoException
└── residence
    ├── domain
    │   └── exception        ResidenciaNoEncontradaException, ResidenciaVigenteDuplicadaException
    └── application
        └── command          RegistrarResidenciaCommand
```

## ApiError

Se usa la estructura exacta de la guía: `timestamp`, `status`, `error`, `message`, `path`, `fieldErrors`.

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
| `CorreoPersonaDuplicadoException`, `ResidenciaVigenteDuplicadaException` | 409 | Conflicto con una regla de negocio |
| `MethodArgumentNotValidException` | 400 | Falla `@Valid` del DTO; `fieldErrors` indica el campo |
| `HttpMessageNotReadableException` | 400 | JSON mal formado o valor fuera del enum (prueba obligatoria del Capítulo 07) |
| `MethodArgumentTypeMismatchException` | 400 | Id con formato inválido, por ejemplo `/api/personas/abc` |
| `IllegalArgumentException` | 400 | El constructor del dominio rechazó un dato |
| `DataIntegrityViolationException` | 409 | PostgreSQL rechazó un UNIQUE/FK/CHECK; no se devuelve el mensaje interno de la base |
| `Exception` | 500 | Error no previsto: se registra en el log y el cliente recibe un mensaje genérico |

## Clasificación de validaciones

| Regla del proyecto | Tipo | Lugar |
|---|---|---|
| `personaId`, `unidadId`, `fechaInicio` obligatorios | Formato | Request DTO (`@NotNull`) |
| Formato de correo, documento y teléfono de persona | Formato | Request DTO (`@Email`, `@Pattern`) |
| Correo de persona duplicado | Negocio | `PersonaService` |
| Residencia VIGENTE duplicada | Negocio | `ResidenciaService` |
| Persona o unidad inexistente | Negocio/existencia | `ResidenciaService` (vía `ConsultarPersonaUseCase` y `ConsultarUnidadUseCase`) |
| FK / UNIQUE / CHECK | Integridad | PostgreSQL (V1 y V3) |

## @Transactional

```java
@Override
@Transactional
public Residencia registrar(RegistrarResidenciaCommand command) {
    // 1. validar existencia del padre
    // 2. validar conflicto de negocio
    // 3. construir dominio
    // 4. guardar por Port OUT
}
```

`ResidenciaService.registrar` es el límite transaccional porque las verificaciones (persona, unidad, residencia vigente) y el INSERT forman una sola operación de negocio: si cualquier paso falla, no queda nada a medias. `PersonaService.registrar` también es transaccional (verificación de correo + INSERT). Las consultas usan `@Transactional(readOnly = true)`.

## Controller limpio

```java
@PostMapping
public ResponseEntity<ResidenciaResponse> registrar(@Valid @RequestBody CrearResidenciaRequest request) {
    Residencia creada = registrarUseCase.registrar(ResidenciaWebMapper.toCommand(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(ResidenciaWebMapper.toResponse(creada));
}
```

Sin `try/catch`: los errores los traduce `GlobalExceptionHandler`.

## Pruebas mínimas obligatorias

| Caso | Resultado esperado | Evidencia |
|---|---|---|
| Registro válido | 201 Created | `{"residenciaId":3,"personaId":3,"unidadId":1,"tipoResidencia":"INQUILINO",...,"estado":"VIGENTE"}` |
| Campo inválido | 400 Bad Request | `fieldErrors: {personaId: "La persona es obligatoria", fechaInicio: "La fecha de inicio es obligatoria"}` |
| Padre inexistente | 404 Not Found | `"No existe una persona con id 999"` |
| Duplicado o conflicto | 409 Conflict | `"La persona 3 ya tiene una residencia vigente en la unidad 1"` |
| Error no controlado simulado | 500 Internal Server Error | `"Ocurrió un error interno. Intente nuevamente más tarde."` (`ResidenciaControllerTest`) |

## Consulta en DataGrip: estado final de los datos

```sql
SELECT residencia_id, persona_id, unidad_id, tipo_residencia, fecha_inicio, fecha_fin, estado
FROM condoflow.residencia
ORDER BY residencia_id;
```

```text
 residencia_id | persona_id | unidad_id | tipo_residencia | fecha_inicio | fecha_fin | estado
---------------+------------+-----------+-----------------+--------------+-----------+---------
             1 |          1 |         1 | PROPIETARIO     | 2026-09-28   |           | VIGENTE
             2 |          2 |         2 | PROPIETARIO     | 2026-09-28   |           | VIGENTE
             3 |          3 |         1 | INQUILINO       | 2026-09-01   |           | VIGENTE
```

Después de los casos 400, 404 y 409 no quedó ninguna fila parcial: la base sigue consistente.

## Respuestas de defensa

1. **¿Por qué no usaste try/catch en el controller?** Porque `@RestControllerAdvice` centraliza la traducción de excepciones a HTTP; así cada controller sólo recibe, delega y responde.
2. **¿Qué error produce 409 y por qué?** Registrar una segunda residencia VIGENTE para la misma persona y unidad: la petición es válida pero choca con el estado actual de los datos.
3. **¿Qué validación pertenece al DTO y cuál al negocio?** Campos obligatorios y formatos van en el DTO; la existencia de la persona y el duplicado van en el caso de uso.
4. **¿Qué hace `@RestControllerAdvice`?** Intercepta las excepciones de todos los controllers y construye un `ApiError` uniforme.
5. **¿Por qué no devolver mensajes internos de PostgreSQL?** Exponen detalles de la base (tablas, constraints) y no son útiles para el usuario.
6. **¿Qué protege `@Transactional`?** Que la verificación y el INSERT se confirmen juntos o no se confirme nada.
7. **¿Dónde está el Port OUT y por qué el controller no lo conoce?** `ResidenciaRepositoryPort` en `domain/port/out`; el controller sólo conoce los Port IN, así no depende de la persistencia.
