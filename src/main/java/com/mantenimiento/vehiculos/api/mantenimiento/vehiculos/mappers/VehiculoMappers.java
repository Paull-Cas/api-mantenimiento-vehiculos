package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.VehiculoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.VehiculoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Cliente;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Vehiculo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class VehiculoMappers {

    public VehiculoResponse mapearVheiculo(Vehiculo vehiculo) {
        VehiculoResponse vehiculoDTO = new VehiculoResponse();
        BeanUtils.copyProperties(vehiculo, vehiculoDTO);
        vehiculoDTO.setPropietarioId(vehiculo.getCliente().getId());
        return vehiculoDTO;
    }

    public Vehiculo mapearVheiculoDto(VehiculoRequest vehiculoDto, Cliente cliente) {
        Vehiculo vehiculo = new Vehiculo();
        BeanUtils.copyProperties(vehiculoDto, vehiculo);
        vehiculo.setCliente(cliente);
        return vehiculo;
    }
}
