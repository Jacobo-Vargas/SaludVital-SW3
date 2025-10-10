package com.uniquindio.edu.back.service;

import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.Optional;  


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.uniquindio.edu.back.model.Cita;

@Service
@Slf4j
public class CitaService {
      private final Map<Long, Cita> citas = new LinkedHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // Crear una nueva cita
    public Cita crearCita(Cita cita) {
        cita.setId(idCounter.getAndIncrement());
        citas.put(cita.getId(), cita);
        return cita;
    }

    // Listar todas las citas
    public List<Cita> listarCitas() {
        return new ArrayList<>(citas.values());
    }

    // Buscar cita por id
    public Optional<Cita> obtenerCita(Long id) {
        return Optional.ofNullable(citas.get(id));
    }

    // Actualizar una cita existente
    public Optional<Cita> actualizarCita(Long id, Cita datos) {
        if (!citas.containsKey(id)) return Optional.empty();
        Cita cita = citas.get(id);
        cita.setPaciente(datos.getPaciente());
        cita.setEspecialidad(datos.getEspecialidad());
        cita.setFechaHora(datos.getFechaHora());
        cita.setMotivo(datos.getMotivo());
        return Optional.of(cita);
    }

    // Eliminar una cita
    public boolean eliminarCita(Long id) {
        return citas.remove(id) != null;
    }
}
