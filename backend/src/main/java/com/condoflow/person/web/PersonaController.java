package com.condoflow.person.web;

import com.condoflow.person.application.PersonaDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaDemoService personaDemoService;

    public PersonaController(PersonaDemoService personaDemoService) {
        this.personaDemoService = personaDemoService;
    }

    @GetMapping("/demo")
    public PersonaDemoResponse demo() {
        return personaDemoService.obtenerDemo();
    }
}