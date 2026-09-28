# Capítulo 02 - Java 21

Código: [`condoflow-backend-lab/`](../../condoflow-backend-lab).

## Estructura

```text
com.condoflow
├── Main.java
├── person
│   ├── domain
│   │   ├── Persona.java
│   │   ├── exception
│   │   │   ├── CorreoPersonaDuplicadoException.java
│   │   │   └── PersonaNoEncontradaException.java
│   │   └── port
│   │       └── PersonaRepository.java
│   ├── application
│   │   └── PersonaService.java
│   └── infrastructure
│       └── memory
│           └── PersonaRepositoryEnMemoria.java
└── residence
    ├── domain
    │   ├── Residencia.java
    │   ├── TipoResidencia.java
    │   └── EstadoResidencia.java
    └── application
        └── command
            └── RegistrarResidenciaCommand.java
```

## Entidad padre

`Persona` — PK `personaId`, campo UNIQUE `correoElectronico` (`uq_persona_correo` en PostgreSQL).

## Entidad dependiente

`Residencia` — PK `residenciaId`, FK `personaId`.

## Relación

1:N porque una persona puede registrar varias residencias (por ejemplo, fue propietaria de una unidad y luego inquilina de otra), pero cada residencia pertenece a una sola persona.

## Contrato del repositorio

`PersonaRepository` (en `domain.port`) declara `guardar`, `buscarPorId`, `listarTodos` y `existePorCorreoElectronico`. No menciona PostgreSQL ni JPA: define **qué** necesita la aplicación, no **cómo** se guarda.

`PersonaService` depende de la **interface**, nunca de `PersonaRepositoryEnMemoria`. Cuando pasemos a PostgreSQL sólo cambiará la implementación.

## Colección elegida

Usamos `Map<Long, Persona>` (`LinkedHashMap`) porque la búsqueda principal es por id (clave → valor) y conserva el orden de inserción al listar.

## Optional

`buscarPorId` devuelve `Optional<Persona>` porque la búsqueda puede no encontrar nada. El servicio lo convierte en `PersonaNoEncontradaException` con `orElseThrow`.

## Excepciones propias

- `PersonaNoEncontradaException`: se busca un id que no existe.
- `CorreoPersonaDuplicadoException`: se intenta registrar un correo ya usado (ignora mayúsculas/minúsculas).

## Enum

- `TipoResidencia` (`PROPIETARIO`, `INQUILINO`): sale de la ficha oficial PA-04 ("propietarios/inquilinos") y de la columna `tipo_residencia`.
- `EstadoResidencia` (`VIGENTE`, `FINALIZADA`): sale de RN-01 (vigencia temporal) y RF-14 (registrar residencia vigente).

## Record

`RegistrarResidenciaCommand(personaId, unidadId, tipoResidencia, fechaInicio)`: contiene sólo los datos que necesita la operación. No lleva id (lo asigna el sistema) ni estado (toda residencia nueva nace `VIGENTE`).

## Relación con PostgreSQL

| PostgreSQL | Java en este capítulo |
|---|---|
| PRIMARY KEY | id del objeto / clave del `Map` |
| UNIQUE `correo_electronico` | `existePorCorreoElectronico` antes de guardar |
| FOREIGN KEY `persona_id` | `personaId` dentro de `Residencia` |
| CHECK de estado | enum `EstadoResidencia` |
| NOT NULL | validación en el constructor |

## Evidencia

Salida de `Main`:

```text
Cantidad de personas: 2
Persona encontrada: Juan
ERROR CONTROLADO: No existe la persona con id: 999
ERROR CONTROLADO: Ya existe una persona con el correo: JUAN.PEREZ@gmail.com
Residencias de Juan: 2
  - unidad 10 | PROPIETARIO | FINALIZADA
  - unidad 11 | INQUILINO | VIGENTE
ERROR CONTROLADO: La fecha de fin no puede ser anterior a la fecha de inicio
```
