package com.condoflow.person.infrastructure.adapter.in.web;

import com.condoflow.person.application.PersonaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Pruebas mínimas obligatorias del Capítulo 04. */
@WebMvcTest(PersonaController.class)
@Import(PersonaService.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PersonaControllerTest {

    private static final String VALIDA = """
            {"nombre":"Maria","apellido":"Lopez","documento":"7845123",
             "telefono":"+591 70000001","correoElectronico":"Maria.Lopez@CondoFlow.com"}
            """;

    @Autowired
    private MockMvc mvc;

    private void crear(String json) throws Exception {
        mvc.perform(post("/api/personas").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void postValidoDevuelve201ConElRecurso() throws Exception {
        mvc.perform(post("/api/personas").contentType(MediaType.APPLICATION_JSON).content(VALIDA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personaId").value(1))
                .andExpect(jsonPath("$.correoElectronico").value("maria.lopez@condoflow.com"))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    void postConCampoObligatorioVacioDevuelve400() throws Exception {
        mvc.perform(post("/api/personas").contentType(MediaType.APPLICATION_JSON)
                        .content(VALIDA.replace("\"Maria\"", "\"  \"")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postConFormatoInvalidoDevuelve400() throws Exception {
        mvc.perform(post("/api/personas").contentType(MediaType.APPLICATION_JSON)
                        .content(VALIDA.replace("Maria.Lopez@CondoFlow.com", "no-es-un-correo")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getListaDevuelve200ConArreglo() throws Exception {
        crear(VALIDA);
        mvc.perform(get("/api/personas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getPorIdExistenteDevuelve200() throws Exception {
        crear(VALIDA);
        mvc.perform(get("/api/personas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Maria"));
    }

    @Test
    void getPorIdInexistenteDevuelve404() throws Exception {
        mvc.perform(get("/api/personas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getConParametroBuscarFiltraResultados() throws Exception {
        crear(VALIDA);
        crear(VALIDA.replace("Maria", "Carlos").replace("Lopez", "Rojas"));
        mvc.perform(get("/api/personas").param("buscar", "rojas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Carlos"));
    }
}
