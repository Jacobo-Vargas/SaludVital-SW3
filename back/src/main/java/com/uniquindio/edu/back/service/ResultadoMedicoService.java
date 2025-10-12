package com.uniquindio.edu.back.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.uniquindio.edu.back.mapper.ResultadoMedicoMapper;
import com.uniquindio.edu.back.model.ResultadoMedico;
import com.uniquindio.edu.back.model.dto.ResultadoMedicoDTO;
import com.uniquindio.edu.back.repository.ResultadoMedicoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResultadoMedicoService {

    private final ResultadoMedicoRepository resultadoMedicoRepository;
    private final ResultadoMedicoMapper resultadoMedicoMapper;

    // Crear un nuevo resultado médico
    public ResultadoMedicoDTO crearResultadoMedico(ResultadoMedicoDTO dto) {
        log.info("Creando nuevo resultado médico para paciente: {}", dto.getPaciente());
        
        // Establecer fecha de emisión si no está presente
        if (dto.getFechaEmision() == null) {
            dto.setFechaEmision(LocalDateTime.now());
        }
        
        // Establecer estado por defecto si no está presente
        if (dto.getEstado() == null || dto.getEstado().isEmpty()) {
            dto.setEstado("PENDIENTE");
        }
        
        ResultadoMedico resultado = resultadoMedicoMapper.toEntity(dto);
        ResultadoMedico guardado = resultadoMedicoRepository.save(resultado);
        
        log.info("Resultado médico creado exitosamente con ID: {}", guardado.getId());
        return resultadoMedicoMapper.toDTO(guardado);
    }

    // Listar todos los resultados médicos
    public List<ResultadoMedicoDTO> listarResultadosMedicos() {
        return resultadoMedicoRepository.findAll()
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultado médico por id
    public Optional<ResultadoMedicoDTO> obtenerResultadoMedico(Long id) {
        return resultadoMedicoRepository.findById(id)
                .map(resultadoMedicoMapper::toDTO);
    }

    // Actualizar un resultado médico existente
    public ResultadoMedicoDTO actualizarResultadoMedico(Long id, ResultadoMedicoDTO dto) {
        ResultadoMedico resultado = resultadoMedicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resultado médico no encontrado con id: " + id));

        resultado.setPaciente(dto.getPaciente());
        resultado.setTipoExamen(dto.getTipoExamen());
        resultado.setResultados(dto.getResultados());
        resultado.setMedicoResponsable(dto.getMedicoResponsable());
        resultado.setFechaExamen(dto.getFechaExamen());
        resultado.setFechaEmision(dto.getFechaEmision());
        resultado.setObservaciones(dto.getObservaciones());
        resultado.setEstado(dto.getEstado());

        ResultadoMedico actualizado = resultadoMedicoRepository.save(resultado);
        return resultadoMedicoMapper.toDTO(actualizado);
    }

    // Eliminar un resultado médico
    public boolean eliminarResultadoMedico(Long id) {
        if (!resultadoMedicoRepository.existsById(id)) {
            return false;
        }
        resultadoMedicoRepository.deleteById(id);
        return true;
    }

    // Buscar resultados por paciente
    public List<ResultadoMedicoDTO> buscarPorPaciente(String paciente) {
        return resultadoMedicoRepository.findByPacienteContainingIgnoreCase(paciente)
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultados por tipo de examen
    public List<ResultadoMedicoDTO> buscarPorTipoExamen(String tipoExamen) {
        return resultadoMedicoRepository.findByTipoExamenContainingIgnoreCase(tipoExamen)
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultados por médico responsable
    public List<ResultadoMedicoDTO> buscarPorMedicoResponsable(String medicoResponsable) {
        return resultadoMedicoRepository.findByMedicoResponsableContainingIgnoreCase(medicoResponsable)
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultados por estado
    public List<ResultadoMedicoDTO> buscarPorEstado(String estado) {
        return resultadoMedicoRepository.findByEstado(estado)
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultados por rango de fechas
    public List<ResultadoMedicoDTO> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return resultadoMedicoRepository.findByFechaExamenBetween(fechaInicio, fechaFin)
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultados pendientes
    public List<ResultadoMedicoDTO> buscarResultadosPendientes() {
        return resultadoMedicoRepository.findResultadosPendientes()
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultados recientes
    public List<ResultadoMedicoDTO> buscarResultadosRecientes(LocalDateTime fechaInicio) {
        return resultadoMedicoRepository.findResultadosRecientes(fechaInicio)
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Cambiar estado de un resultado médico
    public ResultadoMedicoDTO cambiarEstado(Long id, String nuevoEstado) {
        ResultadoMedico resultado = resultadoMedicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resultado médico no encontrado con id: " + id));

        resultado.setEstado(nuevoEstado);
        ResultadoMedico actualizado = resultadoMedicoRepository.save(resultado);
        
        log.info("Estado del resultado médico {} cambiado a: {}", id, nuevoEstado);
        return resultadoMedicoMapper.toDTO(actualizado);
    }
}
