package com.uniquindio.edu.back;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uniquindio.edu.back.mapper.ResultadoMedicoMapper;
import com.uniquindio.edu.back.model.ResultadoMedico;
import com.uniquindio.edu.back.model.dto.ResultadoMedicoDTO;
import com.uniquindio.edu.back.repository.ResultadoMedicoRepository;
import com.uniquindio.edu.back.service.ResultadoMedicoService;

@ExtendWith(MockitoExtension.class)
class ResultadoMedicoServiceTest {

    @Mock
    private ResultadoMedicoRepository resultadoMedicoRepository;

    @Mock
    private ResultadoMedicoMapper resultadoMedicoMapper;

    @InjectMocks
    private ResultadoMedicoService resultadoMedicoService;

    private ResultadoMedico resultadoMedico;
    private ResultadoMedicoDTO resultadoMedicoDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime fechaExamen = LocalDateTime.now().minusDays(1);
        LocalDateTime fechaEmision = LocalDateTime.now();

        resultadoMedico = new ResultadoMedico(
                1L, "Juan Pérez", "Hemograma Completo", 
                "Hemoglobina: 14.2 g/dL (Normal)", 
                "Dr. María González", fechaExamen, fechaEmision, 
                "Resultados normales", "COMPLETADO");

        resultadoMedicoDTO = new ResultadoMedicoDTO(
                1L, "Juan Pérez", "Hemograma Completo", 
                "Hemoglobina: 14.2 g/dL (Normal)", 
                "Dr. María González", fechaExamen, fechaEmision, 
                "Resultados normales", "COMPLETADO");
    }

    @Test
    void crearResultadoMedico_DeberiaCrearYRetornarResultadoMedico() {
        // Arrange
        when(resultadoMedicoMapper.toEntity(any(ResultadoMedicoDTO.class))).thenReturn(resultadoMedico);
        when(resultadoMedicoRepository.save(any(ResultadoMedico.class))).thenReturn(resultadoMedico);
        when(resultadoMedicoMapper.toDTO(any(ResultadoMedico.class))).thenReturn(resultadoMedicoDTO);

        // Act
        ResultadoMedicoDTO resultado = resultadoMedicoService.crearResultadoMedico(resultadoMedicoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getPaciente());
        assertEquals("Hemograma Completo", resultado.getTipoExamen());
        verify(resultadoMedicoRepository, times(1)).save(any(ResultadoMedico.class));
    }

    @Test
    void listarResultadosMedicos_DeberiaRetornarListaDeResultados() {
        // Arrange
        List<ResultadoMedico> resultados = Arrays.asList(resultadoMedico);
        when(resultadoMedicoRepository.findAll()).thenReturn(resultados);
        when(resultadoMedicoMapper.toDTO(any(ResultadoMedico.class))).thenReturn(resultadoMedicoDTO);

        // Act
        List<ResultadoMedicoDTO> resultado = resultadoMedicoService.listarResultadosMedicos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getPaciente());
    }

    @Test
    void obtenerResultadoMedico_ConIdExistente_DeberiaRetornarResultado() {
        // Arrange
        when(resultadoMedicoRepository.findById(1L)).thenReturn(Optional.of(resultadoMedico));
        when(resultadoMedicoMapper.toDTO(any(ResultadoMedico.class))).thenReturn(resultadoMedicoDTO);

        // Act
        Optional<ResultadoMedicoDTO> resultado = resultadoMedicoService.obtenerResultadoMedico(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getPaciente());
    }

    @Test
    void obtenerResultadoMedico_ConIdInexistente_DeberiaRetornarVacio() {
        // Arrange
        when(resultadoMedicoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<ResultadoMedicoDTO> resultado = resultadoMedicoService.obtenerResultadoMedico(999L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void actualizarResultadoMedico_ConIdExistente_DeberiaActualizarYRetornarResultado() {
        // Arrange
        ResultadoMedicoDTO dtoActualizado = new ResultadoMedicoDTO();
        dtoActualizado.setPaciente("Juan Carlos Pérez");
        dtoActualizado.setTipoExamen("Hemograma Completo");
        dtoActualizado.setResultados("Hemoglobina: 15.0 g/dL (Normal)");
        dtoActualizado.setMedicoResponsable("Dr. María González");
        dtoActualizado.setFechaExamen(LocalDateTime.now().minusDays(1));
        dtoActualizado.setFechaEmision(LocalDateTime.now());
        dtoActualizado.setObservaciones("Resultados actualizados");
        dtoActualizado.setEstado("COMPLETADO");

        when(resultadoMedicoRepository.findById(1L)).thenReturn(Optional.of(resultadoMedico));
        when(resultadoMedicoRepository.save(any(ResultadoMedico.class))).thenReturn(resultadoMedico);
        when(resultadoMedicoMapper.toDTO(any(ResultadoMedico.class))).thenReturn(dtoActualizado);

        // Act
        ResultadoMedicoDTO resultado = resultadoMedicoService.actualizarResultadoMedico(1L, dtoActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Carlos Pérez", resultado.getPaciente());
        verify(resultadoMedicoRepository, times(1)).save(any(ResultadoMedico.class));
    }

    @Test
    void actualizarResultadoMedico_ConIdInexistente_DeberiaLanzarExcepcion() {
        // Arrange
        when(resultadoMedicoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            resultadoMedicoService.actualizarResultadoMedico(999L, resultadoMedicoDTO);
        });
        assertNotNull(exception);
    }

    @Test
    void eliminarResultadoMedico_ConIdExistente_DeberiaEliminarYRetornarTrue() {
        // Arrange
        when(resultadoMedicoRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean resultado = resultadoMedicoService.eliminarResultadoMedico(1L);

        // Assert
        assertTrue(resultado);
        verify(resultadoMedicoRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarResultadoMedico_ConIdInexistente_DeberiaRetornarFalse() {
        // Arrange
        when(resultadoMedicoRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean resultado = resultadoMedicoService.eliminarResultadoMedico(999L);

        // Assert
        assertFalse(resultado);
        verify(resultadoMedicoRepository, never()).deleteById(any());
    }

    @Test
    void buscarPorPaciente_DeberiaRetornarResultadosDelPaciente() {
        // Arrange
        List<ResultadoMedico> resultados = Arrays.asList(resultadoMedico);
        when(resultadoMedicoRepository.findByPacienteContainingIgnoreCase("Juan")).thenReturn(resultados);
        when(resultadoMedicoMapper.toDTO(any(ResultadoMedico.class))).thenReturn(resultadoMedicoDTO);

        // Act
        List<ResultadoMedicoDTO> resultado = resultadoMedicoService.buscarPorPaciente("Juan");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getPaciente());
    }

    @Test
    void buscarPorEstado_DeberiaRetornarResultadosConEstadoEspecifico() {
        // Arrange
        List<ResultadoMedico> resultados = Arrays.asList(resultadoMedico);
        when(resultadoMedicoRepository.findByEstado("COMPLETADO")).thenReturn(resultados);
        when(resultadoMedicoMapper.toDTO(any(ResultadoMedico.class))).thenReturn(resultadoMedicoDTO);

        // Act
        List<ResultadoMedicoDTO> resultado = resultadoMedicoService.buscarPorEstado("COMPLETADO");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("COMPLETADO", resultado.get(0).getEstado());
    }

    @Test
    void cambiarEstado_ConIdExistente_DeberiaCambiarEstadoYRetornarResultado() {
        // Arrange
        when(resultadoMedicoRepository.findById(1L)).thenReturn(Optional.of(resultadoMedico));
        when(resultadoMedicoRepository.save(any(ResultadoMedico.class))).thenReturn(resultadoMedico);
        
        ResultadoMedicoDTO dtoConNuevoEstado = new ResultadoMedicoDTO();
        dtoConNuevoEstado.setEstado("REVISADO");
        when(resultadoMedicoMapper.toDTO(any(ResultadoMedico.class))).thenReturn(dtoConNuevoEstado);

        // Act
        ResultadoMedicoDTO resultado = resultadoMedicoService.cambiarEstado(1L, "REVISADO");

        // Assert
        assertNotNull(resultado);
        assertEquals("REVISADO", resultado.getEstado());
        verify(resultadoMedicoRepository, times(1)).save(any(ResultadoMedico.class));
    }

    @Test
    void cambiarEstado_ConIdInexistente_DeberiaLanzarExcepcion() {
        // Arrange
        when(resultadoMedicoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            resultadoMedicoService.cambiarEstado(999L, "REVISADO");
        });
        assertNotNull(exception);
    }
}
