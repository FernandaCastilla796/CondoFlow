# Capítulo 03 - Introducción a Spring Boot

Código: [`backend/`](../../backend) (proyecto `condoflow-backend`).

## Ficha

| Dato | Valor |
|---|---|
| Nombre del sistema | CondoFlow |
| Nombre del backend | `condoflow-backend` |
| Package base | `com.condoflow` |
| Entidad padre 1:N | `Persona` |
| Entidad dependiente N | `Residencia` |
| Relación | 1 persona tiene muchas residencias |

## Configuración del proyecto

Spring Initializr con Maven, Java 21, packaging Jar y dependencias **Spring Web**, **Validation** y **DevTools**. Sin JPA ni driver de PostgreSQL en este capítulo.

## Endpoints

| Verbo | Ruta | Respuesta |
|---|---|---|
| GET | `/api/health` | `200` con `status`, `application`, `stage`, `timestamp` |
| GET | `/api/personas/demo` | `200` con un `PersonaDemoResponse` fijo (reemplazado en el Capítulo 04) |

Pruebas en [`backend/requests.http`](../../backend/requests.http).

## Flujo de una petición

```text
Cliente HTTP
   ↓
GET /api/health
   ↓
Spring MVC (DispatcherServlet)
   ↓
HealthController           ← Bean creado por Spring
   ↓
ProjectInfoService         ← Bean inyectado por constructor
   ↓
Map → JSON, HTTP 200
```

## Conceptos

| Concepto | Explicación |
|---|---|
| Spring Boot | Arma la aplicación con configuración automática y un servidor web embebido (Tomcat en el puerto 8080). |
| Bean | Objeto que crea y administra Spring, por ejemplo `ProjectInfoService`. |
| IoC | No hacemos `new ProjectInfoService()`: Spring decide cuándo crear los objetos y los conecta. |
| DI | El controller recibe sus dependencias desde afuera, por el constructor. |
| `@RestController` | Clase que atiende peticiones HTTP y devuelve JSON. |
| `@Service` | Marca una clase de la capa de aplicación como Bean. |
| `@GetMapping` | Asocia un método al verbo GET de una ruta. |

## Evidencia

- `./mvnw test` levanta el contexto de Spring correctamente (`CondoflowBackendApplicationTests`).
- `GET /api/health` → `200 OK`.
