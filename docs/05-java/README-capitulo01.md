# Capítulo 01 - Java esencial

Código: [`condoflow-backend-lab/`](../../condoflow-backend-lab) (proyecto `condoflow-backend-lab`, package base `com.condoflow`).

## Entidades elegidas

- Tabla padre: `persona`
- Tabla dependiente: `residencia`
- Relación: 1:N (una persona puede tener varias residencias a lo largo del tiempo)
- PK de persona: `persona_id`
- PK de residencia: `residencia_id`
- FK: `residencia.persona_id → persona.persona_id`

## Clases Java

| Clase | Package | Qué representa |
|---|---|---|
| `Persona` | `person.domain` | Entidad padre |
| `Residencia` | `residence.domain` | Entidad dependiente |
| `TipoResidencia` | `residence.domain` | Enum: `PROPIETARIO`, `INQUILINO` |
| `EstadoResidencia` | `residence.domain` | Enum: `VIGENTE`, `FINALIZADA` |
| `PersonaRepository` | `person.domain.port` | Interface de contrato (qué necesitamos, no cómo) |
| `Main` | `com.condoflow` | Programa de prueba sin Spring |

## Relación 1:N en Java

`Persona` mantiene una colección privada:

```java
private final List<Residencia> residencias = new ArrayList<>();
```

Sólo se modifica mediante el método de negocio `agregarResidencia()`, y `getResidencias()` devuelve una lista no modificable.

En PostgreSQL la relación vive en la FK `residencia.persona_id`; en Java se expresa con objetos y colecciones. No son representaciones idénticas.

## Reglas implementadas

- `Residencia` no puede crearse sin persona, unidad, tipo ni fecha de inicio (equivalente a `NOT NULL`).
- RN-01 (vigencia temporal): `finalizar(fechaFin)` rechaza una fecha de fin anterior a la de inicio y no permite finalizar dos veces.
- `agregarResidencia()` rechaza `null`.

## Decisiones

- **¿Por qué enum?** `tipo_residencia` y `estado` son conjuntos cerrados de valores; un enum impide valores inválidos como `"propietarioo"` que un `String` libre sí aceptaría.
- **¿Por qué la colección es privada?** Para que nadie agregue o borre residencias sin pasar por las reglas de `Persona`.
- **¿Qué NO implementamos todavía?** Spring Boot, JPA y PostgreSQL desde Java.

## Prueba

Ejecutar `Main` (ver salida en [README-capitulo02.md](README-capitulo02.md#evidencia)).
