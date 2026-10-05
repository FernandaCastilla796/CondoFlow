package com.condoflow.residence.infrastructure.adapter.in.web;

import com.condoflow.person.domain.exception.PersonaNoEncontradaException;
import com.condoflow.residence.domain.exception.ResidenciaNoEncontradaException;
import com.condoflow.residence.domain.exception.ResidenciaVigenteDuplicadaException;
import com.condoflow.residence.domain.model.EstadoResidencia;
import com.condoflow.residence.domain.model.Residencia;
import com.condoflow.residence.domain.model.TipoResidencia;
import com.condoflow.residence.domain.port.in.ActualizarResidenciaUseCase;
import com.condoflow.residence.domain.port.in.ConsultarResidenciaUseCase;
import com.condoflow.residence.domain.port.in.EliminarResidenciaUseCase;
import com.condoflow.residence.domain.port.in.RegistrarResidenciaUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas obligatorias del Capítulo 07 y los cinco escenarios del Capítulo 08 (201, 400, 404, 409, 500).
 * GlobalExceptionHandler se carga automáticamente en @WebMvcTest.
 */
@WebMvcTest(ResidenciaController.class)
class ResidenciaControllerTest {

    private static final String VALIDA = """
            {"personaId":1,"unidadId":1,"tipoResidencia":"INQUILINO","fechaInicio":"2026-09-01"}
            """;

    private static final Residencia CREADA = new Residencia(3L, 1L, 1L, TipoResidencia.INQUILINO,
            LocalDate.of(2026, 9, 1), null, EstadoResidencia.VIGENTE);

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private RegistrarResidenciaUseCase registrar;
    @MockitoBean
    private ConsultarResidenciaUseCase consultar;
    @MockitoBean
    private ActualizarResidenciaUseCase actualizar;
    @MockitoBean
    private EliminarResidenciaUseCase eliminar;

    private static final String ACTUALIZAR = """
            {"personaId":2,"unidadId":1,"tipoResidencia":"INQUILINO","fechaInicio":"2026-09-01",
             "fechaFin":null,"estado":"VIGENTE"}
            """;

