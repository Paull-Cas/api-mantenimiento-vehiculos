package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoCorrectivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoPreventivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoCorrectivoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoPreventivoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.MantenimientoCorrectivo;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.MantenimientoPreventivo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class MantenimientoMapper {

    public MantenimientoPreventivo mapearDeMantPreventivoRequest(MantenimientoPreventivoRequestDTO requestDTO){
        MantenimientoPreventivo mantenimientoPreventivo = new MantenimientoPreventivo();
        BeanUtils.copyProperties(requestDTO, mantenimientoPreventivo);
        return mantenimientoPreventivo;
    }

    public MantenimientoCorrectivo mapearDeMantCorrectivoRequest(MantenimientoCorrectivoRequestDTO requestDTO){
        MantenimientoCorrectivo mantenimientoCorrectivo = new MantenimientoCorrectivo();
        BeanUtils.copyProperties(requestDTO, mantenimientoCorrectivo);
        return  mantenimientoCorrectivo;
    }

    public MantenimientoResponseDTO mapearDeMantResponseDTO(Mantenimiento mantenimiento){
        if (mantenimiento instanceof MantenimientoPreventivo) {
            return mapearDeMantPreventivoResponse((MantenimientoPreventivo) mantenimiento);
        } else if (mantenimiento instanceof MantenimientoCorrectivo) {
            return mapearDeMantCorrectivoResponse((MantenimientoCorrectivo) mantenimiento);
        }
        throw new IllegalArgumentException("Tipo de Mantenimiento desconocido: " + mantenimiento.getClass().getName());
    }

    private MantenimientoPreventivoResponseDTO mapearDeMantPreventivoResponse(MantenimientoPreventivo mantenimientoPreventivo){
        MantenimientoPreventivoResponseDTO responseDTO = new MantenimientoPreventivoResponseDTO();
        BeanUtils.copyProperties(mantenimientoPreventivo, responseDTO);
        responseDTO.setTipoServicio("PREVENTIVO");
        return responseDTO;
    }

    private MantenimientoCorrectivoResponseDTO mapearDeMantCorrectivoResponse(MantenimientoCorrectivo mantenimientoCorrectivo){
        MantenimientoCorrectivoResponseDTO responseDTO = new MantenimientoCorrectivoResponseDTO();
        BeanUtils.copyProperties(mantenimientoCorrectivo, responseDTO);
        responseDTO.setTipoServicio("CORRECTIVO");
        return responseDTO;
    }
}
