package com.uniquindio.edu.back;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uniquindio.edu.back.model.Cita;
import com.uniquindio.edu.back.service.CitaService;

public class CitaServiceTest {
    private CitaService citaService;

    @BeforeEach
    void setUp() {
        citaService = new CitaService();
    }

    @Test
    void crearCita_deberiaGuardarCitaCorrectamente() {
        Cita nueva = new Cita(null, "Juan Pérez", "Odontología", "2025-10-20 10:00", "Limpieza dental");
        Cita guardada = citaService.crearCita(nueva);

        assertNotNull(guardada.getId());
        assertEquals("Juan Pérez", guardada.getPaciente());
        assertEquals(1, citaService.listarCitas().size());
    }

    @Test
    void actualizarCita_deberiaModificarCampos() {
        Cita cita = citaService.crearCita(new Cita(null, "Ana", "Medicina General", "2025-10-11 09:00", "Chequeo"));
        Cita nuevosDatos = new Cita(null, "Ana María", "Cardiología", "2025-10-12 11:00", "Control");

        var actualizada = citaService.actualizarCita(cita.getId(), nuevosDatos);
        assertTrue(actualizada.isPresent());
        assertEquals("Ana María", actualizada.get().getPaciente());
        assertEquals("Cardiología", actualizada.get().getEspecialidad());
    }

    @Test
    void eliminarCita_deberiaRemoverCita() {
        Cita cita = citaService.crearCita(new Cita(null, "Pedro", "Pediatría", "2025-10-15 15:00", "Consulta"));
        boolean eliminada = citaService.eliminarCita(cita.getId());

        assertTrue(eliminada);
        assertTrue(citaService.listarCitas().isEmpty());
    }

    @Test
    void obtenerCita_deberiaRetornarCitaPorId() {
        Cita cita = citaService.crearCita(new Cita(null, "Carlos", "Neurología", "2025-11-01 14:00", "Control"));
        var encontrada = citaService.obtenerCita(cita.getId());

        assertTrue(encontrada.isPresent());
        assertEquals("Carlos", encontrada.get().getPaciente());
    }
}
