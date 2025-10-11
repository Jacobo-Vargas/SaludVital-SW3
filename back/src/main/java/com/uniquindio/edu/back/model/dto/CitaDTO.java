package com.uniquindio.edu.back.model.dto;

public class CitaDTO {

    private Long id;
    private String paciente;
    private String especialidad;
    private String fechaHora;
    private String motivo;

    public CitaDTO() {}

    public CitaDTO(Long id, String paciente, String especialidad, String fechaHora, String motivo) {
        this.id = id;
        this.paciente = paciente;
        this.especialidad = especialidad;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPaciente() { return paciente; }
    public void setPaciente(String paciente) { this.paciente = paciente; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
