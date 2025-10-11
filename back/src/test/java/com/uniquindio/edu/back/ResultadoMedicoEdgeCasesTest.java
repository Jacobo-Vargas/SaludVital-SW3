package com.uniquindio.edu.back;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.edu.back.model.ResultadoMedico;
import com.uniquindio.edu.back.model.dto.ResultadoMedicoDTO;
import com.uniquindio.edu.back.repository.ResultadoMedicoRepository;
import com.uniquindio.edu.back.service.ResultadoMedicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class ResultadoMedicoEdgeCasesTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ResultadoMedicoRepository resultadoMedicoRepository;

    @Autowired
    private ResultadoMedicoService resultadoMedicoService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        resultadoMedicoRepository.deleteAll();
    }

    @Test
    void crearResultadoMedico_ConEstadoNulo_DeberiaEstablecerEstadoPorDefecto() {
        // Arrange
        ResultadoMedicoDTO dto = new ResultadoMedicoDTO();
        dto.setPaciente("Test Paciente");
        dto.setTipoExamen("Test Examen");
        dto.setResultados("Resultados de prueba");
        dto.setMedicoResponsable("Dr. Test");
        dto.setFechaExamen(LocalDateTime.now().minusDays(1));
        dto.setFechaEmision(LocalDateTime.now());
        dto.setObservaciones("Observaciones de prueba");
        dto.setEstado(null); // Estado nulo

        // Act
        ResultadoMedicoDTO resultado = resultadoMedicoService.crearResultadoMedico(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
    }

    @Test
    void crearResultadoMedico_ConEstadoVacio_DeberiaEstablecerEstadoPorDefecto() {
        // Arrange
        ResultadoMedicoDTO dto = new ResultadoMedicoDTO();
        dto.setPaciente("Test Paciente");
        dto.setTipoExamen("Test Examen");
        dto.setResultados("Resultados de prueba");
        dto.setMedicoResponsable("Dr. Test");
        dto.setFechaExamen(LocalDateTime.now().minusDays(1));
        dto.setFechaEmision(LocalDateTime.now());
        dto.setObservaciones("Observaciones de prueba");
        dto.setEstado(""); // Estado vacío

        // Act
        ResultadoMedicoDTO resultado = resultadoMedicoService.crearResultadoMedico(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
    }

    @Test
    void crearResultadoMedico_ConFechaEmisionNula_DeberiaEstablecerFechaActual() {
        // Arrange
        LocalDateTime antesDeCrear = LocalDateTime.now();
        
        ResultadoMedicoDTO dto = new ResultadoMedicoDTO();
        dto.setPaciente("Test Paciente");
        dto.setTipoExamen("Test Examen");
        dto.setResultados("Resultados de prueba");
        dto.setMedicoResponsable("Dr. Test");
        dto.setFechaExamen(LocalDateTime.now().minusDays(1));
        dto.setFechaEmision(null); // Fecha de emisión nula
        dto.setObservaciones("Observaciones de prueba");
        dto.setEstado("COMPLETADO");

        // Act
        ResultadoMedicoDTO resultado = resultadoMedicoService.crearResultadoMedico(dto);
        
        LocalDateTime despuesDeCrear = LocalDateTime.now();

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getFechaEmision());
        assertTrue(resultado.getFechaEmision().isAfter(antesDeCrear) || 
                  resultado.getFechaEmision().isEqual(antesDeCrear));
        assertTrue(resultado.getFechaEmision().isBefore(despuesDeCrear) || 
                  resultado.getFechaEmision().isEqual(despuesDeCrear));
    }

    @Test
    void actualizarResultadoMedico_ConIdInexistente_DeberiaLanzarExcepcion() {
        // Arrange
        ResultadoMedicoDTO dto = new ResultadoMedicoDTO();
        dto.setPaciente("Test Paciente");
        dto.setTipoExamen("Test Examen");
        dto.setResultados("Resultados de prueba");
        dto.setMedicoResponsable("Dr. Test");
        dto.setFechaExamen(LocalDateTime.now().minusDays(1));
        dto.setFechaEmision(LocalDateTime.now());
        dto.setObservaciones("Observaciones de prueba");
        dto.setEstado("COMPLETADO");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            resultadoMedicoService.actualizarResultadoMedico(999L, dto);
        });
    }

    @Test
    void cambiarEstado_ConIdInexistente_DeberiaLanzarExcepcion() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            resultadoMedicoService.cambiarEstado(999L, "REVISADO");
        });
    }

    @Test
    void buscarPorPaciente_ConTextoMuyLargo_DeberiaManejarCorrectamente() {
        // Arrange
        String textoMuyLargo = "A".repeat(1000);
        
        // Act
        List<ResultadoMedicoDTO> resultados = resultadoMedicoService.buscarPorPaciente(textoMuyLargo);

        // Assert
        assertNotNull(resultados);
        assertTrue(resultados.isEmpty());
    }

    @Test
    void buscarPorTipoExamen_ConTextoMuyLargo_DeberiaManejarCorrectamente() {
        // Arrange
        String textoMuyLargo = "B".repeat(1000);
        
        // Act
        List<ResultadoMedicoDTO> resultados = resultadoMedicoService.buscarPorTipoExamen(textoMuyLargo);

        // Assert
        assertNotNull(resultados);
        assertTrue(resultados.isEmpty());
    }

    @Test
    void crearResultadoMedico_ConCamposMuyLargos_DeberiaManejarCorrectamente() throws Exception {
        // Arrange - Usar texto que exceda el límite de la base de datos (255 caracteres)
        String textoMuyLargo = "X".repeat(300); // Excede el límite de 255 caracteres
        
        ResultadoMedicoDTO dto = new ResultadoMedicoDTO();
        dto.setPaciente(textoMuyLargo);
        dto.setTipoExamen(textoMuyLargo);
        dto.setResultados(textoMuyLargo);
        dto.setMedicoResponsable(textoMuyLargo);
        dto.setFechaExamen(LocalDateTime.now().minusDays(1));
        dto.setFechaEmision(LocalDateTime.now());
        dto.setObservaciones(textoMuyLargo);
        dto.setEstado("COMPLETADO");

        // Act & Assert - Debería fallar con error 500 por violación de integridad de datos
        mockMvc.perform(post("/api/resultados-medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void crearResultadoMedico_ConCaracteresEspeciales_DeberiaManejarCorrectamente() throws Exception {
        // Arrange
        ResultadoMedicoDTO dto = new ResultadoMedicoDTO();
        dto.setPaciente("José María O'Connor-García");
        dto.setTipoExamen("Análisis de Sangre (CBC) - Completo");
        dto.setResultados("Hemoglobina: 14.2 g/dL (Normal), Hematocrito: 42% (Normal), Leucocitos: 7,500/μL (Normal)");
        dto.setMedicoResponsable("Dr. María José González-Pérez");
        dto.setFechaExamen(LocalDateTime.now().minusDays(1));
        dto.setFechaEmision(LocalDateTime.now());
        dto.setObservaciones("Resultados dentro de parámetros normales. ¡Excelente!");
        dto.setEstado("COMPLETADO");

        // Act & Assert
        mockMvc.perform(post("/api/resultados-medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paciente").value("José María O'Connor-García"))
                .andExpect(jsonPath("$.tipoExamen").value("Análisis de Sangre (CBC) - Completo"))
                .andExpect(jsonPath("$.medicoResponsable").value("Dr. María José González-Pérez"));
    }

    @Test
    void crearResultadoMedico_ConFechasEnElLimite_DeberiaManejarCorrectamente() throws Exception {
        // Arrange
        LocalDateTime fechaLimite = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        
        ResultadoMedicoDTO dto = new ResultadoMedicoDTO();
        dto.setPaciente("Test Paciente");
        dto.setTipoExamen("Test Examen");
        dto.setResultados("Resultados de prueba");
        dto.setMedicoResponsable("Dr. Test");
        dto.setFechaExamen(fechaLimite);
        dto.setFechaEmision(fechaLimite);
        dto.setObservaciones("Observaciones de prueba");
        dto.setEstado("COMPLETADO");

        // Act & Assert
        mockMvc.perform(post("/api/resultados-medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void obtenerResultadoMedico_ConIdNegativo_DeberiaRetornarVacio() {
        // Act
        Optional<ResultadoMedicoDTO> resultado = resultadoMedicoService.obtenerResultadoMedico(-1L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void eliminarResultadoMedico_ConIdNegativo_DeberiaRetornarFalse() {
        // Act
        boolean resultado = resultadoMedicoService.eliminarResultadoMedico(-1L);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void actualizarResultadoMedico_ConIdNegativo_DeberiaLanzarExcepcion() {
        // Arrange
        ResultadoMedicoDTO dto = new ResultadoMedicoDTO();
        dto.setPaciente("Test Paciente");
        dto.setTipoExamen("Test Examen");
        dto.setResultados("Resultados de prueba");
        dto.setMedicoResponsable("Dr. Test");
        dto.setFechaExamen(LocalDateTime.now().minusDays(1));
        dto.setFechaEmision(LocalDateTime.now());
        dto.setObservaciones("Observaciones de prueba");
        dto.setEstado("COMPLETADO");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            resultadoMedicoService.actualizarResultadoMedico(-1L, dto);
        });
    }

    @Test
    void cambiarEstado_ConIdNegativo_DeberiaLanzarExcepcion() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            resultadoMedicoService.cambiarEstado(-1L, "REVISADO");
        });
    }

    @Test
    void crearResultadoMedico_ConJSONMalformado_DeberiaRetornar400() throws Exception {
        // Arrange
        String jsonMalformado = "{ \"paciente\": \"Test Paciente\", \"tipoExamen\": }";

        // Act & Assert
        mockMvc.perform(post("/api/resultados-medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMalformado))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarResultadoMedico_ConJSONMalformado_DeberiaRetornar400() throws Exception {
        // Arrange
        String jsonMalformado = "{ \"paciente\": \"Test Paciente\", \"tipoExamen\": }";

        // Act & Assert
        mockMvc.perform(put("/api/resultados-medicos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMalformado))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearResultadoMedico_ConContentTypeIncorrecto_DeberiaRetornar415() throws Exception {
        // Arrange
        ResultadoMedicoDTO dto = new ResultadoMedicoDTO();
        dto.setPaciente("Test Paciente");
        dto.setTipoExamen("Test Examen");
        dto.setResultados("Resultados de prueba");
        dto.setMedicoResponsable("Dr. Test");
        dto.setFechaExamen(LocalDateTime.now().minusDays(1));
        dto.setFechaEmision(LocalDateTime.now());
        dto.setObservaciones("Observaciones de prueba");
        dto.setEstado("COMPLETADO");

        // Act & Assert
        mockMvc.perform(post("/api/resultados-medicos")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("texto plano"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void buscarResultados_ConParametrosVacios_DeberiaManejarCorrectamente() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/buscar/paciente")
                        .param("paciente", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(get("/api/resultados-medicos/buscar/tipo-examen")
                        .param("tipoExamen", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(get("/api/resultados-medicos/buscar/medico")
                        .param("medicoResponsable", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(get("/api/resultados-medicos/buscar/estado")
                        .param("estado", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void cambiarEstado_ConEstadoInvalido_DeberiaManejarCorrectamente() throws Exception {
        // Arrange
        ResultadoMedico resultado = crearResultadoDePrueba();
        Long id = resultado.getId();

        // Act & Assert
        mockMvc.perform(put("/api/resultados-medicos/{id}/cambiar-estado", id)
                        .param("estado", "ESTADO_INEXISTENTE"))
                .andExpect(status().isOk()); // El servicio acepta cualquier estado
    }

    @Test
    void operacionesConBaseDeDatosVacia_DeberiaManejarCorrectamente() {
        // Act
        List<ResultadoMedicoDTO> todosLosResultados = resultadoMedicoService.listarResultadosMedicos();
        List<ResultadoMedicoDTO> pendientes = resultadoMedicoService.buscarResultadosPendientes();
        List<ResultadoMedicoDTO> recientes = resultadoMedicoService.buscarResultadosRecientes();

        // Assert
        assertNotNull(todosLosResultados);
        assertTrue(todosLosResultados.isEmpty());
        
        assertNotNull(pendientes);
        assertTrue(pendientes.isEmpty());
        
        assertNotNull(recientes);
        assertTrue(recientes.isEmpty());
    }

    // Método auxiliar
    private ResultadoMedico crearResultadoDePrueba() {
        ResultadoMedico resultado = new ResultadoMedico();
        resultado.setPaciente("Test Paciente");
        resultado.setTipoExamen("Test Examen");
        resultado.setResultados("Resultados de prueba");
        resultado.setMedicoResponsable("Dr. Test");
        resultado.setFechaExamen(LocalDateTime.now().minusDays(1));
        resultado.setFechaEmision(LocalDateTime.now());
        resultado.setObservaciones("Observaciones de prueba");
        resultado.setEstado("PENDIENTE");
        
        return resultadoMedicoRepository.save(resultado);
    }
}
