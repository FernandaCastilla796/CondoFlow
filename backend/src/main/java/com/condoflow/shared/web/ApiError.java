package com.condoflow.shared.web;

import java.time.Instant;
import java.util.Map;

/**
 * Contrato único de error de la API: todas las respuestas 4xx/5xx tienen esta forma,
 * así web y móvil pueden mostrar mensajes sin conocer detalles internos del backend.
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
}
