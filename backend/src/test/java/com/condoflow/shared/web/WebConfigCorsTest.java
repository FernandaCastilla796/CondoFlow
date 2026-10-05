package com.condoflow.shared.web;

import com.condoflow.shared.application.ProjectInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Guía 05 del frontend: sólo el origen de Vite puede leer las respuestas de /api/** desde el navegador. */
@WebMvcTest(HealthController.class)
@Import(ProjectInfoService.class)
class WebConfigCorsTest {

    private static final String VITE = "http://localhost:5173";

    @Autowired
    private MockMvc mvc;

    @Test
    void preflightDelOrigenDeViteEsAceptado() throws Exception {
        mvc.perform(options("/api/health")
                        .header(HttpHeaders.ORIGIN, VITE)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, VITE));
    }

    @Test
    void getDesdeElOrigenDeViteIncluyeLaCabeceraCors() throws Exception {
        mvc.perform(get("/api/health").header(HttpHeaders.ORIGIN, VITE))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, VITE));
    }

    @Test
    void otroOrigenEsRechazado() throws Exception {
        mvc.perform(get("/api/health").header(HttpHeaders.ORIGIN, "http://sitio-desconocido.com"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }
}
