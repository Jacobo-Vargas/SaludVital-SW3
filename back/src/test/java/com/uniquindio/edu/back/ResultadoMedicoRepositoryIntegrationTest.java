package com.uniquindio.edu.back;

import com.uniquindio.edu.back.model.ResultadoMedico;
import com.uniquindio.edu.back.repository.ResultadoMedicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ResultadoMedicoRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ResultadoMedicoRepository resultadoMedicoRepository;

    private ResultadoMedico resultado1;
    private ResultadoMedico resultado2;
    private ResultadoMedico resultado3;
    private ResultadoMedico resultado4;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada test
        resultadoMedicoRepository.deleteAll();
        
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime haceUnaSemana = ahora.minusDays(7);
        LocalDateTime haceDosDias = ahora.minusDays(2);
        LocalDateTime ayer = ahora.minusDays(1);

        // Crear datos de prueba
        resultado1 = new ResultadoMedico(
                null, "Juan Pérez", "Hemograma Completo", 
                "Hemoglobina: 14.2 g/dL (Normal)", 
                "Dr. María González", haceUnaSemana, ayer, 
                "Resultados normales", "COMPLETADO");

        resultado2 = new ResultadoMedico(
                null, "Ana María", "Electrocardiograma", 
                "Ritmo sinusal regular", 
                "Dr. Carlos Mendoza", haceDosDias, ayer, 
                "ECG normal", "COMPLETADO");

        resultado3 = new ResultadoMedico(
                null, "Pedro López", "Radiografía de Tórax", 
                "Campos pulmonares claros", 
                "Dr. Ana Rodríguez", ayer, ahora, 
                "Radiografía normal", "PENDIENTE");

        resultado4 = new ResultadoMedico(
                null, "Juan Carlos", "Resonancia Magnética", 
                "Sin alteraciones", 
                "Dr. Luis Fernández", haceUnaSemana, haceDosDias, 
                "RMN normal", "REVISADO");

        // Persistir en la base de datos
        entityManager.persistAndFlush(resultado1);
        entityManager.persistAndFlush(resultado2);
        entityManager.persistAndFlush(resultado3);
        entityManager.persistAndFlush(resultado4);
    }

    @Test
    void findByPacienteContainingIgnoreCase_DeberiaEncontrarResultadosPorPaciente() {
        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findByPacienteContainingIgnoreCase("Juan");

        // Assert
        assertEquals(2, resultados.size());
        assertTrue(resultados.stream().anyMatch(r -> r.getPaciente().equals("Juan Pérez")));
        assertTrue(resultados.stream().anyMatch(r -> r.getPaciente().equals("Juan Carlos")));
    }

    @Test
    void findByPacienteContainingIgnoreCase_ConTextoInexistente_DeberiaRetornarListaVacia() {
        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findByPacienteContainingIgnoreCase("Inexistente");

        // Assert
        assertTrue(resultados.isEmpty());
    }

    @Test
    void findByTipoExamenContainingIgnoreCase_DeberiaEncontrarResultadosPorTipoExamen() {
        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findByTipoExamenContainingIgnoreCase("Hemograma");

        // Assert
        assertEquals(1, resultados.size());
        assertEquals("Juan Pérez", resultados.get(0).getPaciente());
        assertEquals("Hemograma Completo", resultados.get(0).getTipoExamen());
    }

    @Test
    void findByMedicoResponsableContainingIgnoreCase_DeberiaEncontrarResultadosPorMedico() {
        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findByMedicoResponsableContainingIgnoreCase("Dr. María");

        // Assert
        assertEquals(1, resultados.size());
        assertEquals("Dr. María González", resultados.get(0).getMedicoResponsable());
        assertEquals("Juan Pérez", resultados.get(0).getPaciente());
    }

    @Test
    void findByEstado_DeberiaEncontrarResultadosPorEstado() {
        // Act
        List<ResultadoMedico> resultadosCompletados = resultadoMedicoRepository.findByEstado("COMPLETADO");
        List<ResultadoMedico> resultadosPendientes = resultadoMedicoRepository.findByEstado("PENDIENTE");
        List<ResultadoMedico> resultadosRevisados = resultadoMedicoRepository.findByEstado("REVISADO");

        // Assert
        assertEquals(2, resultadosCompletados.size());
        assertEquals(1, resultadosPendientes.size());
        assertEquals(1, resultadosRevisados.size());
        
        assertTrue(resultadosCompletados.stream().allMatch(r -> r.getEstado().equals("COMPLETADO")));
        assertTrue(resultadosPendientes.stream().allMatch(r -> r.getEstado().equals("PENDIENTE")));
        assertTrue(resultadosRevisados.stream().allMatch(r -> r.getEstado().equals("REVISADO")));
    }

    @Test
    void findByFechaExamenBetween_DeberiaEncontrarResultadosEnRangoDeFechas() {
        // Arrange
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(3);
        LocalDateTime fechaFin = LocalDateTime.now();

        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findByFechaExamenBetween(fechaInicio, fechaFin);

        // Assert
        assertEquals(1, resultados.size());
        assertEquals("Pedro López", resultados.get(0).getPaciente());
    }

    @Test
    void findByPacienteContainingIgnoreCaseAndTipoExamenContainingIgnoreCase_DeberiaEncontrarResultadosCombinados() {
        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository
                .findByPacienteContainingIgnoreCaseAndTipoExamenContainingIgnoreCase("Juan", "Hemograma");

        // Assert
        assertEquals(1, resultados.size());
        assertEquals("Juan Pérez", resultados.get(0).getPaciente());
        assertEquals("Hemograma Completo", resultados.get(0).getTipoExamen());
    }

    @Test
    void findResultadosPendientes_DeberiaRetornarSoloResultadosPendientesOrdenados() {
        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findResultadosPendientes();

        // Assert
        assertEquals(1, resultados.size());
        assertEquals("PENDIENTE", resultados.get(0).getEstado());
        assertEquals("Pedro López", resultados.get(0).getPaciente());
    }

    @Test
    void findResultadosRecientes_DeberiaRetornarResultadosDeLosUltimos30Dias() {
        // Arrange
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(30);

        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findResultadosRecientes(fechaInicio);

        // Assert
        assertEquals(4, resultados.size());
        // Verificar que están ordenados por fecha de emisión descendente
        for (int i = 0; i < resultados.size() - 1; i++) {
            assertTrue(resultados.get(i).getFechaEmision()
                    .isAfter(resultados.get(i + 1).getFechaEmision()) ||
                    resultados.get(i).getFechaEmision()
                            .isEqual(resultados.get(i + 1).getFechaEmision()));
        }
    }

    @Test
    void save_DeberiaPersistirNuevoResultadoMedico() {
        // Arrange
        ResultadoMedico nuevoResultado = new ResultadoMedico(
                null, "Nuevo Paciente", "Nuevo Examen", 
                "Nuevos resultados", 
                "Dr. Nuevo", LocalDateTime.now(), LocalDateTime.now(), 
                "Nuevas observaciones", "PENDIENTE");

        // Act
        ResultadoMedico resultadoGuardado = resultadoMedicoRepository.save(nuevoResultado);
        entityManager.flush();

        // Assert
        assertNotNull(resultadoGuardado.getId());
        assertEquals("Nuevo Paciente", resultadoGuardado.getPaciente());
        assertEquals("Nuevo Examen", resultadoGuardado.getTipoExamen());
        
        // Verificar que se guardó en la base de datos
        ResultadoMedico resultadoEncontrado = entityManager.find(ResultadoMedico.class, resultadoGuardado.getId());
        assertNotNull(resultadoEncontrado);
        assertEquals("Nuevo Paciente", resultadoEncontrado.getPaciente());
    }

    @Test
    void deleteById_DeberiaEliminarResultadoMedico() {
        // Arrange
        Long idAEliminar = resultado1.getId();

        // Act
        resultadoMedicoRepository.deleteById(idAEliminar);
        entityManager.flush();

        // Assert
        ResultadoMedico resultadoEliminado = entityManager.find(ResultadoMedico.class, idAEliminar);
        assertNull(resultadoEliminado);
    }

    @Test
    void findAll_DeberiaRetornarTodosLosResultados() {
        // Act
        List<ResultadoMedico> todosLosResultados = resultadoMedicoRepository.findAll();

        // Assert
        assertEquals(4, todosLosResultados.size());
    }

    @Test
    void findById_DeberiaRetornarResultadoEspecifico() {
        // Act
        var resultadoEncontrado = resultadoMedicoRepository.findById(resultado1.getId());

        // Assert
        assertTrue(resultadoEncontrado.isPresent());
        assertEquals("Juan Pérez", resultadoEncontrado.get().getPaciente());
        assertEquals("Hemograma Completo", resultadoEncontrado.get().getTipoExamen());
    }

    @Test
    void findById_ConIdInexistente_DeberiaRetornarVacio() {
        // Act
        var resultadoEncontrado = resultadoMedicoRepository.findById(999L);

        // Assert
        assertFalse(resultadoEncontrado.isPresent());
    }
}
