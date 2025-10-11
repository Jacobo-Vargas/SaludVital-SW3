package com.uniquindio.edu.back.model.dto;

import java.time.LocalDateTime;

public class ResultadoMedicoDTO {

    private Long id;
    private String paciente;
    private String tipoExamen;
    private String resultados;
    private String medicoResponsable;
    private LocalDateTime fechaExamen;
    private LocalDateTime fechaEmision;
    private String observaciones;
    private String estado;

    public ResultadoMedicoDTO() {}

    public ResultadoMedicoDTO(Long id, String paciente, String tipoExamen, String resultados, 
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

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPaciente() { return paciente; }
    public void setPaciente(String paciente) { this.paciente = paciente; }

    public String getTipoExamen() { return tipoExamen; }
    public void setTipoExamen(String tipoExamen) { this.tipoExamen = tipoExamen; }

    public String getResultados() { return resultados; }
    public void setResultados(String resultados) { this.resultados = resultados; }

    public String getMedicoResponsable() { return medicoResponsable; }
    public void setMedicoResponsable(String medicoResponsable) { this.medicoResponsable = medicoResponsable; }

    public LocalDateTime getFechaExamen() { return fechaExamen; }
    public void setFechaExamen(LocalDateTime fechaExamen) { this.fechaExamen = fechaExamen; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
