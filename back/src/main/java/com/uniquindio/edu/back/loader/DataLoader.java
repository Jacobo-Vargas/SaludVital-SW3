package com.uniquindio.edu.back.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.uniquindio.edu.back.model.Cita;
import com.uniquindio.edu.back.repository.CitaRepository;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CitaRepository citaRepository;

    @Override
    public void run(String... args) throws Exception {

        citaRepository.save(new Cita(null, "Juan Pérez", "Odontología", "2025-10-20 10:00", "Limpieza dental"));
        citaRepository.save(new Cita(null, "Ana María", "Cardiología", "2025-10-22 09:30", "Control cardíaco"));
        citaRepository.save(new Cita(null, "Pedro López", "Pediatría", "2025-10-25 14:00", "Consulta general"));
        citaRepository.save(new Cita(null, "Carlos Ramírez", "Neurología", "2025-11-01 11:00", "Revisión"));
        citaRepository.save(new Cita(null, "Laura Gómez", "Medicina General", "2025-10-28 16:00", "Chequeo anual"));

        log.info("Datos iniciales de citas cargados correctamente!");
    }
}
