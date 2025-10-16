package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ActualizarEstadoMantenimientoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoCorrectivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoPreventivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.MantenimientoCorrectivo;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.MantenimientoPreventivo;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.OrdenMantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoMantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.OrdenMantenimientoNoEncontradaExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.MantenimientoMapper;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.MantenimientoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.OrdenMantenimientoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.MantenimientoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class MantenimientoServiceImpl implements MantenimientoService {

    @Autowired
    MantenimientoRepositorio mantenimientoRepositorio;

    @Autowired
    OrdenMantenimientoRepositorio ordenMantenimientoRepositorio;

    @Autowired
    MantenimientoMapper mantenimientoMapper;

    @Override
    public MantenimientoResponseDTO saveMantenimientoPreventivo(MantenimientoPreventivoRequestDTO mantenimientoPreventivoRequestDTO) throws OrdenMantenimientoNoEncontradaExcepcion {
        log.info("Guardar mantenimiento preventivo...");
        OrdenMantenimiento ordenMantenimiento = ordenMantenimientoRepositorio.findById(mantenimientoPreventivoRequestDTO.getOrdenMantenimientoId()).orElse(null);
        if(ordenMantenimiento == null){
            throw new OrdenMantenimientoNoEncontradaExcepcion("Orden de mantenimiento no encontrada");
        }
        MantenimientoPreventivo mantenimientoPreventivo = mantenimientoMapper.mapearMantenimientoPreventivoRequestDTO(mantenimientoPreventivoRequestDTO);

        mantenimientoPreventivo.setOrdenMantenimiento(ordenMantenimiento);
        mantenimientoPreventivo.setEstadoMantenimiento(EstadoMantenimiento.PENDIENTE);
        mantenimientoPreventivo.setFechaRegistro(new Date());

        MantenimientoPreventivo mantPreventivoBD = mantenimientoRepositorio.save(mantenimientoPreventivo);
        return mantenimientoMapper.mapearMantenimiento(mantPreventivoBD);
    }

    @Override
    public MantenimientoResponseDTO saveMantenimientoCorrectivo(MantenimientoCorrectivoRequestDTO mantenimientoCorrectivoRequestDTO) throws OrdenMantenimientoNoEncontradaExcepcion {
        log.info("Guardar mantenimiento correctivo...");
        OrdenMantenimiento ordenMantenimiento = ordenMantenimientoRepositorio.findById(mantenimientoCorrectivoRequestDTO.getOrdenMantenimientoId()).orElse(null);
        if(ordenMantenimiento == null){
            throw new OrdenMantenimientoNoEncontradaExcepcion("Orden de mantenimiento no encontrada");
        }
        MantenimientoCorrectivo mantenimientoCorrectivo = mantenimientoMapper.mapearMantenimientoCorrectivoRequestDTO(mantenimientoCorrectivoRequestDTO);

        mantenimientoCorrectivo.setOrdenMantenimiento(ordenMantenimiento);
        mantenimientoCorrectivo.setEstadoMantenimiento(EstadoMantenimiento.PENDIENTE);
        mantenimientoCorrectivo.setFechaRegistro(new Date());

        MantenimientoCorrectivo mantCorrectivoBD = mantenimientoRepositorio.save(mantenimientoCorrectivo);
        return mantenimientoMapper.mapearMantenimiento(mantCorrectivoBD);
    }

    @Override
    public MantenimientoResponseDTO getMantenimientoById(Long mantenimientoId) throws MantenimientoNoEncontradoExcepcion {
        log.info("Buscando mantenimiento con ID: {}", mantenimientoId);
        Mantenimiento mantenimiento = mantenimientoRepositorio.findById(mantenimientoId)
                .orElseThrow(() -> new MantenimientoNoEncontradoExcepcion("Mantenimiento con ID " + mantenimientoId + " no encontrado."));

        return mantenimientoMapper.mapearMantenimiento(mantenimiento);
    }

    @Override
    public List<MantenimientoResponseDTO> getMantenimientosByOrdenId(Long ordenId) throws OrdenMantenimientoNoEncontradaExcepcion {
        log.info("Buscando todos los mantenimientos para la orden de mantenimiento solicitada");

        List<Mantenimiento> mantenimientos = mantenimientoRepositorio.findByOrdenMantenimientoId(ordenId);

        return mantenimientos.stream()
                .map(mantenimientoMapper::mapearMantenimiento)
                .collect(Collectors.toList());
    }

    @Override
    public MantenimientoResponseDTO updateEstadoMantenimiento(Long mantenimientoId, ActualizarEstadoMantenimientoRequestDTO actualizarEstadoMantenimientoRequestDTO) throws MantenimientoNoEncontradoExcepcion {
        log.info("Actualizando estado del mantenimiento ID: {} al nuevo estado: {}", mantenimientoId, actualizarEstadoMantenimientoRequestDTO.getNuevoEstado());
        Mantenimiento mantenimiento = mantenimientoRepositorio.findById(mantenimientoId)
                .orElseThrow(() -> new MantenimientoNoEncontradoExcepcion("Mantenimiento con ID " + mantenimientoId + " no encontrado."));
        mantenimiento.setEstadoMantenimiento(actualizarEstadoMantenimientoRequestDTO.getNuevoEstado());
        Mantenimiento mantenimientoActualizado = mantenimientoRepositorio.save(mantenimiento);

        return mantenimientoMapper.mapearMantenimiento(mantenimientoActualizado);
    }

    @Override
    public void deleteMantenimiento(Long mantenimientoId) throws MantenimientoNoEncontradoExcepcion {
        log.warn("Intentando eliminar el mantenimiento");

        boolean existe = mantenimientoRepositorio.existsById(mantenimientoId);
        if (!existe) {
            throw new MantenimientoNoEncontradoExcepcion("No se pudo eliminar. Mantenimiento con ID " + mantenimientoId + " no encontrado.");
        }

        mantenimientoRepositorio.deleteById(mantenimientoId);
    }
}
