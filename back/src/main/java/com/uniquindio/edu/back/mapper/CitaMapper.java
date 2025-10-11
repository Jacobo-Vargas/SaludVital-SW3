package com.uniquindio.edu.back.mapper;

import com.uniquindio.edu.back.model.Cita;
import com.uniquindio.edu.back.model.dto.CitaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CitaMapper {


    CitaDTO toDTO(Cita cita);
    Cita toEntity(CitaDTO dto);
}

