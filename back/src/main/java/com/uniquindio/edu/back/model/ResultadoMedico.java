package com.uniquindio.edu.back.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
@Entity
@Table(name = "resultado_medico")
public class ResultadoMedico implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @NotNull(message = "La fecha de emisión es obligatoria")
    @PastOrPresent(message = "La fecha de emisión no puede ser futura")
    private LocalDateTime fechaEmision;

    private String descripcion;

    private String observaciones;
    private String estado; // PENDIENTE, COMPLETADO, REVISADO

    public ResultadoMedico() {}

    public ResultadoMedico(Long id, String paciente, String tipoExamen, String resultados, 
                          String medicoResponsable, LocalDateTime fechaExamen, 
                          LocalDateTime fechaEmision, String observaciones, String estado) {
        this.id = id;
        this.paciente = paciente;
        this.tipoExamen = tipoExamen;
        this.resultados = resultados;
        this.medicoResponsable = medicoResponsable;
        this.fechaExamen = fechaExamen;
        this.fechaEmision = fechaEmision;
        this.observaciones = observaciones;
        this.estado = estado;
    }
}
