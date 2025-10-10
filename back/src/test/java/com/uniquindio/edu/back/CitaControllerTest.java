package com.uniquindio.edu.back;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.uniquindio.edu.back.model.Cita;

@SpringBootTest
@AutoConfigureMockMvc
public class CitaControllerTest {
     @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deberiaCrearYListarCitas() throws Exception {
        Cita cita = new Cita(null, "Laura", "Cardiología", "2025-10-25 09:00", "Chequeo");

        // Crear cita
        mockMvc.perform(post("/api/citas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isCreated());

        // Listar citas
        mockMvc.perform(get("/api/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paciente").value("Laura"));
    }

    @Test
    void deberiaActualizarCitaExistente() throws Exception {
        Cita cita = new Cita(null, "Andrés", "Pediatría", "2025-10-22 08:30", "Revisión");
        var result = mockMvc.perform(post("/api/citas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cita)))
                .andReturn();

        String location = result.getResponse().getHeader("Location");
        assert location != null;

        Cita actualizada = new Cita(null, "Andrés Gómez", "Pediatría", "2025-10-22 09:00", "Control anual");

        mockMvc.perform(put(location)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paciente").value("Andrés Gómez"));
    }
}
