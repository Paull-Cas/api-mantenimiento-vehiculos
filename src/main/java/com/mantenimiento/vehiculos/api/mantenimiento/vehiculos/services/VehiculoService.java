package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.response.VehiculoDto;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.VehiculoNoEncontradoExcepcion;

import java.util.List;

public interface VehiculoService {

    //Vehiculo saveVehiculo(Vehiculo vehiculo);

    VehiculoDto saveVehiculo(VehiculoDto vehiculoDto);

    VehiculoDto getVehiculo(Long vehiculoId) throws VehiculoNoEncontradoExcepcion;

    VehiculoDto updateVehiculo(VehiculoDto vehiculoDto) throws VehiculoNoEncontradoExcepcion;

    void deleteVehiculo(Long vehiculoId) throws VehiculoNoEncontradoExcepcion;

    List<VehiculoDto> listVehiculos();

    List<VehiculoDto> searchVehiculos(String keyword);

}
