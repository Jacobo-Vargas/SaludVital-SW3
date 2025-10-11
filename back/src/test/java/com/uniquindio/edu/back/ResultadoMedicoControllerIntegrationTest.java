package com.uniquindio.edu.back;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.edu.back.model.ResultadoMedico;
import com.uniquindio.edu.back.model.dto.ResultadoMedicoDTO;
import com.uniquindio.edu.back.repository.ResultadoMedicoRepository;
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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class ResultadoMedicoControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ResultadoMedicoRepository resultadoMedicoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        resultadoMedicoRepository.deleteAll();
    }

    @Test
    void crearResultadoMedico_DeberiaCrearYRetornar201() throws Exception {
        // Arrange
        ResultadoMedicoDTO nuevoResultado = new ResultadoMedicoDTO();
        nuevoResultado.setPaciente("Test Paciente");
        nuevoResultado.setTipoExamen("Test Examen");
        nuevoResultado.setResultados("Resultados de prueba");
        nuevoResultado.setMedicoResponsable("Dr. Test");
        nuevoResultado.setFechaExamen(LocalDateTime.now().minusDays(1));
        nuevoResultado.setFechaEmision(LocalDateTime.now());
        nuevoResultado.setObservaciones("Observaciones de prueba");
        nuevoResultado.setEstado("PENDIENTE");

        // Act & Assert
        mockMvc.perform(post("/api/resultados-medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoResultado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.paciente").value("Test Paciente"))
                .andExpect(jsonPath("$.tipoExamen").value("Test Examen"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        // Verificar que se guardó en la base de datos
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findAll();
        assertEquals(1, resultados.size());
        assertEquals("Test Paciente", resultados.get(0).getPaciente());
    }

    @Test
    void crearResultadoMedico_ConDatosInvalidos_DeberiaRetornar400() throws Exception {
        // Arrange - DTO con datos inválidos (campos obligatorios faltantes)
        ResultadoMedicoDTO resultadoInvalido = new ResultadoMedicoDTO();
        // No se establecen campos obligatorios

        // Act & Assert
        mockMvc.perform(post("/api/resultados-medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resultadoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarResultadosMedicos_ConDatosExistentes_DeberiaRetornarLista() throws Exception {
        // Arrange
        crearDatosDePrueba();

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].paciente").exists())
                .andExpect(jsonPath("$[0].tipoExamen").exists());
    }

    @Test
    void listarResultadosMedicos_SinDatos_DeberiaRetornarListaVacia() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void obtenerResultadoMedico_ConIdExistente_DeberiaRetornarResultado() throws Exception {
        // Arrange
        ResultadoMedico resultado = crearResultadoDePrueba();
        Long id = resultado.getId();

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.paciente").value("Test Paciente"))
                .andExpect(jsonPath("$.tipoExamen").value("Test Examen"));
    }

    @Test
    void obtenerResultadoMedico_ConIdInexistente_DeberiaRetornar404() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarResultadoMedico_ConIdExistente_DeberiaActualizarYRetornar200() throws Exception {
        // Arrange
        ResultadoMedico resultado = crearResultadoDePrueba();
        Long id = resultado.getId();

        ResultadoMedicoDTO dtoActualizado = new ResultadoMedicoDTO();
        dtoActualizado.setPaciente("Paciente Actualizado");
        dtoActualizado.setTipoExamen("Examen Actualizado");
        dtoActualizado.setResultados("Resultados actualizados");
        dtoActualizado.setMedicoResponsable("Dr. Actualizado");
        dtoActualizado.setFechaExamen(LocalDateTime.now().minusDays(1));
        dtoActualizado.setFechaEmision(LocalDateTime.now());
        dtoActualizado.setObservaciones("Observaciones actualizadas");
        dtoActualizado.setEstado("COMPLETADO");

        // Act & Assert
        mockMvc.perform(put("/api/resultados-medicos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.paciente").value("Paciente Actualizado"))
                .andExpect(jsonPath("$.estado").value("COMPLETADO"));

        // Verificar que se actualizó en la base de datos
        ResultadoMedico resultadoActualizado = resultadoMedicoRepository.findById(id).orElse(null);
        assertNotNull(resultadoActualizado);
        assertEquals("Paciente Actualizado", resultadoActualizado.getPaciente());
        assertEquals("COMPLETADO", resultadoActualizado.getEstado());
    }

    @Test
    void eliminarResultadoMedico_ConIdExistente_DeberiaEliminarYRetornar204() throws Exception {
        // Arrange
        ResultadoMedico resultado = crearResultadoDePrueba();
        Long id = resultado.getId();

        // Act & Assert
        mockMvc.perform(delete("/api/resultados-medicos/{id}", id))
                .andExpect(status().isNoContent());

        // Verificar que se eliminó de la base de datos
        assertFalse(resultadoMedicoRepository.existsById(id));
    }

    @Test
    void eliminarResultadoMedico_ConIdInexistente_DeberiaRetornar404() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/resultados-medicos/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorPaciente_DeberiaRetornarResultadosDelPaciente() throws Exception {
        // Arrange
        crearDatosDePrueba();

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/buscar/paciente")
                        .param("paciente", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].paciente", everyItem(containsString("Juan"))));
    }

    @Test
    void buscarPorTipoExamen_DeberiaRetornarResultadosDelTipo() throws Exception {
        // Arrange
        crearDatosDePrueba();

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/buscar/tipo-examen")
                        .param("tipoExamen", "Hemograma"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tipoExamen").value("Hemograma Completo"));
    }

    @Test
    void buscarPorMedicoResponsable_DeberiaRetornarResultadosDelMedico() throws Exception {
        // Arrange
        crearDatosDePrueba();

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/buscar/medico")
                        .param("medicoResponsable", "Dr. María"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].medicoResponsable").value("Dr. María González"));
    }

    @Test
    void buscarPorEstado_DeberiaRetornarResultadosConEstado() throws Exception {
        // Arrange
        crearDatosDePrueba();

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/buscar/estado")
                        .param("estado", "COMPLETADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].estado", everyItem(is("COMPLETADO"))));
    }

    @Test
    void buscarResultadosPendientes_DeberiaRetornarSoloResultadosPendientes() throws Exception {
        // Arrange
        crearDatosDePrueba();

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/pendientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    void buscarResultadosRecientes_DeberiaRetornarResultadosRecientes() throws Exception {
        // Arrange
        crearDatosDePrueba();

        // Act & Assert
        mockMvc.perform(get("/api/resultados-medicos/recientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void cambiarEstado_DeberiaCambiarEstadoYRetornar200() throws Exception {
        // Arrange
        ResultadoMedico resultado = crearResultadoDePrueba();
        Long id = resultado.getId();

        // Act & Assert
        mockMvc.perform(put("/api/resultados-medicos/{id}/cambiar-estado", id)
                        .param("estado", "REVISADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("REVISADO"));

        // Verificar que se actualizó en la base de datos
        ResultadoMedico resultadoActualizado = resultadoMedicoRepository.findById(id).orElse(null);
        assertNotNull(resultadoActualizado);
        assertEquals("REVISADO", resultadoActualizado.getEstado());
    }

    @Test
    void cambiarEstado_ConIdInexistente_DeberiaRetornarError() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/resultados-medicos/{id}/cambiar-estado", 999L)
                        .param("estado", "REVISADO"))
                .andExpect(status().isInternalServerError());
    }

    // Métodos auxiliares
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

    private void crearDatosDePrueba() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime haceUnaSemana = ahora.minusDays(7);
        LocalDateTime haceDosDias = ahora.minusDays(2);
        LocalDateTime ayer = ahora.minusDays(1);

        // Resultado 1
        ResultadoMedico resultado1 = new ResultadoMedico();
        resultado1.setPaciente("Juan Pérez");
        resultado1.setTipoExamen("Hemograma Completo");
        resultado1.setResultados("Hemoglobina: 14.2 g/dL (Normal)");
        resultado1.setMedicoResponsable("Dr. María González");
        resultado1.setFechaExamen(haceUnaSemana);
        resultado1.setFechaEmision(ayer);
        resultado1.setObservaciones("Resultados normales");
        resultado1.setEstado("COMPLETADO");
        resultadoMedicoRepository.save(resultado1);

        // Resultado 2
        ResultadoMedico resultado2 = new ResultadoMedico();
        resultado2.setPaciente("Ana María");
        resultado2.setTipoExamen("Electrocardiograma");
        resultado2.setResultados("Ritmo sinusal regular");
        resultado2.setMedicoResponsable("Dr. Carlos Mendoza");
        resultado2.setFechaExamen(haceDosDias);
        resultado2.setFechaEmision(ayer);
        resultado2.setObservaciones("ECG normal");
        resultado2.setEstado("COMPLETADO");
        resultadoMedicoRepository.save(resultado2);

        // Resultado 3
        ResultadoMedico resultado3 = new ResultadoMedico();
        resultado3.setPaciente("Juan Carlos");
        resultado3.setTipoExamen("Radiografía de Tórax");
        resultado3.setResultados("Campos pulmonares claros");
        resultado3.setMedicoResponsable("Dr. Ana Rodríguez");
        resultado3.setFechaExamen(ayer);
        resultado3.setFechaEmision(ahora);
        resultado3.setObservaciones("Radiografía normal");
        resultado3.setEstado("PENDIENTE");
        resultadoMedicoRepository.save(resultado3);
    }
}
