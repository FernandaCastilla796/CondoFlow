package com.condoflow.person.infrastructure.adapter.in.web;

import com.condoflow.person.domain.exception.CorreoPersonaDuplicadoException;
import com.condoflow.person.domain.model.EstadoPersona;
import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.domain.port.in.ConsultarPersonaUseCase;
import com.condoflow.person.domain.port.in.RegistrarPersonaUseCase;
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

/** Contrato HTTP de /api/personas, aislado de la base de datos (los casos de uso se simulan). */
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
    private RegistrarPersonaUseCase registrar;

    @MockitoBean
    private ConsultarPersonaUseCase consultar;

    @Test
    void postValidoDevuelve201ConElRecurso() throws Exception {
        when(registrar.registrar(any())).thenReturn(MARIA);

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
        verify(registrar, never()).registrar(any());
    }

    @Test
    void postConFormatoInvalidoDevuelve400() throws Exception {
        mvc.perform(post("/api/personas").contentType(MediaType.APPLICATION_JSON)
                        .content(VALIDA.replace("Maria.Lopez@CondoFlow.com", "no-es-un-correo")))
                .andExpect(status().isBadRequest());
        verify(registrar, never()).registrar(any());
    }

    @Test
    void getListaDevuelve200ConArreglo() throws Exception {
        when(consultar.listar(isNull())).thenReturn(List.of(MARIA));

        mvc.perform(get("/api/personas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getPorIdExistenteDevuelve200() throws Exception {
        when(consultar.buscarPorId(1L)).thenReturn(Optional.of(MARIA));

        mvc.perform(get("/api/personas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Maria"));
    }

    @Test
    void getPorIdInexistenteDevuelve404() throws Exception {
        when(consultar.buscarPorId(999L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/personas/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No existe una persona con id 999"));
    }

    @Test
    void postConCorreoDuplicadoDevuelve409() throws Exception {
        when(registrar.registrar(any()))
                .thenThrow(new CorreoPersonaDuplicadoException("maria.lopez@condoflow.com"));

        mvc.perform(post("/api/personas").contentType(MediaType.APPLICATION_JSON).content(VALIDA))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Ya existe una persona registrada con el correo maria.lopez@condoflow.com"));
    }

    @Test
    void erroresDeValidacionIncluyenElCampo() throws Exception {
        mvc.perform(post("/api/personas").contentType(MediaType.APPLICATION_JSON)
                        .content(VALIDA.replace("Maria.Lopez@CondoFlow.com", "no-es-un-correo")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.correoElectronico")
                        .value("El correo electrónico no tiene un formato válido"));
    }

    @Test
    void getConParametroBuscarLoEnviaAlCasoDeUso() throws Exception {
        when(consultar.listar("rojas")).thenReturn(List.of());

        mvc.perform(get("/api/personas").param("buscar", "rojas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        verify(consultar).listar("rojas");
    }
}
