# Capítulo 01 - Java esencial

## Entidades elegidas

- Tabla padre: persona
- Tabla dependiente: esidencia
- Relación: 1:N
- PK de persona: persona_id
- PK de residencia: esidencia_id
- FK: esidencia.persona_id → persona.persona_id

## Clases Java

- Persona
- Residencia
- PersonaRepository
- Main

## Relación 1:N

Una persona puede tener varias residencias.

En Java esta relación se representa mediante una colección privada de Residencia.

## Regla implementada

La clase Residencia valida que esidenciaId, personaId y unidadId no sean 
ull.

En PostgreSQL, persona.correo_electronico tiene una restricción de unicidad.

## Decisiones

- Los atributos son private.
- La colección de residencias es privada.
- getResidencias() devuelve una colección no modificable.
- PersonaRepository define el contrato para guardar y buscar una persona.
- No se implementaron todavía Spring Boot, JPA ni PostgreSQL desde Java.

## Prueba

Main crea una persona y dos residencias y las relaciona mediante gregarResidencia().

Resultado:

    Persona: Juan Perez
    Correo: juan.perez@gmail.com
    Cantidad de residencias: 2
