package com.uniquindio.edu.back;

import com.uniquindio.edu.back.model.ResultadoMedico;
import com.uniquindio.edu.back.model.dto.ResultadoMedicoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ResultadoMedicoValidationTest {

    @Autowired
    private Validator validator;

    private ResultadoMedico resultadoMedico;
    private ResultadoMedicoDTO resultadoMedicoDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime fechaExamen = LocalDateTime.now().minusDays(1);
        LocalDateTime fechaEmision = LocalDateTime.now();

        resultadoMedico = new ResultadoMedico();
        resultadoMedico.setPaciente("Juan Pérez");
        resultadoMedico.setTipoExamen("Hemograma Completo");
        resultadoMedico.setResultados("Hemoglobina: 14.2 g/dL (Normal)");
        resultadoMedico.setMedicoResponsable("Dr. María González");
        resultadoMedico.setFechaExamen(fechaExamen);
        resultadoMedico.setFechaEmision(fechaEmision);
        resultadoMedico.setObservaciones("Resultados normales");
        resultadoMedico.setEstado("COMPLETADO");

        resultadoMedicoDTO = new ResultadoMedicoDTO();
        resultadoMedicoDTO.setPaciente("Juan Pérez");
        resultadoMedicoDTO.setTipoExamen("Hemograma Completo");
        resultadoMedicoDTO.setResultados("Hemoglobina: 14.2 g/dL (Normal)");
        resultadoMedicoDTO.setMedicoResponsable("Dr. María González");
        resultadoMedicoDTO.setFechaExamen(fechaExamen);
        resultadoMedicoDTO.setFechaEmision(fechaEmision);
        resultadoMedicoDTO.setObservaciones("Resultados normales");
        resultadoMedicoDTO.setEstado("COMPLETADO");
    }

    @Test
    void validarResultadoMedico_ConDatosValidos_DeberiaPasarValidacion() {
        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    void validarResultadoMedico_ConPacienteVacio_DeberiaFallarValidacion() {
        // Arrange
        resultadoMedico.setPaciente("");

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("paciente") &&
                        v.getMessage().contains("obligatorio")));
    }

    @Test
    void validarResultadoMedico_ConPacienteNulo_DeberiaFallarValidacion() {
        // Arrange
        resultadoMedico.setPaciente(null);

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("paciente") &&
                        v.getMessage().contains("obligatorio")));
    }

    @Test
    void validarResultadoMedico_ConTipoExamenVacio_DeberiaFallarValidacion() {
        // Arrange
        resultadoMedico.setTipoExamen("");

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("tipoExamen") &&
                        v.getMessage().contains("obligatorio")));
    }

    @Test
    void validarResultadoMedico_ConResultadosVacios_DeberiaFallarValidacion() {
        // Arrange
        resultadoMedico.setResultados("");

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("resultados") &&
                        v.getMessage().contains("obligatorio")));
    }

    @Test
    void validarResultadoMedico_ConMedicoResponsableVacio_DeberiaFallarValidacion() {
        // Arrange
        resultadoMedico.setMedicoResponsable("");

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("medicoResponsable") &&
                        v.getMessage().contains("obligatorio")));
    }

    @Test
    void validarResultadoMedico_ConFechaExamenNula_DeberiaFallarValidacion() {
        // Arrange
        resultadoMedico.setFechaExamen(null);

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fechaExamen") &&
                        v.getMessage().contains("obligatoria")));
    }

    @Test
    void validarResultadoMedico_ConFechaEmisionNula_DeberiaFallarValidacion() {
        // Arrange
        resultadoMedico.setFechaEmision(null);

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fechaEmision") &&
                        v.getMessage().contains("obligatoria")));
    }

    @Test
    void validarResultadoMedico_ConFechaExamenFutura_DeberiaFallarValidacion() {
        // Arrange
        resultadoMedico.setFechaExamen(LocalDateTime.now().plusDays(1));

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fechaExamen") &&
                        v.getMessage().contains("futura")));
    }

    @Test
    void validarResultadoMedico_ConFechaEmisionFutura_DeberiaFallarValidacion() {
        // Arrange
        resultadoMedico.setFechaEmision(LocalDateTime.now().plusDays(1));

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fechaEmision") &&
                        v.getMessage().contains("futura")));
    }

    @Test
    void validarResultadoMedico_ConFechaExamenActual_DeberiaPasarValidacion() {
        // Arrange
        resultadoMedico.setFechaExamen(LocalDateTime.now());

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertTrue(violations.isEmpty(), "La fecha actual debería ser válida");
    }

    @Test
    void validarResultadoMedico_ConFechaEmisionActual_DeberiaPasarValidacion() {
        // Arrange
        resultadoMedico.setFechaEmision(LocalDateTime.now());

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertTrue(violations.isEmpty(), "La fecha actual debería ser válida");
    }

    @Test
    void validarResultadoMedico_ConObservacionesNulas_DeberiaPasarValidacion() {
        // Arrange
        resultadoMedico.setObservaciones(null);

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertTrue(violations.isEmpty(), "Las observaciones son opcionales");
    }

    @Test
    void validarResultadoMedico_ConObservacionesVacias_DeberiaPasarValidacion() {
        // Arrange
        resultadoMedico.setObservaciones("");

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertTrue(violations.isEmpty(), "Las observaciones vacías deberían ser válidas");
    }

    @Test
    void validarResultadoMedico_ConEstadoNulo_DeberiaPasarValidacion() {
        // Arrange
        resultadoMedico.setEstado(null);

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertTrue(violations.isEmpty(), "El estado nulo debería ser válido (se establece por defecto)");
    }

    @Test
    void validarResultadoMedico_ConEstadoVacio_DeberiaPasarValidacion() {
        // Arrange
        resultadoMedico.setEstado("");

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertTrue(violations.isEmpty(), "El estado vacío debería ser válido (se establece por defecto)");
    }

    @Test
    void validarResultadoMedico_ConMultiplesViolaciones_DeberiaRetornarTodasLasViolaciones() {
        // Arrange
        resultadoMedico.setPaciente("");
        resultadoMedico.setTipoExamen(null);
        resultadoMedico.setResultados("");
        resultadoMedico.setMedicoResponsable(null);
        resultadoMedico.setFechaExamen(null);
        resultadoMedico.setFechaEmision(null);

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(6, violations.size(), "Debería haber 6 violaciones de validación");
        
        // Verificar que todas las violaciones esperadas están presentes
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("paciente")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("tipoExamen")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("resultados")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("medicoResponsable")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fechaExamen")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fechaEmision")));
    }

    @Test
    void validarResultadoMedico_ConCamposConEspacios_DeberiaPasarValidacion() {
        // Arrange
        resultadoMedico.setPaciente("  Juan Pérez  ");
        resultadoMedico.setTipoExamen("  Hemograma Completo  ");
        resultadoMedico.setResultados("  Hemoglobina: 14.2 g/dL (Normal)  ");

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertTrue(violations.isEmpty(), "Los campos con espacios deberían ser válidos");
    }

    @Test
    void validarResultadoMedico_ConCamposConCaracteresEspeciales_DeberiaPasarValidacion() {
        // Arrange
        resultadoMedico.setPaciente("José María O'Connor");
        resultadoMedico.setTipoExamen("Análisis de Sangre (CBC)");
        resultadoMedico.setResultados("Hemoglobina: 14.2 g/dL (Normal), Hematocrito: 42% (Normal)");
        resultadoMedico.setMedicoResponsable("Dr. María José González-Pérez");

        // Act
        Set<ConstraintViolation<ResultadoMedico>> violations = validator.validate(resultadoMedico);

        // Assert
        assertTrue(violations.isEmpty(), "Los campos con caracteres especiales deberían ser válidos");
    }
}
