package com.uniquindio.edu.back.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uniquindio.edu.back.model.ResultadoMedico;

@Repository
public interface ResultadoMedicoRepository extends JpaRepository<ResultadoMedico, Long> {
    
    // Buscar resultados por paciente
    List<ResultadoMedico> findByPacienteContainingIgnoreCase(String paciente);
    
    // Buscar resultados por tipo de examen
    List<ResultadoMedico> findByTipoExamenContainingIgnoreCase(String tipoExamen);
    
    // Buscar resultados por médico responsable
    List<ResultadoMedico> findByMedicoResponsableContainingIgnoreCase(String medicoResponsable);
    
    // Buscar resultados por estado
    List<ResultadoMedico> findByEstado(String estado);
    
    // Buscar resultados por rango de fechas
    List<ResultadoMedico> findByFechaExamenBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar resultados por paciente y tipo de examen
    List<ResultadoMedico> findByPacienteContainingIgnoreCaseAndTipoExamenContainingIgnoreCase(
            String paciente, String tipoExamen);
    
    // Buscar resultados pendientes de revisión
    @Query("SELECT r FROM ResultadoMedico r WHERE r.estado = 'PENDIENTE' ORDER BY r.fechaEmision ASC")
    List<ResultadoMedico> findResultadosPendientes();
    
    // Buscar resultados recientes (últimos 30 días)
    @Query("SELECT r FROM ResultadoMedico r WHERE r.fechaEmision >= :fechaInicio ORDER BY r.fechaEmision DESC")
    List<ResultadoMedico> findResultadosRecientes(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT r FROM ResultadoMedico r WHERE (:texto IS NULL OR r.descripcion LIKE %:texto%)")
List<ResultadoMedico> buscarPorTexto(@Param("texto") String texto); 

@Query("SELECT r FROM ResultadoMedico r WHERE r.fechaExamen BETWEEN :fechaInicio AND :fechaFin ORDER BY r.fechaExamen DESC")
List<ResultadoMedico> findByFechaExamenBetween(
    @Param("fechaInicio") LocalDate fechaInicio, 
    @Param("fechaFin") LocalDate fechaFin
);
}
