package com.uniquindio.edu.back.controller;

import java.net.URI;

import com.uniquindio.edu.back.model.dto.CitaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;    

import java.util.List;

import com.uniquindio.edu.back.model.Cita;
import com.uniquindio.edu.back.service.CitaService;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {
    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public ResponseEntity<CitaDTO> crearCita(@Valid @RequestBody CitaDTO cita) {
        CitaDTO nueva = citaService.crearCita(cita);
        return ResponseEntity.created(URI.create("/api/citas/" + nueva.getId())).body(nueva);
    }

    @GetMapping
    public List<CitaDTO> listarCitas() {
        return citaService.listarCitas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> obtenerCita(@PathVariable Long id) {
        return citaService.obtenerCita(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaDTO> actualizarCita(@PathVariable Long id, @Valid @RequestBody CitaDTO cita) {
        return ResponseEntity.ok(citaService.actualizarCita(id, cita));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        boolean eliminada = citaService.eliminarCita(id);
        return eliminada ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
