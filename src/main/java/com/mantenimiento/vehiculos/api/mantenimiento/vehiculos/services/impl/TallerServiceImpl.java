package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.TallerRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.TallerResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Taller;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.TallerMapper;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.MantenimientoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.TallerRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.TallerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class TallerServiceImpl implements TallerService {

    @Autowired
    private TallerRepositorio tallerRepositorio;

    @Autowired
    private MantenimientoRepositorio mantenimientoRepositorio;

    @Autowired
    private TallerMapper tallerMapper;

    @Override
    public TallerResponseDTO crearTaller(TallerRequest request) {
        log.info("Creando nuevo taller: {}", request.getNombre());

        Taller taller = tallerMapper.toEntity(request);
        Taller tallerGuardado = tallerRepositorio.save(taller);

        if (request.getMantenimientosIds() != null && !request.getMantenimientosIds().isEmpty()) {
            List<Mantenimiento> mantenimientos = mantenimientoRepositorio
                    .findAllById(request.getMantenimientosIds());
            tallerGuardado.setMantenimientos(mantenimientos);
            tallerGuardado = tallerRepositorio.save(tallerGuardado);
        }

        return tallerMapper.toResponse(tallerGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public TallerResponseDTO obtenerTallerPorId(Long id) {
        Taller taller = tallerRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado con ID: " + id));
        return tallerMapper.toResponse(taller);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TallerResponseDTO> obtenerTodos() {
        return tallerRepositorio.findAll().stream()
                .map(tallerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TallerResponseDTO> buscarPorCiudad(String ciudad) {
        return tallerRepositorio.findByCiudadIgnoreCase(ciudad).stream()
                .map(tallerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TallerResponseDTO> buscarPorDistrito(String distrito) {
        return tallerRepositorio.findByDistritoIgnoreCase(distrito).stream()
                .map(tallerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TallerResponseDTO> buscarPorKeyword(String keyword) {
        return tallerRepositorio.buscarPorKeyword(keyword).stream()
                .map(tallerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TallerResponseDTO actualizarTaller(Long id, TallerRequest request) {
        log.info("Actualizando taller con ID: {}", id);

        Taller taller = tallerRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado con ID: " + id));

        taller.setNombre(request.getNombre());
        taller.setCiudad(request.getCiudad());
        taller.setDistrito(request.getDistrito());
        taller.setDireccion(request.getDireccion());
        taller.setHorario(request.getHorario());

        if (request.getMantenimientosIds() != null) {
            List<Mantenimiento> mantenimientos = mantenimientoRepositorio
                    .findAllById(request.getMantenimientosIds());
            taller.setMantenimientos(mantenimientos);
        }

        return tallerMapper.toResponse(tallerRepositorio.save(taller));
    }

    @Override
    public void eliminarTaller(Long id) {
        log.info("Eliminando taller con ID: {}", id);
        Taller taller = tallerRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado con ID: " + id));
        tallerRepositorio.delete(taller);
    }
}