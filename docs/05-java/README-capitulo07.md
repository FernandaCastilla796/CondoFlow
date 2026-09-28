# Capítulo 07 - Relación 1:N entre módulos (Persona → Residencia)

## Ficha

| Dato | ParkFlow (profesor) | CondoFlow |
|---|---|---|
| Entidad padre | Cliente | `Persona` |
| PK padre | `cliente_id` | `persona_id` |
| Entidad dependiente | Vehiculo | `Residencia` |
| PK dependiente | `vehiculo_id` | `residencia_id` |
| FK dependiente | `cliente_id` | `persona_id` (`fk_residencia_persona`) |
| Campo UNIQUE | `placa` | Residencia VIGENTE por (`persona_id`, `unidad_id`): índice UNIQUE parcial `uq_residencia_vigente_persona_unidad` |
| CHECK/enum | `tipo` | `tipo_residencia` → `TipoResidencia`; `estado` → `EstadoResidencia` |
| Package base | `com.parkflow` | `com.condoflow` |

`residencia` tiene además una segunda FK hacia `unidad` (`fk_residencia_unidad`). Para validarla con el mismo patrón se agregó el módulo `unit` de consulta (`ConsultarUnidadUseCase`, `GET /api/unidades`).

## Relación física

```text
PERSONA (1) ────────────────< (N) RESIDENCIA >──────────────── (1) UNIDAD
persona_id [PK]                  residencia_id [PK]                  unidad_id [PK]
correo_electronico [UNIQUE]      persona_id    [FK → persona]        numero_unidad [UNIQUE]
                                 unidad_id     [FK → unidad]
                                 tipo_residencia [CHECK]
                                 fecha_inicio, fecha_fin [CHECK fin >= inicio]
                                 estado [CHECK]
                                 UNIQUE parcial (persona_id, unidad_id) WHERE estado = 'VIGENTE'
```

## Tres niveles

| Nivel | Implementación |
|---|---|
| Dominio | `Residencia` guarda `personaId` y `unidadId` (Long). Sin `@Entity` ni `@ManyToOne`. |
| JPA | `ResidenciaJpaEntity` con `@ManyToOne(fetch = LAZY) @JoinColumn(name = "persona_id")` y lo mismo para `unidad_id`. |
| PostgreSQL | `fk_residencia_persona` y `fk_residencia_unidad` siguen intactas. |

## Cómo se valida que el padre exista

```text
ResidenciaService
    +--> ConsultarPersonaUseCase   (Port IN del módulo person)
    +--> ConsultarUnidadUseCase    (Port IN del módulo unit)
    +--> ResidenciaRepositoryPort  (Port OUT propio)

ResidenciaService  X--> SpringDataPersonaRepository   ← prohibido
```

Reglas del caso de uso `registrar`:

1. La persona existe, si no → `PersonaNoEncontradaException`.
2. La unidad existe, si no → `UnidadNoEncontradaException`.
3. La persona no tiene ya una residencia VIGENTE en esa unidad, si no → `ResidenciaVigenteDuplicadaException`.

`ResidenciaPersistenceAdapter` usa `getReferenceById` de los repositorios de persona y unidad para armar la entidad JPA **sin** hacer SELECT extra. Esas referencias quedan dentro de infraestructura; el caso de uso no las ve.

## Endpoints

| Operación | Método | Ruta | Status |
|---|---|---|---|
| Registrar residencia | POST | `/api/residencias` | 201 / 400 / 404 / 409 |
| Consultar por id | GET | `/api/residencias/{id}` | 200 / 404 |
| Listar por persona | GET | `/api/residencias/persona/{personaId}` | 200 / 404 |
| Listar unidades | GET | `/api/unidades` | 200 |

## Pruebas obligatorias

| Prueba | Resultado |
|---|---|
| Registro válido con persona existente | 201, `estado = VIGENTE` |
| Consulta por id del registro creado | 200 |
| Listado de residencias de la misma persona | 200 con arreglo |
| Registro con persona inexistente | `PersonaNoEncontradaException` → 404 con el handler del Capítulo 08 |
| Duplicar la residencia VIGENTE | `ResidenciaVigenteDuplicadaException` → 409 con el handler del Capítulo 08 |
| `tipoResidencia = "ALQUILER"` (fuera del enum) | 400 |

`ResidenciaServiceTest` cubre las reglas con Mockito (sin base de datos).

## Comprobación SQL (JOIN)

```sql
SELECT r.residencia_id, r.persona_id AS fk_persona,
       p.nombre || ' ' || p.apellido AS persona,
       u.numero_unidad, r.tipo_residencia, r.estado
FROM condoflow.residencia r
JOIN condoflow.persona p ON p.persona_id = r.persona_id
JOIN condoflow.unidad  u ON u.unidad_id  = r.unidad_id
ORDER BY r.residencia_id;
```

```text
 residencia_id | fk_persona |   persona    | numero_unidad | tipo_residencia | estado
---------------+------------+--------------+---------------+-----------------+---------
             1 |          1 | Maria Lopez  | A-102         | PROPIETARIO     | VIGENTE
             2 |          2 | Carlos Rojas | B-201         | PROPIETARIO     | VIGENTE
             3 |          3 | Ana Suarez   | A-102         | INQUILINO       | VIGENTE
```

## Respuestas de defensa

- **¿Por qué `@ManyToOne` y no `@OneToMany`?** La FK vive en `residencia`: muchas filas de residencia apuntan a una persona. `@ManyToOne` mapea exactamente esa columna. No agregamos `@OneToMany` en `PersonaJpaEntity` porque no necesitamos cargar todas las residencias desde la persona.
- **¿Qué significa LAZY?** La persona asociada no se carga hasta que se accede a un campo distinto del id; evita consultas innecesarias (y el problema N+1 al listar).
- **¿Qué pasa si la base recibe una FK inexistente?** PostgreSQL la rechaza con `fk_residencia_persona`. El caso de uso lo detecta antes para devolver un 404 claro en lugar de un error de base de datos.
