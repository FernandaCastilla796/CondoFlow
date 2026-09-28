package com.condoflow.person.infrastructure.adapter.in.web;

import com.condoflow.person.application.PersonaService;
import com.condoflow.person.domain.model.EstadoPersona;
import com.condoflow.person.domain.model.Persona;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Contrato HTTP de /api/personas (Capítulo 04), aislado de la base de datos. */
@WebMvcTest(PersonaController.class)
class PersonaControllerTest {

    private static final String VALIDA = """
            {"nombre":"Maria","apellido":"Lopez","documento":"7845123",
             "telefono":"+591 70000001","correoElectronico":"Maria.Lopez@CondoFlow.com"}
            """;

    private static final Persona MARIA = new Persona(1L, "Maria", "Lopez", "7845123",
            "+591 70000001", "maria.lopez@condoflow.com", EstadoPersona.ACTIVO);

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private PersonaService service;

    @Test
    void postValidoDevuelve201ConElRecurso() throws Exception {
        when(service.registrar(any())).thenReturn(MARIA);

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
        verify(service, never()).registrar(any());
    }

    @Test
    void postConFormatoInvalidoDevuelve400() throws Exception {
        mvc.perform(post("/api/personas").contentType(MediaType.APPLICATION_JSON)
                        .content(VALIDA.replace("Maria.Lopez@CondoFlow.com", "no-es-un-correo")))
                .andExpect(status().isBadRequest());
        verify(service, never()).registrar(any());
    }

    @Test
    void getListaDevuelve200ConArreglo() throws Exception {
        when(service.listar(isNull())).thenReturn(List.of(MARIA));

        mvc.perform(get("/api/personas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getPorIdExistenteDevuelve200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(MARIA));

        mvc.perform(get("/api/personas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Maria"));
    }

    @Test
    void getPorIdInexistenteDevuelve404() throws Exception {
        when(service.buscarPorId(999L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/personas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getConParametroBuscarLoEnviaAlServicio() throws Exception {
        when(service.listar("rojas")).thenReturn(List.of());

        mvc.perform(get("/api/personas").param("buscar", "rojas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        verify(service).listar("rojas");
    }
}
