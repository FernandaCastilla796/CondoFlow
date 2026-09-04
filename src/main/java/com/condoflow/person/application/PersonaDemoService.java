package com.condoflow.person.application;

import com.condoflow.person.web.PersonaDemoResponse;
import org.springframework.stereotype.Service;

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