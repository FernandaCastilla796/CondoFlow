package com.condoflow.person.infrastructure.adapter.in.web;

import com.condoflow.person.application.service.PersonaDemoResponse;
import com.condoflow.person.application.service.PersonaDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Capítulo 03: endpoint demo de la entidad padre, con el servicio inyectado por constructor. */
@RestController
@RequestMapping("/api/personas")
public class PersonaDemoController {

    private final PersonaDemoService service;

    public PersonaDemoController(PersonaDemoService service) {
        this.service = service;
    }

    @GetMapping("/demo")
    public PersonaDemoResponse demo() {
        return service.obtenerDemo();
    }
}
