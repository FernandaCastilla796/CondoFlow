# Capítulo 01 - Java esencial

## Entidades elegidas

* Tabla padre: `persona`
* Tabla dependiente: `residencia`
* Relación: `1:N`
* PK de persona: `persona_id`
* PK de residencia: `residencia_id`
* FK: `residencia.persona_id → persona.persona_id`

## Clases Java

* `Persona`
* `Residencia`
* `PersonaRepository`
* `Main`

## Estructura de packages

```text
com.condoflow
├── Main.java
├── person
│   ├── domain
│   │   ├── Persona.java
│   │   └── PersonaRepository.java
│   └── exception
└── residence
    ├── domain
    │   └── Residencia.java
    └── exception
```

## Relación 1:N

Una persona puede tener varias residencias.

En Java esta relación se representa mediante una colección privada:

```java
private final List<Residencia> residencias = new ArrayList<>();
```

La relación se administra mediante:

```java
agregarResidencia()
```

## Regla implementada

La clase `Residencia` valida que `residenciaId`, `personaId` y `unidadId` no sean `null`.

Además, en PostgreSQL existe una restricción de unicidad para el correo electrónico de `persona`.

## Decisiones

* Los atributos son `private` para proteger el estado interno de los objetos.
* La colección de residencias es privada para evitar modificaciones directas.
* `getResidencias()` devuelve una colección no modificable.
* La interfaz `PersonaRepository` expresa el contrato de guardar y buscar una persona.
* La interfaz todavía no implementa PostgreSQL ni `JpaRepository`.
* No se implementaron todavía Spring Boot, JPA ni conexión entre Java y PostgreSQL.

## Prueba

El programa `Main` crea una persona y dos residencias, las relaciona mediante `agregarResidencia()` y muestra como resultado:

```text
Persona: Juan Perez
Correo: juan.perez@gmail.com
Cantidad de residencias: 2
```

Y pega esto:

```markdown
# Capítulo 02 - Java 21

## Entidad padre

`Persona`

PK:

`personaId`

## Entidad dependiente

`Residencia`

FK:

`personaId`

## Relación

La relación es `1:N` porque una persona puede tener varias residencias.

En la clase `Persona` se representa mediante:

```java
private final List<Residencia> residencias = new ArrayList<>();