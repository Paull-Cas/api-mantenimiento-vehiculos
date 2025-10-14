package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.response.VehiculoDto;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Vehiculo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class VehiculoMappers {
    public VehiculoDto mapearVheiculo(Vehiculo vehiculo) {
        VehiculoDto vehiculoDTO = new VehiculoDto();
        BeanUtils.copyProperties(vehiculo, vehiculoDTO);
        return vehiculoDTO;
    }

    public Vehiculo mapearVheiculoDto(VehiculoDto vehiculoDto) {
        Vehiculo vehiculo = new Vehiculo();
        BeanUtils.copyProperties(vehiculoDto, vehiculo);
        return vehiculo;
    }
}
