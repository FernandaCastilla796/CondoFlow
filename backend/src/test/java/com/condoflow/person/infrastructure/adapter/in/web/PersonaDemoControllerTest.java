package com.condoflow.person.infrastructure.adapter.in.web;

import com.condoflow.person.application.service.PersonaDemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Capítulo 03: el endpoint demo de la entidad padre responde 200 sin base de datos. */
@WebMvcTest(PersonaDemoController.class)
@Import(PersonaDemoService.class)
class PersonaDemoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void demoDevuelve200() throws Exception {
        mvc.perform(get("/api/personas/demo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personaId").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }
}
