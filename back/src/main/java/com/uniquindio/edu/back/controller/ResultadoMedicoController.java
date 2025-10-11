package com.uniquindio.edu.back.controller;

import java.net.URI;

import com.uniquindio.edu.back.model.dto.ResultadoMedicoDTO;
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
import jakarta.validation.Valid;    

import java.util.List;
import com.uniquindio.edu.back.service.ResultadoMedicoService;

@RestController
@RequestMapping("/api/resultados-medicos")
@CrossOrigin(origins = "*")
public class ResultadoMedicoController {
    
    private final ResultadoMedicoService resultadoMedicoService;

    public ResultadoMedicoController(ResultadoMedicoService resultadoMedicoService) {
        this.resultadoMedicoService = resultadoMedicoService;
    }

    @PostMapping
    public ResponseEntity<ResultadoMedicoDTO> crearResultadoMedico(@Valid @RequestBody ResultadoMedicoDTO resultado) {
        ResultadoMedicoDTO nuevo = resultadoMedicoService.crearResultadoMedico(resultado);
        return ResponseEntity.created(URI.create("/api/resultados-medicos/" + nuevo.getId())).body(nuevo);
    }

    @GetMapping
    public List<ResultadoMedicoDTO> listarResultadosMedicos() {
        return resultadoMedicoService.listarResultadosMedicos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoMedicoDTO> obtenerResultadoMedico(@PathVariable Long id) {
        return resultadoMedicoService.obtenerResultadoMedico(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultadoMedicoDTO> actualizarResultadoMedico(@PathVariable Long id, @Valid @RequestBody ResultadoMedicoDTO resultado) {
        return ResponseEntity.ok(resultadoMedicoService.actualizarResultadoMedico(id, resultado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResultadoMedico(@PathVariable Long id) {
        boolean eliminado = resultadoMedicoService.eliminarResultadoMedico(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Endpoints adicionales para búsquedas específicas
    
    @GetMapping("/buscar/paciente")
    public List<ResultadoMedicoDTO> buscarPorPaciente(@RequestParam String paciente) {
        return resultadoMedicoService.buscarPorPaciente(paciente);
    }

    @GetMapping("/buscar/tipo-examen")
    public List<ResultadoMedicoDTO> buscarPorTipoExamen(@RequestParam String tipoExamen) {
        return resultadoMedicoService.buscarPorTipoExamen(tipoExamen);
    }

    @GetMapping("/buscar/medico")
    public List<ResultadoMedicoDTO> buscarPorMedicoResponsable(@RequestParam String medicoResponsable) {
        return resultadoMedicoService.buscarPorMedicoResponsable(medicoResponsable);
    }

    @GetMapping("/buscar/estado")
    public List<ResultadoMedicoDTO> buscarPorEstado(@RequestParam String estado) {
        return resultadoMedicoService.buscarPorEstado(estado);
    }

    @GetMapping("/pendientes")
    public List<ResultadoMedicoDTO> buscarResultadosPendientes() {
        return resultadoMedicoService.buscarResultadosPendientes();
    }

    @GetMapping("/recientes")
    public List<ResultadoMedicoDTO> buscarResultadosRecientes() {
        return resultadoMedicoService.buscarResultadosRecientes();
    }

    @PutMapping("/{id}/cambiar-estado")
    public ResponseEntity<ResultadoMedicoDTO> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(resultadoMedicoService.cambiarEstado(id, estado));
    }
}
