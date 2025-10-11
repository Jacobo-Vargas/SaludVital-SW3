package com.uniquindio.edu.back.mapper;

import com.uniquindio.edu.back.model.ResultadoMedico;
import com.uniquindio.edu.back.model.dto.ResultadoMedicoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResultadoMedicoMapper {

    ResultadoMedicoDTO toDTO(ResultadoMedico resultadoMedico);
    ResultadoMedico toEntity(ResultadoMedicoDTO dto);
}
