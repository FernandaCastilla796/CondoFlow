# DER lógico v0.1 — CondoFlow

## 1. Objetivo

Representar gráficamente la estructura lógica de la base de datos de CondoFlow, mostrando las entidades principales, sus atributos, claves primarias, claves foráneas y relaciones.

Este diagrama se construye a partir del modelo relacional v0.1 definido previamente.

## 2. Convenciones

- PK = clave primaria
- FK = clave foránea
- UQ = unicidad
- NN = obligatorio

## 3. Entidades

### unidad

PK unidad_id  
NN numero_unidad  
NN tipo  
NN estado  
UQ numero_unidad

### persona

PK persona_id  
NN nombre  
NN apellido  
NN correo_electronico  
UQ correo_electronico

### residencia

PK residencia_id  
FK persona_id -> persona.persona_id  
FK unidad_id -> unidad.unidad_id  
NN persona_id  
NN unidad_id

### area_comun

PK area_comun_id  
NN nombre  
NN descripcion  
NN capacidad  
NN estado  
UQ nombre

### reserva

PK reserva_id  
FK area_comun_id -> area_comun.area_comun_id  
FK persona_id -> persona.persona_id  
NN area_comun_id  
NN persona_id  
NN fecha_inicio  
NN fecha_fin  
NN estado

### visita

PK visita_id  
FK unidad_id -> unidad.unidad_id  
NN unidad_id  
NN nombre_visitante  
NN documento_visitante  
NN fecha_ingreso  
fecha_salida  
NN estado

### incidencia

PK incidencia_id  
FK unidad_id -> unidad.unidad_id  
FK persona_id -> persona.persona_id  
NN unidad_id  
NN persona_id  
NN descripcion  
NN fecha_reporte  
NN estado

### tarea_mantenimiento

PK tarea_mantenimiento_id  
FK incidencia_id -> incidencia.incidencia_id  
NN incidencia_id  
NN descripcion  
NN fecha_asignacion  
fecha_finalizacion  
NN estado

## 4. Relaciones

1. persona 1 ---- N residencia
2. unidad 1 ---- N residencia
3. area_comun 1 ---- N reserva
4. persona 1 ---- N reserva
5. unidad 1 ---- N visita
6. unidad 1 ---- N incidencia
7. persona 1 ---- N incidencia
8. incidencia 1 ---- N tarea_mantenimiento

## 5. Reglas que afectan el modelo

- RN-__: Una residencia debe estar asociada a una persona y a una unidad.
- RN-__: Una reserva debe estar asociada a un área común y a una persona.
- RN-__: Una incidencia debe estar asociada a una unidad y a una persona.
- RN-__: Una tarea de mantenimiento debe estar asociada a una incidencia.
