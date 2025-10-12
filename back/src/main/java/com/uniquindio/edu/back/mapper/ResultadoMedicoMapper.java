package com.uniquindio.edu.back.mapper;

import org.mapstruct.Mapper;

import com.uniquindio.edu.back.model.ResultadoMedico;
import com.uniquindio.edu.back.model.dto.ResultadoMedicoDTO;

@Mapper(componentModel = "spring")
public interface ResultadoMedicoMapper {

    ResultadoMedicoDTO toDTO(ResultadoMedico resultadoMedico);
    ResultadoMedico toEntity(ResultadoMedicoDTO dto);
}
