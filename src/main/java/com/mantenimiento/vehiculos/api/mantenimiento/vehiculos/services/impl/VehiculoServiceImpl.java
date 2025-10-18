package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.VehiculoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.VehiculoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Cliente;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Vehiculo;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.ClienteNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.VehiculoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.VehiculoMappers;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.ClienteRepositorio;
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
    private VehiculoRepositorio vehiculoRepositorio;

    @Autowired
    private VehiculoMappers vehiculoMapper;  // Cambié el nombre a "vehiculoMapper" para hacerlo consistente

    @Autowired
    private ClienteRepositorio clienteRepo;

    @Override
    public VehiculoResponse saveVehiculo(VehiculoRequest vehiculoDto) throws ClienteNoEncontradoExcepcion {
        log.info("Guardando vehiculo");
        Cliente cliente = clienteRepo.findById(vehiculoDto.getPropietarioId()).orElseThrow(() ->
                new ClienteNoEncontradoExcepcion("Cliente no encontrado con ID: " + vehiculoDto.getPropietarioId()));
        Vehiculo vehiculo = vehiculoMapper.mapearVheiculoDto(vehiculoDto, cliente);  // Usando vehiculoMapper
        Vehiculo vheculoGuardado = vehiculoRepositorio.save(vehiculo);
        return vehiculoMapper.mapearVheiculo(vheculoGuardado);  // Usando vehiculoMapper
    }

    @Override
    public VehiculoResponse getVehiculo(Long vehiculoId) throws VehiculoNoEncontradoExcepcion {
        Vehiculo vehiculo = vehiculoRepositorio.findById(vehiculoId)
                .orElseThrow(() -> new VehiculoNoEncontradoExcepcion("Vehiculo no encontrado"));
        return vehiculoMapper.mapearVheiculo(vehiculo);  // Usando vehiculoMapper
    }

    @Override
    public VehiculoResponse updateVehiculo(Long id, VehiculoRequest vehiculoDto) throws VehiculoNoEncontradoExcepcion {
        log.info("Vehiculo Actualizado");
        Cliente cliente = clienteRepo.findById(vehiculoDto.getPropietarioId()).orElseThrow(() ->
                new ClienteNoEncontradoExcepcion("Cliente no encontrado con ID: " + vehiculoDto.getPropietarioId()));
        Vehiculo vehiculo = vehiculoMapper.mapearVheiculoDto(vehiculoDto, cliente);  // Usando vehiculoMapper
        Vehiculo vheculoGuardado = vehiculoRepositorio.save(vehiculo);
        return vehiculoMapper.mapearVheiculo(vheculoGuardado);  // Usando vehiculoMapper
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
                .map(vehiculoMapper::mapearVheiculo)  // Usando vehiculoMapper
                .collect(Collectors.toList());
        return vehiculoDtos;
    }

    @Override
    public List<VehiculoResponse> searchVehiculos(String keyword) {
        List<Vehiculo> vehiculos = vehiculoRepositorio.searchVehiculos(keyword);
        List<VehiculoResponse> vehiculoDtos = vehiculos.stream()
                .map(vehiculoMapper::mapearVheiculo)  // Usando vehiculoMapper
                .collect(Collectors.toList());
        return vehiculoDtos;
    }

    @Override
    public List<VehiculoResponse> listByCliente(Long clienteId) {
        return vehiculoRepositorio.findByCliente_Id(clienteId).stream()
                .map(vehiculoMapper::mapearVheiculo)  // Usando vehiculoMapper
                .collect(Collectors.toList());
    }
}
