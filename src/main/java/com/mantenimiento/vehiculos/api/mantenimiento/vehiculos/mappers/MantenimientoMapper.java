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
    public MantenimientoPreventivo mapearMantenimientoPreventivoRequestDTO(MantenimientoPreventivoRequestDTO mantenimientoPreventivoRequestDTO){
        MantenimientoPreventivo mantenimientoPreventivo = new MantenimientoPreventivo();
        BeanUtils.copyProperties(mantenimientoPreventivoRequestDTO, mantenimientoPreventivo);
        return mantenimientoPreventivo;
    }

    public MantenimientoCorrectivo mapearMantenimientoCorrectivoRequestDTO(MantenimientoCorrectivoRequestDTO mantenimientoCorrectivoRequestDTO){
        MantenimientoCorrectivo mantenimientoCorrectivo = new MantenimientoCorrectivo();
        BeanUtils.copyProperties(mantenimientoCorrectivoRequestDTO, mantenimientoCorrectivo);
        return  mantenimientoCorrectivo;
    }

    private MantenimientoPreventivoResponseDTO mapearMantenimientoPreventivo(MantenimientoPreventivo mantenimientoPreventivo){
        MantenimientoPreventivoResponseDTO mantenimientoPreventivoResponseDTO = new MantenimientoPreventivoResponseDTO();
        BeanUtils.copyProperties(mantenimientoPreventivo, mantenimientoPreventivoResponseDTO);
        mantenimientoPreventivoResponseDTO.setTipoServicio(mantenimientoPreventivo.getClass().getSimpleName());
        if(mantenimientoPreventivo.getOrdenMantenimiento() != null){
            mantenimientoPreventivoResponseDTO.setOrdenMantenimientoId(mantenimientoPreventivo.getOrdenMantenimiento().getId());
        }
        return mantenimientoPreventivoResponseDTO;
    }
    private MantenimientoCorrectivoResponseDTO mapearMantenimientoCorrectivo(MantenimientoCorrectivo mantenimientoCorrectivo){
        MantenimientoCorrectivoResponseDTO mantenimientoCorrectivoResponseDTO = new MantenimientoCorrectivoResponseDTO();
        BeanUtils.copyProperties(mantenimientoCorrectivo, mantenimientoCorrectivoResponseDTO);
        mantenimientoCorrectivoResponseDTO.setTipoServicio(mantenimientoCorrectivo.getClass().getSimpleName());
        if(mantenimientoCorrectivo.getOrdenMantenimiento() != null){
            mantenimientoCorrectivoResponseDTO.setOrdenMantenimientoId(mantenimientoCorrectivo.getOrdenMantenimiento().getId());
        }
        return mantenimientoCorrectivoResponseDTO;
    }
    public MantenimientoResponseDTO mapearMantenimiento(Mantenimiento mantenimiento){
        if(mantenimiento instanceof MantenimientoPreventivo){
            return mapearMantenimientoPreventivo((MantenimientoPreventivo) mantenimiento);
        } else if (mantenimiento instanceof  MantenimientoCorrectivo) {
            return mapearMantenimientoCorrectivo((MantenimientoCorrectivo) mantenimiento);
        }
        throw new IllegalArgumentException("Tipo de Mantenimiento desconocido: " + mantenimiento.getClass().getName());
    }
}
