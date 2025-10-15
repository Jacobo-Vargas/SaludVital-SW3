package com.uniquindio.edu.back.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uniquindio.edu.back.model.dto.ResultadoMedicoDTO;
import com.uniquindio.edu.back.service.ResultadoMedicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/resultados-medicos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ResultadoMedicoController {

    private final ResultadoMedicoService resultadoMedicoService;

    @PostMapping
    public ResponseEntity<ResultadoMedicoDTO> crearResultadoMedico(@Valid @RequestBody ResultadoMedicoDTO resultadoMedico) {
        log.info("Creando nuevo resultado médico para paciente: {}", resultadoMedico.getPaciente());
        ResultadoMedicoDTO nuevo = resultadoMedicoService.crearResultadoMedico(resultadoMedico);
        return ResponseEntity.created(URI.create("/api/resultados-medicos/" + nuevo.getId())).body(nuevo);
    }

    @GetMapping
    public List<ResultadoMedicoDTO> listarResultadosMedicos() {
        log.info("Listando todos los resultados médicos");
        return resultadoMedicoService.listarResultadosMedicos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoMedicoDTO> obtenerResultadoMedico(@PathVariable Long id) {
        log.info("Obteniendo resultado médico con ID: {}", id);
        return resultadoMedicoService.obtenerResultadoMedico(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultadoMedicoDTO> actualizarResultadoMedico(@PathVariable Long id,
                                                                        @Valid @RequestBody ResultadoMedicoDTO resultadoMedico) {
        log.info("Actualizando resultado médico con ID: {}", id);
        try {
            ResultadoMedicoDTO actualizado = resultadoMedicoService.actualizarResultadoMedico(id, resultadoMedico);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            log.error("Error al actualizar resultado médico: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResultadoMedico(@PathVariable Long id) {
        log.info("Eliminando resultado médico con ID: {}", id);
        boolean eliminado = resultadoMedicoService.eliminarResultadoMedico(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar/paciente")
    public List<ResultadoMedicoDTO> buscarPorPaciente(@RequestParam String paciente) {
        log.info("Buscando resultados médicos por paciente: {}", paciente);
        return resultadoMedicoService.buscarPorPaciente(paciente);
    }

    @GetMapping("/buscar/tipo-examen")
    public List<ResultadoMedicoDTO> buscarPorTipoExamen(@RequestParam String tipoExamen) {
        log.info("Buscando resultados médicos por tipo de examen: {}", tipoExamen);
        return resultadoMedicoService.buscarPorTipoExamen(tipoExamen);
    }

    @GetMapping("/buscar/medico")
    public List<ResultadoMedicoDTO> buscarPorMedicoResponsable(@RequestParam String medicoResponsable) {
        log.info("Buscando resultados médicos por médico responsable: {}", medicoResponsable);
        return resultadoMedicoService.buscarPorMedicoResponsable(medicoResponsable);
    }

    @GetMapping("/buscar/estado")
    public List<ResultadoMedicoDTO> buscarPorEstado(@RequestParam String estado) {
        log.info("Buscando resultados médicos por estado: {}", estado);
        return resultadoMedicoService.buscarPorEstado(estado);
    }

    @GetMapping("/buscar/fechas")
    public List<ResultadoMedicoDTO> buscarPorRangoFechas(@RequestParam LocalDateTime fechaInicio,
                                                         @RequestParam LocalDateTime fechaFin) {
        log.info("Buscando resultados médicos entre fechas: {} y {}", fechaInicio, fechaFin);
        return resultadoMedicoService.buscarPorRangoFechas(fechaInicio, fechaFin);
    }

    @GetMapping("/pendientes")
    public List<ResultadoMedicoDTO> buscarResultadosPendientes() {
        log.info("Buscando resultados médicos pendientes");
        return resultadoMedicoService.buscarResultadosPendientes();
    }

    @GetMapping("/recientes")
    public List<ResultadoMedicoDTO> buscarResultadosRecientes(@RequestParam LocalDateTime fechaInicio) {
        log.info("Buscando resultados médicos recientes desde: {}", fechaInicio);
        return resultadoMedicoService.buscarResultadosRecientes(fechaInicio);
    }

    @PutMapping("/{id}/cambiar-estado")
    public ResponseEntity<ResultadoMedicoDTO> cambiarEstado(@PathVariable Long id,
                                                            @RequestParam String estado) {
        log.info("Cambiando estado del resultado médico {} a: {}", id, estado);
        try {
            ResultadoMedicoDTO actualizado = resultadoMedicoService.cambiarEstado(id, estado);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            log.error("Error al cambiar estado del resultado médico: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
