package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.response.VehiculoDto;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.VehiculoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.VehiculoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Vehiculo;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.VehiculoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.VehiculoMappers;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.VehiculoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.VehiculoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class VehiculoServiceImpl implements VehiculoService {

    @Autowired
    VehiculoRepositorio vehiculoRepositorio;
    @Autowired
    VehiculoMappers VehiculoService;

    /*@Override
    public Vehiculo saveCliente(Vehiculo vehiculo) {
        log.info("Vehiculo guardado");
        Vehiculo vheculoGuardado = vehiculoRepositorio.save(vehiculo);
        return vheculoGuardado;
    }*/

    @Override
    public VehiculoResponse saveVehiculo(VehiculoRequest vehiculoDto) {
        log.info("Guardando vehiculo");
        Vehiculo vehiculo = VehiculoService.mapearVheiculoDto(vehiculoDto);
        Vehiculo vheculoGuardado = vehiculoRepositorio.save(vehiculo);
        return VehiculoService.mapearVheiculo(vheculoGuardado);
    }

    @Override
    public VehiculoResponse getVehiculo(Long vehiculoId) throws VehiculoNoEncontradoExcepcion {
        Vehiculo vehiculo = vehiculoRepositorio.findById(vehiculoId)
                .orElseThrow(() -> new VehiculoNoEncontradoExcepcion("Vehiculo no encontrado"));
        return VehiculoService.mapearVheiculo(vehiculo);
    }

    @Override
    public VehiculoResponse updateVehiculo(Long id,  VehiculoRequest vehiculoDto) throws VehiculoNoEncontradoExcepcion {
        log.info("Vehiculo Actualizado");
        Vehiculo vehiculo = VehiculoService.mapearVheiculoDto(vehiculoDto);
        Vehiculo vheculoGuardado = vehiculoRepositorio.save(vehiculo);
        return VehiculoService.mapearVheiculo(vheculoGuardado);
    }

    @Override
    public void deleteVehiculo(Long vehiculoId) throws VehiculoNoEncontradoExcepcion {
        if (!vehiculoRepositorio.existsById(vehiculoId)){
            throw new VehiculoNoEncontradoExcepcion("Vehiculo no encontrado");
        }
        vehiculoRepositorio.deleteById(vehiculoId);
        log.info("Vehiculo eliminado", vehiculoId);
    }

    @Override
    public List<VehiculoResponse> listVehiculos() {
        List<Vehiculo> vehiculos = vehiculoRepositorio.findAll();
        List<VehiculoResponse> vehiculoDtos = vehiculos.stream()
                .map(vehiculo -> VehiculoService.mapearVheiculo(vehiculo))
                .collect(Collectors.toList());
        return vehiculoDtos;

    }

    @Override
    public List<VehiculoResponse> searchVehiculos(String keyword) {
        List<Vehiculo> vehiculos = vehiculoRepositorio.searchVehiculos(keyword);
        List<VehiculoResponse> vehiculoDtos = vehiculos.stream()
                .map(vehiculo -> VehiculoService.mapearVheiculo(vehiculo))
                .collect(Collectors.toList());
        return vehiculoDtos;
    }
}
