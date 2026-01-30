package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mantenimiento;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class MantenimientoMapper {

    public Mantenimiento mapearDeRequest(MantenimientoRequest requestDTO) {
        Mantenimiento mantenimiento = new Mantenimiento();
        BeanUtils.copyProperties(requestDTO, mantenimiento);
        return mantenimiento;
    }

    public MantenimientoResponseDTO mapearDeResponseDTO(Mantenimiento mantenimiento) {
        MantenimientoResponseDTO responseDTO = new MantenimientoResponseDTO();
        BeanUtils.copyProperties(mantenimiento, responseDTO);
        return responseDTO;
    }
}