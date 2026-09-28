package com.condoflow.person.application.service;

import org.springframework.stereotype.Service;

/**
 * Capítulo 03: servicio temporal que devuelve una persona de ejemplo, todavía sin base de datos.
 * Se conserva como evidencia del primer endpoint de la entidad padre.
 */
@Service
public class PersonaDemoService {

    public PersonaDemoResponse obtenerDemo() {
        return new PersonaDemoResponse(
                1L,
                "Juan",
                "Perez",
                "juan.perez@gmail.com"
        );
    }
}
