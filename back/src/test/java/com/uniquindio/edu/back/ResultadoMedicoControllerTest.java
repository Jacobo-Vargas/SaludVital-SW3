package com.uniquindio.edu.back;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.uniquindio.edu.back.controller.ResultadoMedicoController;
import com.uniquindio.edu.back.model.dto.ResultadoMedicoDTO;
import com.uniquindio.edu.back.service.ResultadoMedicoService;

@ExtendWith(MockitoExtension.class)
class ResultadoMedicoControllerTest {

    @Mock
    private ResultadoMedicoService resultadoMedicoService;

    @InjectMocks
    private ResultadoMedicoController resultadoMedicoController;

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
    void crearResultadoMedico_DeberiaRetornar201ConResultadoCreado() {
        // Arrange
        when(resultadoMedicoService.crearResultadoMedico(any(ResultadoMedicoDTO.class)))
                .thenReturn(resultadoMedicoDTO);

        // Act
        ResponseEntity<ResultadoMedicoDTO> response = resultadoMedicoController.crearResultadoMedico(resultadoMedicoDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        if (response.getBody() != null) {
            if (response.getBody() != null) {
            assertEquals("Juan Pérez", response.getBody().getPaciente());
        }
        }
    }

    @Test
    void listarResultadosMedicos_DeberiaRetornarListaDeResultados() {
        // Arrange
        List<ResultadoMedicoDTO> resultados = Arrays.asList(resultadoMedicoDTO);
        when(resultadoMedicoService.listarResultadosMedicos()).thenReturn(resultados);

        // Act
        List<ResultadoMedicoDTO> response = resultadoMedicoController.listarResultadosMedicos();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Juan Pérez", response.get(0).getPaciente());
    }

    @Test
    void obtenerResultadoMedico_ConIdExistente_DeberiaRetornar200ConResultado() {
        // Arrange
        when(resultadoMedicoService.obtenerResultadoMedico(1L))
                .thenReturn(Optional.of(resultadoMedicoDTO));

        // Act
        ResponseEntity<ResultadoMedicoDTO> response = resultadoMedicoController.obtenerResultadoMedico(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        if (response.getBody() != null) {
            assertEquals("Juan Pérez", response.getBody().getPaciente());
        }
    }

    @Test
    void obtenerResultadoMedico_ConIdInexistente_DeberiaRetornar404() {
        // Arrange
        when(resultadoMedicoService.obtenerResultadoMedico(999L))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<ResultadoMedicoDTO> response = resultadoMedicoController.obtenerResultadoMedico(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void actualizarResultadoMedico_ConIdExistente_DeberiaRetornar200ConResultadoActualizado() {
        // Arrange
        when(resultadoMedicoService.actualizarResultadoMedico(1L, resultadoMedicoDTO))
                .thenReturn(resultadoMedicoDTO);

        // Act
        ResponseEntity<ResultadoMedicoDTO> response = resultadoMedicoController.actualizarResultadoMedico(1L, resultadoMedicoDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        if (response.getBody() != null) {
            assertEquals("Juan Pérez", response.getBody().getPaciente());
        }
    }

    @Test
    void eliminarResultadoMedico_ConIdExistente_DeberiaRetornar204() {
        // Arrange
        when(resultadoMedicoService.eliminarResultadoMedico(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = resultadoMedicoController.eliminarResultadoMedico(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void eliminarResultadoMedico_ConIdInexistente_DeberiaRetornar404() {
        // Arrange
        when(resultadoMedicoService.eliminarResultadoMedico(999L)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = resultadoMedicoController.eliminarResultadoMedico(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void buscarPorPaciente_DeberiaRetornarListaDeResultados() {
        // Arrange
        List<ResultadoMedicoDTO> resultados = Arrays.asList(resultadoMedicoDTO);
        when(resultadoMedicoService.buscarPorPaciente("Juan")).thenReturn(resultados);

        // Act
        List<ResultadoMedicoDTO> response = resultadoMedicoController.buscarPorPaciente("Juan");

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Juan Pérez", response.get(0).getPaciente());
    }

    @Test
    void buscarPorEstado_DeberiaRetornarListaDeResultados() {
        // Arrange
        List<ResultadoMedicoDTO> resultados = Arrays.asList(resultadoMedicoDTO);
        when(resultadoMedicoService.buscarPorEstado("COMPLETADO")).thenReturn(resultados);

        // Act
        List<ResultadoMedicoDTO> response = resultadoMedicoController.buscarPorEstado("COMPLETADO");

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("COMPLETADO", response.get(0).getEstado());
    }

    @Test
    void cambiarEstado_ConIdExistente_DeberiaRetornar200ConResultadoActualizado() {
        // Arrange
        when(resultadoMedicoService.cambiarEstado(1L, "REVISADO"))
                .thenReturn(resultadoMedicoDTO);

        // Act
        ResponseEntity<ResultadoMedicoDTO> response = resultadoMedicoController.cambiarEstado(1L, "REVISADO");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        if (response.getBody() != null) {
            assertEquals("Juan Pérez", response.getBody().getPaciente());
        }
    }
}
