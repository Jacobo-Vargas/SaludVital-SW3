package com.uniquindio.edu.back;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.edu.back.model.dto.ResultadoMedicoDTO;
import com.uniquindio.edu.back.service.ResultadoMedicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(com.uniquindio.edu.back.controller.ResultadoMedicoController.class)
class ResultadoMedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @org.springframework.boot.test.mock.mockito.MockBean
    private ResultadoMedicoService resultadoMedicoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultadoMedicoDTO resultadoMedicoDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime fechaExamen = LocalDateTime.now().minusDays(1);
        LocalDateTime fechaEmision = LocalDateTime.now();

        resultadoMedicoDTO = new ResultadoMedicoDTO(
                1L, "Juan Pérez", "Hemograma Completo", 
                "Hemoglobina: 14.2 g/dL (Normal)", 
                "Dr. María González", fechaExamen, fechaEmision, 
                "Resultados normales", "COMPLETADO");
    }

    @Test
    void crearResultadoMedico_DeberiaCrearYRetornar201() throws Exception {
        // Arrange
        when(resultadoMedicoService.crearResultadoMedico(any(ResultadoMedicoDTO.class)))
                .thenReturn(resultadoMedicoDTO);

        // Act & Assert
        mockMvc.perform(post("/api/resultados-medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resultadoMedicoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.paciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.tipoExamen").value("Hemograma Completo"))
                .andExpect(jsonPath("$.estado").value("COMPLETADO"));
    }

    @Test
    void listarResultadosMedicos_DeberiaRetornarListaDeResultados() throws Exception {
        // Arrange
        List<ResultadoMedicoDTO> resultados = Arrays.asList(resultadoMedicoDTO);
        when(resultadoMedicoService.listarResultadosMedicos()).thenReturn(resultados);

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].paciente").value("Juan Pérez"));
    }

    @Test
    void obtenerResultadoMedico_ConIdExistente_DeberiaRetornarResultado() throws Exception {
        // Arrange
        when(resultadoMedicoService.obtenerResultadoMedico(1L))
                .thenReturn(Optional.of(resultadoMedicoDTO));

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.paciente").value("Juan Pérez"));
    }

    @Test
    void obtenerResultadoMedico_ConIdInexistente_DeberiaRetornar404() throws Exception {
        // Arrange
        when(resultadoMedicoService.obtenerResultadoMedico(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarResultadoMedico_ConIdExistente_DeberiaActualizarYRetornar200() throws Exception {
        // Arrange
        when(resultadoMedicoService.actualizarResultadoMedico(eq(1L), any(ResultadoMedicoDTO.class)))
                .thenReturn(resultadoMedicoDTO);

        // Act & Assert
        mockMvc.perform(put("/api/resultados-medicos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resultadoMedicoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.paciente").value("Juan Pérez"));
    }

    @Test
    void eliminarResultadoMedico_ConIdExistente_DeberiaEliminarYRetornar204() throws Exception {
        // Arrange
        when(resultadoMedicoService.eliminarResultadoMedico(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/resultados-medicos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarResultadoMedico_ConIdInexistente_DeberiaRetornar404() throws Exception {
        // Arrange
        when(resultadoMedicoService.eliminarResultadoMedico(999L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/resultados-medicos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorPaciente_DeberiaRetornarResultadosDelPaciente() throws Exception {
        // Arrange
        List<ResultadoMedicoDTO> resultados = Arrays.asList(resultadoMedicoDTO);
        when(resultadoMedicoService.buscarPorPaciente("Juan")).thenReturn(resultados);

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/buscar/paciente")
                        .param("paciente", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].paciente").value("Juan Pérez"));
    }

    @Test
    void buscarPorTipoExamen_DeberiaRetornarResultadosDelTipo() throws Exception {
        // Arrange
        List<ResultadoMedicoDTO> resultados = Arrays.asList(resultadoMedicoDTO);
        when(resultadoMedicoService.buscarPorTipoExamen("Hemograma")).thenReturn(resultados);

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/buscar/tipo-examen")
                        .param("tipoExamen", "Hemograma"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].tipoExamen").value("Hemograma Completo"));
    }

    @Test
    void buscarPorEstado_DeberiaRetornarResultadosConEstado() throws Exception {
        // Arrange
        List<ResultadoMedicoDTO> resultados = Arrays.asList(resultadoMedicoDTO);
        when(resultadoMedicoService.buscarPorEstado("COMPLETADO")).thenReturn(resultados);

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/buscar/estado")
                        .param("estado", "COMPLETADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].estado").value("COMPLETADO"));
    }

    @Test
    void buscarResultadosPendientes_DeberiaRetornarResultadosPendientes() throws Exception {
        // Arrange
        List<ResultadoMedicoDTO> resultados = Arrays.asList(resultadoMedicoDTO);
        when(resultadoMedicoService.buscarResultadosPendientes()).thenReturn(resultados);

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/pendientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void buscarResultadosRecientes_DeberiaRetornarResultadosRecientes() throws Exception {
        // Arrange
        List<ResultadoMedicoDTO> resultados = Arrays.asList(resultadoMedicoDTO);
        when(resultadoMedicoService.buscarResultadosRecientes()).thenReturn(resultados);

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/recientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void cambiarEstado_DeberiaCambiarEstadoYRetornar200() throws Exception {
        // Arrange
        ResultadoMedicoDTO resultadoActualizado = new ResultadoMedicoDTO();
        resultadoActualizado.setId(1L);
        resultadoActualizado.setEstado("REVISADO");
        
        when(resultadoMedicoService.cambiarEstado(1L, "REVISADO")).thenReturn(resultadoActualizado);

        // Act & Assert
        mockMvc.perform(put("/api/resultados-medicos/1/cambiar-estado")
                        .param("estado", "REVISADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("REVISADO"));
    }
}
