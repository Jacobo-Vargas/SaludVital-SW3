package com.uniquindio.edu.back;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.uniquindio.edu.back.model.dto.CitaDTO;
import com.uniquindio.edu.back.service.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CitaServiceTest {

    @Autowired
    private CitaService citaService;

    @BeforeEach
    void limpiarDatos() {
        citaService.listarCitas().forEach(c -> citaService.eliminarCita(c.getId()));
    }

    @Test
    void crearCita_deberiaGuardarCitaCorrectamente() {
        CitaDTO nueva = new CitaDTO(null, "Juan Pérez", "Odontología", "2025-10-20 10:00", "Limpieza dental");
        CitaDTO guardada = citaService.crearCita(nueva);

        assertNotNull(guardada.getId());
        assertEquals("Juan Pérez", guardada.getPaciente());
        assertEquals(1, citaService.listarCitas().size());
    }

    @Test
    void actualizarCita_deberiaModificarCampos() {
        CitaDTO cita = citaService.crearCita(new CitaDTO(null, "Ana", "Medicina General", "2025-10-11 09:00", "Chequeo"));
        CitaDTO nuevosDatos = new CitaDTO(null, "Ana María", "Cardiología", "2025-10-12 11:00", "Control");

        CitaDTO actualizada = citaService.actualizarCita(cita.getId(), nuevosDatos);

        assertNotNull(actualizada);
        assertEquals("Ana María", actualizada.getPaciente());
        assertEquals("Cardiología", actualizada.getEspecialidad());
    }

    @Test
    void eliminarCita_deberiaRemoverCita() {
        CitaDTO cita = citaService.crearCita(new CitaDTO(null, "Pedro", "Pediatría", "2025-10-15 15:00", "Consulta"));
        boolean eliminada = citaService.eliminarCita(cita.getId());

        assertTrue(eliminada);
        assertTrue(citaService.listarCitas().isEmpty());
    }

    @Test
    void obtenerCita_deberiaRetornarCitaPorId() {
        CitaDTO cita = citaService.crearCita(new CitaDTO(null, "Carlos", "Neurología", "2025-11-01 14:00", "Control"));
        var encontrada = citaService.obtenerCita(cita.getId());

        assertTrue(encontrada.isPresent());
        assertEquals("Carlos", encontrada.get().getPaciente());
    }
}
