package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MecanicoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MecanicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mecanico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoMecanico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MecanicoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.MecanicoMapper;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.MecanicoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.MecanicoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MecanicoServiceImpl implements MecanicoService {

    private final MecanicoRepositorio mecanicoRepositorio;
    private final MecanicoMapper mecanicoMapper;

    @Override
    public MecanicoResponse crearMecanico(MecanicoRequest request) {
        log.info("Creando nuevo mecánico: {} {}", request.getNombre(), request.getApellido());
        
        Mecanico mecanico = mecanicoMapper.toEntity(request);
        mecanico.setEstado(EstadoMecanico.DISPONIBLE);
        mecanico.setActivo(true);
        
        Mecanico mecanicoGuardado = mecanicoRepositorio.save(mecanico);
        
        log.info("Mecánico creado exitosamente con ID: {}", mecanicoGuardado.getId());
        return mecanicoMapper.toResponse(mecanicoGuardado);
    }

    @Override
    public MecanicoResponse actualizarMecanico(Long id, MecanicoRequest request) {
        log.info("Actualizando mecánico ID: {}", id);
        
        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion(
                        "Mecánico no encontrado con ID: " + id));
        
        mecanico.setNombre(request.getNombre());
        mecanico.setApellido(request.getApellido());
        mecanico.setEspecialidad(request.getEspecialidad());
        mecanico.setTelefono(request.getTelefono());
        mecanico.setFechaIngreso(request.getFechaIngreso());
        
        Mecanico mecanicoActualizado = mecanicoRepositorio.save(mecanico);
        
        log.info("Mecánico actualizado exitosamente");
        return mecanicoMapper.toResponse(mecanicoActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public MecanicoResponse obtenerMecanicoPorId(Long id) {
        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion(
                        "Mecánico no encontrado con ID: " + id));
        return mecanicoMapper.toResponse(mecanico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MecanicoResponse> obtenerTodosMecanicos() {
        return mecanicoRepositorio.findAll().stream()
                .map(mecanicoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MecanicoResponse> obtenerMecanicosDisponibles() {
        return mecanicoRepositorio.findByEstadoAndActivoTrue(EstadoMecanico.DISPONIBLE).stream()
                .map(mecanicoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MecanicoResponse> buscarPorEspecialidad(String especialidad) {
        return mecanicoRepositorio.findByEspecialidadContainingIgnoreCaseAndActivoTrue(especialidad).stream()
                .map(mecanicoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void desactivarMecanico(Long id) {
        log.info("Desactivando mecánico ID: {}", id);
        
        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion(
                        "Mecánico no encontrado con ID: " + id));
        
        if (mecanico.getEstado() == EstadoMecanico.OCUPADO) {
            throw new IllegalStateException(
                    "No se puede desactivar un mecánico que está ocupado. Finalice sus trabajos primero.");
        }
        
        mecanico.setActivo(false);
        mecanico.setEstado(EstadoMecanico.INACTIVO);
        mecanicoRepositorio.save(mecanico);
        
        log.info("Mecánico desactivado exitosamente");
    }

    @Override
    public void activarMecanico(Long id) {
        log.info("Activando mecánico ID: {}", id);
        
        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion(
                        "Mecánico no encontrado con ID: " + id));
        
        mecanico.setActivo(true);
        mecanico.setEstado(EstadoMecanico.DISPONIBLE);
        mecanicoRepositorio.save(mecanico);
        
        log.info("Mecánico activado exitosamente");
    }

    @Override
    public void cambiarEstado(Long id, EstadoMecanico nuevoEstado) {
        log.info("Cambiando estado de mecánico ID: {} a {}", id, nuevoEstado);
        
        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion(
                        "Mecánico no encontrado con ID: " + id));
        
        mecanico.setEstado(nuevoEstado);
        mecanicoRepositorio.save(mecanico);
        
        log.info("Estado del mecánico actualizado exitosamente");
    }
}
