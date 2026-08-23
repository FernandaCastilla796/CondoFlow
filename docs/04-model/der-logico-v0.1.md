\# DER lógico v0.1 — CondoFlow



\## 1. Objetivo



Representar gráficamente la estructura lógica de la base de datos de CondoFlow, mostrando las entidades principales, sus atributos, claves primarias, claves foráneas y relaciones.



Este diagrama se construye a partir del modelo relacional v0.1 definido previamente.



\## 2. Convenciones



\- PK = clave primaria

\- FK = clave foránea

\- UQ = unicidad

\- NN = obligatorio



\## 3. Entidades



\### unidad



PK unidad\_id  

NN numero\_unidad  

NN tipo  

NN estado  

UQ numero\_unidad



\### persona



PK persona\_id  

NN nombre  

NN apellido  

NN correo\_electronico  

UQ correo\_electronico



\### residencia



PK residencia\_id  

FK persona\_id -> persona.persona\_id  

FK unidad\_id -> unidad.unidad\_id  

NN persona\_id  

NN unidad\_id



\### area\_comun



PK area\_comun\_id  

NN nombre  

NN descripcion  

NN capacidad  

NN estado  

UQ nombre



\### reserva



PK reserva\_id  

FK area\_comun\_id -> area\_comun.area\_comun\_id  

FK persona\_id -> persona.persona\_id  

NN area\_comun\_id  

NN persona\_id  

NN fecha\_inicio  

NN fecha\_fin  

NN estado



\### visita



PK visita\_id  

FK unidad\_id -> unidad.unidad\_id  

NN unidad\_id  

NN nombre\_visitante  

NN documento\_visitante  

NN fecha\_ingreso  

fecha\_salida  

NN estado



\### incidencia



PK incidencia\_id  

FK unidad\_id -> unidad.unidad\_id  

FK persona\_id -> persona.persona\_id  

NN unidad\_id  

NN persona\_id  

NN descripcion  

NN fecha\_reporte  

NN estado



\### tarea\_mantenimiento



PK tarea\_mantenimiento\_id  

FK incidencia\_id -> incidencia.incidencia\_id  

NN incidencia\_id  

NN descripcion  

NN fecha\_asignacion  

fecha\_finalizacion  

NN estado



\## 4. Relaciones



1\. persona 1 ---- N residencia

2\. unidad 1 ---- N residencia

3\. area\_comun 1 ---- N reserva

4\. persona 1 ---- N reserva

5\. unidad 1 ---- N visita

6\. unidad 1 ---- N incidencia

7\. persona 1 ---- N incidencia

8\. incidencia 1 ---- N tarea\_mantenimiento



\## 5. Reglas que afectan el modelo



\- RN-\_\_: Una residencia debe estar asociada a una persona y a una unidad.

\- RN-\_\_: Una reserva debe estar asociada a un área común y a una persona.

\- RN-\_\_: Una incidencia debe estar asociada a una unidad y a una persona.

\- RN-\_\_: Una tarea de mantenimiento debe estar asociada a una incidencia.

