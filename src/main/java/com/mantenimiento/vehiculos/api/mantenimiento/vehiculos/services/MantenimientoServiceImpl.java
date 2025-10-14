package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.response.VehiculoDto;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Cliente;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Vehiculo;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.VehiculoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.MantenimientoMappers;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.VehiculoRepositorio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class MantenimientoServiceImpl implements MantenimientoService{

    @Autowired
    VehiculoRepositorio vehiculoRepositorio;
    @Autowired
    MantenimientoMappers mantenimientoMappers;

    /*@Override
    public Vehiculo saveCliente(Vehiculo vehiculo) {
        log.info("Vehiculo guardado");
        Vehiculo vheculoGuardado = vehiculoRepositorio.save(vehiculo);
        return vheculoGuardado;
    }*/

    @Override
    public VehiculoDto saveVehiculo(VehiculoDto vehiculoDto) {
        log.info("Guardando vehiculo");
        Vehiculo vehiculo = mantenimientoMappers.mapearVheiculoDto(vehiculoDto);
        Vehiculo vheculoGuardado = vehiculoRepositorio.save(vehiculo);
        return mantenimientoMappers.mapearVheiculo(vheculoGuardado);
    }

    @Override
    public VehiculoDto getVehiculo(Long vehiculoId) throws VehiculoNoEncontradoExcepcion {
        Vehiculo vehiculo = vehiculoRepositorio.findById(vehiculoId)
                .orElseThrow(() -> new VehiculoNoEncontradoExcepcion("Vehiculo no encontrado"));
        return mantenimientoMappers.mapearVheiculo(vehiculo);
    }

    @Override
    public VehiculoDto updateVehiculo(VehiculoDto vehiculoDto) throws VehiculoNoEncontradoExcepcion {
        log.info("Vehiculo Actualizado");
        Vehiculo vehiculo = mantenimientoMappers.mapearVheiculoDto(vehiculoDto);
        Vehiculo vheculoGuardado = vehiculoRepositorio.save(vehiculo);
        return mantenimientoMappers.mapearVheiculo(vheculoGuardado);

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
    public List<VehiculoDto> listVehiculos() {
        List<Vehiculo> vehiculos = vehiculoRepositorio.findAll();
        List<VehiculoDto> vehiculoDtos = vehiculos.stream()
                .map(vehiculo -> mantenimientoMappers.mapearVheiculo(vehiculo))
                .collect(Collectors.toList());
        return vehiculoDtos;

    }

    @Override
    public List<VehiculoDto> searchVehiculos(String keyword) {
        List<Vehiculo> vehiculos = vehiculoRepositorio.searchVehiculos(keyword);
        List<VehiculoDto> vehiculoDtos = vehiculos.stream()
                .map(vehiculo -> mantenimientoMappers.mapearVheiculo(vehiculo))
                .collect(Collectors.toList());
        return vehiculoDtos;
    }
}
