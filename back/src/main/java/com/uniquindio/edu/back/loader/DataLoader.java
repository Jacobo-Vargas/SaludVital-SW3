package com.uniquindio.edu.back.loader;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.uniquindio.edu.back.model.Cita;
import com.uniquindio.edu.back.model.ResultadoMedico;
import com.uniquindio.edu.back.repository.CitaRepository;
import com.uniquindio.edu.back.repository.ResultadoMedicoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CitaRepository citaRepository;
    private final ResultadoMedicoRepository resultadoMedicoRepository;

    @Override
    public void run(String... args) throws Exception {

        citaRepository.save(new Cita(null, "Juan Pérez", "Odontología", "2025-10-20 10:00", "Limpieza dental"));
        citaRepository.save(new Cita(null, "Ana María", "Cardiología", "2025-10-22 09:30", "Control cardíaco"));
        citaRepository.save(new Cita(null, "Pedro López", "Pediatría", "2025-10-25 14:00", "Consulta general"));
        citaRepository.save(new Cita(null, "Carlos Ramírez", "Neurología", "2025-11-01 11:00", "Revisión"));
        citaRepository.save(new Cita(null, "Laura Gómez", "Medicina General", "2025-10-28 16:00", "Chequeo anual"));

        log.info("Datos iniciales de citas cargados correctamente!");

        // Cargar datos de prueba para resultados médicos
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime haceUnaSemana = ahora.minusDays(7);
        LocalDateTime haceDosDias = ahora.minusDays(2);
        LocalDateTime ayer = ahora.minusDays(1);

        resultadoMedicoRepository.save(new ResultadoMedico(
                null, "Juan Pérez", "Hemograma Completo", 
                "Hemoglobina: 14.2 g/dL (Normal), Hematocrito: 42% (Normal), Leucocitos: 7,500/μL (Normal)", 
                "Dr. María González", haceUnaSemana, ayer, 
                "Resultados dentro de parámetros normales", "COMPLETADO"));

        resultadoMedicoRepository.save(new ResultadoMedico(
                null, "Ana María", "Electrocardiograma", 
                "Ritmo sinusal regular, FC: 72 lpm, Sin alteraciones significativas", 
                "Dr. Carlos Mendoza", haceDosDias, ayer, 
                "ECG normal para la edad", "COMPLETADO"));

        resultadoMedicoRepository.save(new ResultadoMedico(
                null, "Pedro López", "Radiografía de Tórax", 
                "Campos pulmonares claros, silueta cardíaca normal, estructuras óseas sin alteraciones", 
                "Dr. Ana Rodríguez", ayer, ahora, 
                "Radiografía normal", "PENDIENTE"));

        resultadoMedicoRepository.save(new ResultadoMedico(
                null, "Carlos Ramírez", "Resonancia Magnética Cerebral", 
                "Sin evidencia de lesiones focales, estructuras anatómicas preservadas", 
                "Dr. Luis Fernández", haceUnaSemana, haceDosDias, 
                "RMN normal", "REVISADO"));

        resultadoMedicoRepository.save(new ResultadoMedico(
                null, "Laura Gómez", "Perfil Lipídico", 
                "Colesterol Total: 180 mg/dL (Normal), LDL: 110 mg/dL (Normal), HDL: 55 mg/dL (Normal), Triglicéridos: 120 mg/dL (Normal)", 
                "Dr. Patricia Silva", haceDosDias, ayer, 
                "Perfil lipídico excelente", "COMPLETADO"));

        resultadoMedicoRepository.save(new ResultadoMedico(
                null, "María Torres", "Ultrasonido Abdominal", 
                "Hígado, vesícula biliar, páncreas y riñones de tamaño y ecotextura normales", 
                "Dr. Roberto Jiménez", ayer, ahora, 
                "Ultrasonido normal", "PENDIENTE"));

        log.info("Datos iniciales de resultados médicos cargados correctamente!");
    }
}
