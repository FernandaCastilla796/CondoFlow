package com.condoflow.shared.web;

import com.condoflow.person.domain.exception.CorreoPersonaDuplicadoException;
import com.condoflow.person.domain.exception.PersonaConRegistrosAsociadosException;
import com.condoflow.person.domain.exception.PersonaNoEncontradaException;
import com.condoflow.residence.domain.exception.ResidenciaNoEncontradaException;
import com.condoflow.residence.domain.exception.ResidenciaVigenteDuplicadaException;
import com.condoflow.unit.domain.exception.UnidadNoEncontradaException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduce excepciones a respuestas HTTP con el formato ApiError.
 * Los controllers quedan limpios: no tienen try/catch.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** 404: el recurso pedido o un padre referenciado no existe. */
    @ExceptionHandler({
            PersonaNoEncontradaException.class,
            UnidadNoEncontradaException.class,
            ResidenciaNoEncontradaException.class
    })
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, Map.of());
    }

    /** 409: la petición es válida pero choca con el estado actual de los datos (regla de negocio). */
    @ExceptionHandler({
            CorreoPersonaDuplicadoException.class,
            PersonaConRegistrosAsociadosException.class,
            ResidenciaVigenteDuplicadaException.class
    })
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request, Map.of());
    }

    /** 400: validaciones de formato del Request DTO (@Valid). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> fieldErrors.putIfAbsent(fe.getField(), fe.getDefaultMessage()));
        return build(HttpStatus.BAD_REQUEST, "La solicitud tiene campos inválidos", request, fieldErrors);
    }

    /** 400: JSON mal formado o valor que no pertenece a un enum (p. ej. tipoResidencia = "ALQUILER"). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST,
                "El cuerpo de la solicitud no es un JSON válido o contiene un valor no permitido", request, Map.of());
    }

    /** 400: un parámetro de la ruta no tiene el tipo esperado (p. ej. /api/personas/abc). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                       HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST,
                "El parámetro '" + ex.getName() + "' tiene un formato inválido", request, Map.of());
    }

    /** 400: una validación del constructor del dominio rechazó los datos (p. ej. un campo obligatorio vacío). */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request, Map.of());
    }

    /**
     * 409: última línea de defensa. Si dos peticiones simultáneas pasan la validación del caso de uso,
     * PostgreSQL rechaza la segunda por un UNIQUE/FK/CHECK. No se devuelve el mensaje interno de la base.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex,
                                                        HttpServletRequest request) {
        log.warn("Violación de integridad en {}: {}", request.getRequestURI(), ex.getMostSpecificCause().getMessage());
        return build(HttpStatus.CONFLICT,
                "La operación viola una restricción de integridad de los datos", request, Map.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResource(NoResourceFoundException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "La ruta solicitada no existe", request, Map.of());
    }

    /** 500: error no previsto. Se registra completo en el log y el cliente recibe un mensaje genérico. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Error no controlado en {} {}", request.getMethod(), request.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno. Intente nuevamente más tarde.", request, Map.of());
    }

    private static ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest request,
                                                  Map<String, String> fieldErrors) {
        var body = new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message,
                request.getRequestURI(), fieldErrors);
        return ResponseEntity.status(status).body(body);
    }
}
