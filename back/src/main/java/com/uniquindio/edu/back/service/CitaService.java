package com.uniquindio.edu.back.service;

import com.uniquindio.edu.back.mapper.CitaMapper;
import com.uniquindio.edu.back.model.Cita;
import com.uniquindio.edu.back.model.dto.CitaDTO;
import com.uniquindio.edu.back.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;

    // Crear una nueva cita
    public CitaDTO crearCita(CitaDTO dto) {
        Cita cita = citaMapper.toEntity(dto);
        Cita guardada = citaRepository.save(cita);
        return citaMapper.toDTO(guardada);
    }

    // Listar todas las citas
    public List<CitaDTO> listarCitas() {
        return citaRepository.findAll()
                .stream()
                .map(citaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar cita por id
    public Optional<CitaDTO> obtenerCita(Long id) {
        return citaRepository.findById(id)
                .map(citaMapper::toDTO);
    }

    // Actualizar una cita existente
    public CitaDTO actualizarCita(Long id, CitaDTO dto) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));

        cita.setPaciente(dto.getPaciente());
        cita.setEspecialidad(dto.getEspecialidad());
        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivo(dto.getMotivo());

        Cita actualizada = citaRepository.save(cita);
        return citaMapper.toDTO(actualizada);
    }

    // Eliminar una cita
    public boolean eliminarCita(Long id) {
        if (!citaRepository.existsById(id)) {
            return false;
        }
        citaRepository.deleteById(id);
        return true;
    }
}
