package com.condoflow.shared.web;

import com.condoflow.shared.application.ProjectInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Capítulo 03: GET /api/health responde 200 usando el Bean ProjectInfoService inyectado por constructor. */
@WebMvcTest(HealthController.class)
@Import(ProjectInfoService.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void healthDevuelve200() throws Exception {
        mvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.application").value("condoflow-backend"));
    }
}
