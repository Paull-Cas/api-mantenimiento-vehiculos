package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.VehiculoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.VehiculoPageReponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.VehiculoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.VehiculoNoEncontradoExcepcion;

import java.util.List;

public interface VehiculoService {

    //Vehiculo saveVehiculo(Vehiculo vehiculo);

    VehiculoResponse saveVehiculo(VehiculoRequest vehiculoRequest);

    VehiculoResponse getVehiculo(Long vehiculoId) throws VehiculoNoEncontradoExcepcion;

    VehiculoResponse updateVehiculo(Long id, VehiculoRequest vehiculoRequest) throws VehiculoNoEncontradoExcepcion;

    void deleteVehiculo(Long vehiculoId) throws VehiculoNoEncontradoExcepcion;

    List<VehiculoResponse> listVehiculos();

    List<VehiculoResponse> searchVehiculos(String keyword);

    List<VehiculoResponse> listByCliente(Long clienteId);

    List<VehiculoResponse> misVehiculos();

    //Historial cuenta dto
    //List<VehiculoResponse> listHistorialVheiculo(Long vehiculoId) throws VehiculoNoEncontradoExcepcion;

    //List<VehiculoResponse> getHistorialVehiculo( Long id, int page, int size) throws VehiculoNoEncontradoExcepcion;

    VehiculoPageReponse getVehiculoPageReponse(int page, int size);
}
