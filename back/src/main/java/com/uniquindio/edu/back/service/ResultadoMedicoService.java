package com.uniquindio.edu.back.service;

import com.uniquindio.edu.back.mapper.ResultadoMedicoMapper;
import com.uniquindio.edu.back.model.ResultadoMedico;
import com.uniquindio.edu.back.model.dto.ResultadoMedicoDTO;
import com.uniquindio.edu.back.repository.ResultadoMedicoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        log.info("Listando todos los resultados médicos");
        return resultadoMedicoRepository.findAll()
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultado médico por id
    public Optional<ResultadoMedicoDTO> obtenerResultadoMedico(Long id) {
        log.info("Buscando resultado médico con ID: {}", id);
        return resultadoMedicoRepository.findById(id)
                .map(resultadoMedicoMapper::toDTO);
    }

    // Actualizar un resultado médico existente
    public ResultadoMedicoDTO actualizarResultadoMedico(Long id, ResultadoMedicoDTO dto) {
        log.info("Actualizando resultado médico con ID: {}", id);
        
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
        log.info("Resultado médico actualizado exitosamente");
        return resultadoMedicoMapper.toDTO(actualizado);
    }

    // Eliminar un resultado médico
    public boolean eliminarResultadoMedico(Long id) {
        log.info("Eliminando resultado médico con ID: {}", id);
        
        if (!resultadoMedicoRepository.existsById(id)) {
            log.warn("Resultado médico con ID {} no encontrado para eliminar", id);
            return false;
        }
        
        resultadoMedicoRepository.deleteById(id);
        log.info("Resultado médico eliminado exitosamente");
        return true;
    }

    // Buscar resultados por paciente
    public List<ResultadoMedicoDTO> buscarPorPaciente(String paciente) {
        log.info("Buscando resultados médicos para paciente: {}", paciente);
        return resultadoMedicoRepository.findByPacienteContainingIgnoreCase(paciente)
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultados por tipo de examen
    public List<ResultadoMedicoDTO> buscarPorTipoExamen(String tipoExamen) {
        log.info("Buscando resultados médicos por tipo de examen: {}", tipoExamen);
        return resultadoMedicoRepository.findByTipoExamenContainingIgnoreCase(tipoExamen)
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultados por médico responsable
    public List<ResultadoMedicoDTO> buscarPorMedicoResponsable(String medicoResponsable) {
        log.info("Buscando resultados médicos por médico responsable: {}", medicoResponsable);
        return resultadoMedicoRepository.findByMedicoResponsableContainingIgnoreCase(medicoResponsable)
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultados por estado
    public List<ResultadoMedicoDTO> buscarPorEstado(String estado) {
        log.info("Buscando resultados médicos por estado: {}", estado);
        return resultadoMedicoRepository.findByEstado(estado)
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultados pendientes
    public List<ResultadoMedicoDTO> buscarResultadosPendientes() {
        log.info("Buscando resultados médicos pendientes");
        return resultadoMedicoRepository.findResultadosPendientes()
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar resultados recientes (últimos 30 días)
    public List<ResultadoMedicoDTO> buscarResultadosRecientes() {
        log.info("Buscando resultados médicos recientes");
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(30);
        return resultadoMedicoRepository.findResultadosRecientes(fechaInicio)
                .stream()
                .map(resultadoMedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Cambiar estado de un resultado médico
    public ResultadoMedicoDTO cambiarEstado(Long id, String nuevoEstado) {
        log.info("Cambiando estado del resultado médico ID {} a: {}", id, nuevoEstado);
        
        ResultadoMedico resultado = resultadoMedicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resultado médico no encontrado con id: " + id));

        resultado.setEstado(nuevoEstado);
        ResultadoMedico actualizado = resultadoMedicoRepository.save(resultado);
        
        log.info("Estado actualizado exitosamente");
        return resultadoMedicoMapper.toDTO(actualizado);
    }
}
