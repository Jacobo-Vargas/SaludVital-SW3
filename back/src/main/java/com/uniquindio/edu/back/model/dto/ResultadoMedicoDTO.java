package com.uniquindio.edu.back.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
public class ResultadoMedicoDTO {

    private Long id;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    private String paciente;

    @NotBlank(message = "El tipo de examen es obligatorio")
    private String tipoExamen;

    @NotBlank(message = "Los resultados son obligatorios")
    private String resultados;

    @NotBlank(message = "El médico responsable es obligatorio")
    private String medicoResponsable;

    @NotNull(message = "La fecha del examen es obligatoria")
    @PastOrPresent(message = "La fecha del examen no puede ser futura")
    private LocalDateTime fechaExamen;

    private LocalDateTime fechaEmision;

    private String descripcion;
    private String observaciones;
    private String estado;

    public ResultadoMedicoDTO() {}

    public ResultadoMedicoDTO(Long id, String paciente, String tipoExamen, String resultados,
                              String medicoResponsable, LocalDateTime fechaExamen,
                              LocalDateTime fechaEmision, String descripcion,
                              String observaciones, String estado) {
        this.id = id;
        this.paciente = paciente;
        this.tipoExamen = tipoExamen;
        this.resultados = resultados;
        this.medicoResponsable = medicoResponsable;
        this.fechaExamen = fechaExamen;
        this.fechaEmision = fechaEmision;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.estado = estado;
    }
}
