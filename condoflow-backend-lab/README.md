# condoflow-backend-lab

Laboratorio de Java 21 puro de los Capítulos 01 y 02 (sin Spring Boot, JPA ni PostgreSQL).
El backend real con Spring Boot está en [`../backend`](../backend).

## Cómo ejecutarlo

En IntelliJ: abrir la carpeta `condoflow-backend-lab` (nombre `<nombre-proyecto>-backend-lab` exigido por el Capítulo 01) y ejecutar `com.condoflow.Main` con JDK 21.

Desde terminal (con Maven instalado):

```bash
mvn -q compile exec:java -Dexec.mainClass=com.condoflow.Main
```

## Documentación

- [Capítulo 01 - Java esencial](../docs/05-java/README-capitulo01.md)
- [Capítulo 02 - Contratos, colecciones y errores](../docs/05-java/README-capitulo02.md)
