package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.VehiculoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.VehiculoPageReponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Vehiculo vehiculo = vehiculoRepositorio.findById(vehiculoId).orElseThrow(() ->
                new VehiculoNoEncontradoExcepcion("Vehiculo no encontrado con ID: " + vehiculoId));
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

    @Override
    public VehiculoPageReponse getVehiculoPageReponse(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vehiculo> pagevehiculos = vehiculoRepositorio.findAll(pageable);
        List<VehiculoResponse> vehiculosres = pagevehiculos.getContent().stream()
                .map(vehiculoMapper::mapearVheiculo)  // Usando vehiculoMapper
                .collect(Collectors.toList());

        VehiculoPageReponse vehiculoPageReponse = new VehiculoPageReponse();
        vehiculoPageReponse.setVehiculos(vehiculosres);
        vehiculoPageReponse.setCurrentPage(pagevehiculos.getNumber());
        vehiculoPageReponse.setPageSize(pagevehiculos.getSize());
        vehiculoPageReponse.setTotalPages(pagevehiculos.getTotalPages());
        vehiculoPageReponse.setTotalElements(pagevehiculos.getTotalElements());

        return vehiculoPageReponse;
    }
    /*
    @Override
    public List<VehiculoResponse> listHistorialVheiculo(Long vehiculoId) throws VehiculoNoEncontradoExcepcion {
        List<Vehiculo> vehiculos = vehiculoRepositorio.findAll(vehiculoId);
        if (vehiculos.isEmpty()) {
            throw new VehiculoNoEncontradoExcepcion("Vehiculo no encontrada o sin operaciones");
        }
        return vehiculos.stream().map(vehiculoss ->
                        vehiculoMapper.mapearVheiculo(vehiculoss))
                .collect(Collectors.toList());
    }

    @Override
    public List<VehiculoResponse> getHistorialVehiculo(Long id, int page, int size) throws VehiculoNoEncontradoExcepcion {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Vehiculo> vehicuPage = vehiculoRepositorio.findById(id, pageRequest);

        if (vehicuPage.isEmpty()) {
            throw new VehiculoNoEncontradoExcepcion("Vehiculo no encontrada o sin operaciones");
        }
        List<VehiculoResponse> vehiculoResponses = vehicuPage.getContent().stream()
                .map(vehiculo -> vehiculoMapper.mapearVheiculo(vehiculo))
                .collect(Collectors.toList());
        return vehiculoResponses;
    }
     */


}
