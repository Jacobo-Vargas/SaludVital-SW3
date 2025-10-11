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
class ResultadoMedicoCustomQueriesTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ResultadoMedicoRepository resultadoMedicoRepository;

    private ResultadoMedico resultado1;
    private ResultadoMedico resultado2;
    private ResultadoMedico resultado3;
    private ResultadoMedico resultado4;
    private ResultadoMedico resultado5;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada test
        resultadoMedicoRepository.deleteAll();
        
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime haceUnaSemana = ahora.minusDays(7);
        LocalDateTime haceDosDias = ahora.minusDays(2);
        LocalDateTime ayer = ahora.minusDays(1);
        LocalDateTime haceTresDias = ahora.minusDays(3);

        // Crear datos de prueba con diferentes estados y fechas
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
                null, "Carlos Ramírez", "Resonancia Magnética Cerebral", 
                "Sin evidencia de lesiones", 
                "Dr. Luis Fernández", haceUnaSemana, haceDosDias, 
                "RMN normal", "REVISADO");

        resultado5 = new ResultadoMedico(
                null, "Laura Gómez", "Perfil Lipídico", 
                "Colesterol: 180 mg/dL (Normal)", 
                "Dr. Patricia Silva", haceTresDias, ayer, 
                "Perfil lipídico excelente", "PENDIENTE");

        // Persistir en la base de datos
        entityManager.persistAndFlush(resultado1);
        entityManager.persistAndFlush(resultado2);
        entityManager.persistAndFlush(resultado3);
        entityManager.persistAndFlush(resultado4);
        entityManager.persistAndFlush(resultado5);
    }

    @Test
    void findResultadosPendientes_DeberiaRetornarSoloResultadosPendientesOrdenadosPorFechaEmision() {
        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findResultadosPendientes();

        // Assert
        assertEquals(2, resultados.size());
        
        // Verificar que todos son PENDIENTE
        assertTrue(resultados.stream().allMatch(r -> r.getEstado().equals("PENDIENTE")));
        
        // Verificar que están ordenados por fecha de emisión ascendente
        for (int i = 0; i < resultados.size() - 1; i++) {
            assertTrue(resultados.get(i).getFechaEmision()
                    .isBefore(resultados.get(i + 1).getFechaEmision()) ||
                    resultados.get(i).getFechaEmision()
                            .isEqual(resultados.get(i + 1).getFechaEmision()));
        }
        
        // Verificar que el primer resultado es el más antiguo
        assertEquals("Laura Gómez", resultados.get(0).getPaciente());
        assertEquals("Pedro López", resultados.get(1).getPaciente());
    }

    @Test
    void findResultadosRecientes_DeberiaRetornarResultadosDeLosUltimos30DiasOrdenadosPorFechaEmisionDescendente() {
        // Arrange
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(30);

        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findResultadosRecientes(fechaInicio);

        // Assert
        assertEquals(5, resultados.size());
        
        // Verificar que están ordenados por fecha de emisión descendente
        for (int i = 0; i < resultados.size() - 1; i++) {
            assertTrue(resultados.get(i).getFechaEmision()
                    .isAfter(resultados.get(i + 1).getFechaEmision()) ||
                    resultados.get(i).getFechaEmision()
                            .isEqual(resultados.get(i + 1).getFechaEmision()));
        }
        
        // Verificar que el primer resultado es el más reciente
        assertEquals("Pedro López", resultados.get(0).getPaciente());
        assertEquals("Laura Gómez", resultados.get(1).getPaciente());
    }

    @Test
    void findResultadosRecientes_ConFechaInicioEspecifica_DeberiaRetornarSoloResultadosPosteriores() {
        // Arrange
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(1);

        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findResultadosRecientes(fechaInicio);

        // Assert
        assertEquals(1, resultados.size());
        assertEquals("Pedro López", resultados.get(0).getPaciente());
        
        // Verificar que la fecha de emisión es posterior a la fecha de inicio
        assertTrue(resultados.get(0).getFechaEmision().isAfter(fechaInicio) ||
                resultados.get(0).getFechaEmision().isEqual(fechaInicio));
    }

    @Test
    void findByFechaExamenBetween_ConRangoValido_DeberiaRetornarResultadosEnRango() {
        // Arrange
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(3);
        LocalDateTime fechaFin = LocalDateTime.now().minusDays(1);

        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findByFechaExamenBetween(fechaInicio, fechaFin);

        // Assert
        assertEquals(2, resultados.size());
        
        // Verificar que las fechas están en el rango especificado
        for (ResultadoMedico resultado : resultados) {
            assertTrue(resultado.getFechaExamen().isAfter(fechaInicio) ||
                    resultado.getFechaExamen().isEqual(fechaInicio));
            assertTrue(resultado.getFechaExamen().isBefore(fechaFin) ||
                    resultado.getFechaExamen().isEqual(fechaFin));
        }
    }

    @Test
    void findByFechaExamenBetween_ConRangoSinResultados_DeberiaRetornarListaVacia() {
        // Arrange
        LocalDateTime fechaInicio = LocalDateTime.now().plusDays(1);
        LocalDateTime fechaFin = LocalDateTime.now().plusDays(2);

        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findByFechaExamenBetween(fechaInicio, fechaFin);

        // Assert
        assertTrue(resultados.isEmpty());
    }

    @Test
    void findByPacienteContainingIgnoreCase_ConBusquedaCaseInsensitive_DeberiaEncontrarResultados() {
        // Act
        List<ResultadoMedico> resultadosMinuscula = resultadoMedicoRepository.findByPacienteContainingIgnoreCase("juan");
        List<ResultadoMedico> resultadosMayuscula = resultadoMedicoRepository.findByPacienteContainingIgnoreCase("JUAN");
        List<ResultadoMedico> resultadosMixto = resultadoMedicoRepository.findByPacienteContainingIgnoreCase("JuAn");

        // Assert
        assertEquals(1, resultadosMinuscula.size());
        assertEquals(1, resultadosMayuscula.size());
        assertEquals(1, resultadosMixto.size());
        
        assertEquals("Juan Pérez", resultadosMinuscula.get(0).getPaciente());
        assertEquals("Juan Pérez", resultadosMayuscula.get(0).getPaciente());
        assertEquals("Juan Pérez", resultadosMixto.get(0).getPaciente());
    }

    @Test
    void findByTipoExamenContainingIgnoreCase_ConBusquedaParcial_DeberiaEncontrarResultados() {
        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findByTipoExamenContainingIgnoreCase("hemograma");

        // Assert
        assertEquals(1, resultados.size());
        assertEquals("Hemograma Completo", resultados.get(0).getTipoExamen());
        assertEquals("Juan Pérez", resultados.get(0).getPaciente());
    }

    @Test
    void findByMedicoResponsableContainingIgnoreCase_ConBusquedaParcial_DeberiaEncontrarResultados() {
        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findByMedicoResponsableContainingIgnoreCase("maría");

        // Assert
        assertEquals(1, resultados.size());
        assertEquals("Dr. María González", resultados.get(0).getMedicoResponsable());
        assertEquals("Juan Pérez", resultados.get(0).getPaciente());
    }

    @Test
    void findByPacienteContainingIgnoreCaseAndTipoExamenContainingIgnoreCase_ConBusquedaCombinada_DeberiaEncontrarResultado() {
        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository
                .findByPacienteContainingIgnoreCaseAndTipoExamenContainingIgnoreCase("juan", "hemograma");

        // Assert
        assertEquals(1, resultados.size());
        assertEquals("Juan Pérez", resultados.get(0).getPaciente());
        assertEquals("Hemograma Completo", resultados.get(0).getTipoExamen());
    }

    @Test
    void findByPacienteContainingIgnoreCaseAndTipoExamenContainingIgnoreCase_ConBusquedaSinResultados_DeberiaRetornarListaVacia() {
        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository
                .findByPacienteContainingIgnoreCaseAndTipoExamenContainingIgnoreCase("juan", "radiografía");

        // Assert
        assertTrue(resultados.isEmpty());
    }

    @Test
    void findByEstado_ConEstadosDiferentes_DeberiaRetornarResultadosCorrectos() {
        // Act
        List<ResultadoMedico> completados = resultadoMedicoRepository.findByEstado("COMPLETADO");
        List<ResultadoMedico> pendientes = resultadoMedicoRepository.findByEstado("PENDIENTE");
        List<ResultadoMedico> revisados = resultadoMedicoRepository.findByEstado("REVISADO");

        // Assert
        assertEquals(2, completados.size());
        assertEquals(2, pendientes.size());
        assertEquals(1, revisados.size());
        
        // Verificar que todos tienen el estado correcto
        assertTrue(completados.stream().allMatch(r -> r.getEstado().equals("COMPLETADO")));
        assertTrue(pendientes.stream().allMatch(r -> r.getEstado().equals("PENDIENTE")));
        assertTrue(revisados.stream().allMatch(r -> r.getEstado().equals("REVISADO")));
    }

    @Test
    void findByEstado_ConEstadoInexistente_DeberiaRetornarListaVacia() {
        // Act
        List<ResultadoMedico> resultados = resultadoMedicoRepository.findByEstado("INEXISTENTE");

        // Assert
        assertTrue(resultados.isEmpty());
    }

    @Test
    void consultasConTextoVacio_DeberiaManejarCorrectamente() {
        // Act
        List<ResultadoMedico> resultadosPaciente = resultadoMedicoRepository.findByPacienteContainingIgnoreCase("");
        List<ResultadoMedico> resultadosTipo = resultadoMedicoRepository.findByTipoExamenContainingIgnoreCase("");
        List<ResultadoMedico> resultadosMedico = resultadoMedicoRepository.findByMedicoResponsableContainingIgnoreCase("");

        // Assert
        // Las consultas con texto vacío deberían retornar todos los resultados
        assertEquals(5, resultadosPaciente.size());
        assertEquals(5, resultadosTipo.size());
        assertEquals(5, resultadosMedico.size());
    }

    @Test
    void consultasConTextoNulo_DeberiaManejarCorrectamente() {
        // Act & Assert
        // Estas consultas deberían lanzar excepciones o retornar listas vacías
        assertThrows(Exception.class, () -> {
            resultadoMedicoRepository.findByPacienteContainingIgnoreCase(null);
        });
        
        assertThrows(Exception.class, () -> {
            resultadoMedicoRepository.findByTipoExamenContainingIgnoreCase(null);
        });
        
        assertThrows(Exception.class, () -> {
            resultadoMedicoRepository.findByMedicoResponsableContainingIgnoreCase(null);
        });
    }
}