    @Test
    void registroValidoDevuelve201() throws Exception {
        when(registrar.registrar(any())).thenReturn(CREADA);

        mvc.perform(post("/api/residencias").contentType(MediaType.APPLICATION_JSON).content(VALIDA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.residenciaId").value(3))
                .andExpect(jsonPath("$.estado").value("VIGENTE"));
    }

    @Test
    void campoInvalidoDevuelve400ConErroresPorCampo() throws Exception {
        mvc.perform(post("/api/residencias").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"unidadId\":1,\"tipoResidencia\":\"INQUILINO\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/residencias"))
                .andExpect(jsonPath("$.fieldErrors.personaId").value("La persona es obligatoria"))
                .andExpect(jsonPath("$.fieldErrors.fechaInicio").exists());
        verify(registrar, never()).registrar(any());
    }

    @Test
    void valorFueraDelEnumDevuelve400() throws Exception {
        mvc.perform(post("/api/residencias").contentType(MediaType.APPLICATION_JSON)
                        .content(VALIDA.replace("INQUILINO", "ALQUILER")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("valor no permitido")));
    }

    @Test
    void padreInexistenteDevuelve404() throws Exception {
        when(registrar.registrar(any())).thenThrow(new PersonaNoEncontradaException(99L));

        mvc.perform(post("/api/residencias").contentType(MediaType.APPLICATION_JSON).content(VALIDA))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No existe una persona con id 99"));
    }

    @Test
    void residenciaVigenteDuplicadaDevuelve409() throws Exception {
        when(registrar.registrar(any())).thenThrow(new ResidenciaVigenteDuplicadaException(1L, 1L));

        mvc.perform(post("/api/residencias").contentType(MediaType.APPLICATION_JSON).content(VALIDA))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void errorNoControladoDevuelve500SinDetallesInternos() throws Exception {
        when(registrar.registrar(any())).thenThrow(new RuntimeException("detalle interno de la base"));

        mvc.perform(post("/api/residencias").contentType(MediaType.APPLICATION_JSON).content(VALIDA))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Ocurrió un error interno. Intente nuevamente más tarde."));
    }

    @Test
    void consultarPorIdDevuelve200() throws Exception {
        when(consultar.buscarPorId(3L)).thenReturn(Optional.of(CREADA));

        mvc.perform(get("/api/residencias/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personaId").value(1));
    }

    @Test
    void consultarInexistenteDevuelve404() throws Exception {
        when(consultar.buscarPorId(999L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/residencias/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No existe una residencia con id 999"));
    }

    @Test
    void listarTodasDevuelve200ConArreglo() throws Exception {
        when(consultar.listar()).thenReturn(List.of(CREADA));

        mvc.perform(get("/api/residencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].residenciaId").value(3));
    }

    @Test
    void listarPorPersonaDevuelve200() throws Exception {
        when(consultar.listarPorPersona(1L)).thenReturn(List.of(CREADA));

        mvc.perform(get("/api/residencias/persona/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ===== Guía 07 del frontend: PUT (incluye reasignar la persona) y DELETE =====

    @Test
    void putQueReasignaLaPersonaDevuelve200() throws Exception {
        Residencia reasignada = new Residencia(3L, 2L, 1L, TipoResidencia.INQUILINO,
                LocalDate.of(2026, 9, 1), null, EstadoResidencia.VIGENTE);
        when(actualizar.actualizar(eq(3L), any())).thenReturn(reasignada);

        mvc.perform(put("/api/residencias/3").contentType(MediaType.APPLICATION_JSON).content(ACTUALIZAR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.residenciaId").value(3))
                .andExpect(jsonPath("$.personaId").value(2));
    }

    @Test
    void putSinEstadoDevuelve400() throws Exception {
        mvc.perform(put("/api/residencias/3").contentType(MediaType.APPLICATION_JSON)
                        .content(ACTUALIZAR.replace(",\"estado\":\"VIGENTE\"", "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.estado").exists());
        verify(actualizar, never()).actualizar(any(), any());
    }

    @Test
    void putFinalizadaSinFechaFinDevuelve400() throws Exception {
        when(actualizar.actualizar(eq(3L), any()))
                .thenThrow(new IllegalArgumentException("Una residencia finalizada necesita fecha de fin"));

        mvc.perform(put("/api/residencias/3").contentType(MediaType.APPLICATION_JSON)
                        .content(ACTUALIZAR.replace("VIGENTE", "FINALIZADA")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Una residencia finalizada necesita fecha de fin"));
    }

    @Test
    void putConPersonaInexistenteDevuelve404() throws Exception {
        when(actualizar.actualizar(eq(3L), any())).thenThrow(new PersonaNoEncontradaException(99L));

        mvc.perform(put("/api/residencias/3").contentType(MediaType.APPLICATION_JSON).content(ACTUALIZAR))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No existe una persona con id 99"));
    }

    @Test
    void putQueDuplicaUnaResidenciaVigenteDevuelve409() throws Exception {
        when(actualizar.actualizar(eq(3L), any())).thenThrow(new ResidenciaVigenteDuplicadaException(2L, 1L));

        mvc.perform(put("/api/residencias/3").contentType(MediaType.APPLICATION_JSON).content(ACTUALIZAR))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteExistenteDevuelve204() throws Exception {
        mvc.perform(delete("/api/residencias/3"))
                .andExpect(status().isNoContent());
        verify(eliminar).eliminar(3L);
    }

    @Test
    void deleteInexistenteDevuelve404() throws Exception {
        doThrow(new ResidenciaNoEncontradaException(999L)).when(eliminar).eliminar(999L);

        mvc.perform(delete("/api/residencias/999"))
                .andExpect(status().isNotFound());
    }
}
