package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MecanicoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MecanicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mecanico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoMecanico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MecanicoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.MecanicoMapper;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.MecanicoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.UsuarioRepository;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.MecanicoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MecanicoServiceImpl implements MecanicoService {

    @Autowired
    private final MecanicoRepositorio mecanicoRepositorio;

    private final MecanicoMapper mecanicoMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public MecanicoResponse crearMecanico(MecanicoRequest request) {
        if (!verificarExistenciaUsuario(request.getUsuarioId())) throw new EntityNotFoundException("Usuario no encontrado");
        Mecanico mecanico = mecanicoMapper.toEntity(request);
        mecanico.setEstado(EstadoMecanico.DISPONIBLE);
        mecanico.setActivo(true);
        
        Mecanico mecanicoGuardado = mecanicoRepositorio.save(mecanico);

        return mecanicoMapper.toResponse(mecanicoGuardado);
    }

    @Override
    public MecanicoResponse actualizarMecanico(Long id, MecanicoRequest request) {
        if (!verificarExistenciaUsuario(request.getUsuarioId())) throw new EntityNotFoundException("Usuario no encontrado");;
        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion(
                        "Mecánico no encontrado con ID: " + id));
        
        mecanico.setNombre(request.getNombre());
        mecanico.setApellido(request.getApellido());
        mecanico.setEspecialidad(request.getEspecialidad());
        mecanico.setTelefono(request.getTelefono());
        mecanico.setFechaIngreso(request.getFechaIngreso());
        
        Mecanico mecanicoActualizado = mecanicoRepositorio.save(mecanico);

        return mecanicoMapper.toResponse(mecanicoActualizado);
    }

    private boolean verificarExistenciaUsuario(Long idUsuario) {
        return usuarioRepository.existsById(idUsuario);
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
    }

    @Override
    public void activarMecanico(Long id) {
        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion(
                        "Mecánico no encontrado con ID: " + id));
        
        mecanico.setActivo(true);
        mecanico.setEstado(EstadoMecanico.DISPONIBLE);
        mecanicoRepositorio.save(mecanico);

    }

    @Override
    public void cambiarEstado(Long id, EstadoMecanico nuevoEstado) {
        log.info("Cambiando estado de mecánico ID: {} a {}", id, nuevoEstado);
        
        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion(
                        "Mecánico no encontrado con ID: " + id));
        
        mecanico.setEstado(nuevoEstado);
        mecanicoRepositorio.save(mecanico);

    }

    @Override
    public void eliminarMencanico(Long id) {
        if (!mecanicoRepositorio.existsById(id)) {
            throw new EntityNotFoundException("No existe el mecánico con id: " + id);
        }
        mecanicoRepositorio.deleteById(id);
    }
}
