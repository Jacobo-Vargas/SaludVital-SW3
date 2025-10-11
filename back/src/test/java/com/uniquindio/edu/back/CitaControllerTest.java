package com.uniquindio.edu.back;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.edu.back.mapper.CitaMapper;
import com.uniquindio.edu.back.model.dto.CitaDTO;
import com.uniquindio.edu.back.service.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CitaService citaService;

    @Autowired
    private CitaMapper citaMapper;

    @BeforeEach
    void limpiarBase() throws Exception {
        // Elimina todas las citas existentes
        citaService.listarCitas().forEach(c -> {
            try {
                mockMvc.perform(delete("/api/citas/{id}", c.getId()))
                        .andExpect(status().isNoContent());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void deberiaCrearYListarCitas() throws Exception {
        CitaDTO citaDTO = new CitaDTO(null, "Laura", "Cardiología", "2025-10-25 09:00", "Chequeo");

        // Crear cita
        mockMvc.perform(post("/api/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citaDTO)))
                .andExpect(status().isCreated());

        // Listar citas
        mockMvc.perform(get("/api/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paciente").value("Laura"));
    }

    @Test
    void deberiaActualizarCitaExistente() throws Exception {
        CitaDTO citaDTO = new CitaDTO(null, "Andrés", "Pediatría", "2025-10-22 08:30", "Revisión");

        // Crear cita
        var result = mockMvc.perform(post("/api/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(citaDTO)))
                .andReturn();

        String location = result.getResponse().getHeader("Location");
        assert location != null;

        CitaDTO actualizadaDTO = new CitaDTO(null, "Andrés Gómez", "Pediatría", "2025-10-22 09:00", "Control anual");

        // Actualizar cita
        mockMvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizadaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paciente").value("Andrés Gómez"));
    }
}
