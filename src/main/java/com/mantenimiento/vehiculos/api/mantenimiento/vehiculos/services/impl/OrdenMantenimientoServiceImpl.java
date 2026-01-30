package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.OrdenMantenimientoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.OrdenMantenimientoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Orden;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.OrdenMantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoOrdenMantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.OrdenMantenimientoNoEncontradaExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.OrdenNoEncontradaExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.OrdenMantenimientoMapper;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.MantenimientoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.OrdenMantenimientoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.OrdenRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.OrdenMantenimientoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrdenMantenimientoServiceImpl implements OrdenMantenimientoService {

    private final OrdenMantenimientoRepositorio ordenMantenimientoRepositorio;
    private final OrdenRepositorio ordenRepositorio;
    private final MantenimientoRepositorio mantenimientoRepositorio;

    @Override
    public OrdenMantenimientoResponse create(OrdenMantenimientoRequest request) throws MantenimientoNoEncontradoExcepcion {
        log.info("Creando nuevo OrdenMantenimiento para orden ID: {}", request.getOrdenId());

        Orden orden = ordenRepositorio.findById(request.getOrdenId())
                .orElseThrow(() -> new OrdenNoEncontradaExcepcion(
                        "Orden no encontrada con ID: " + request.getOrdenId()));

        Mantenimiento mantenimiento = mantenimientoRepositorio.findById(request.getMantenimientoId())
                .orElseThrow(() -> new MantenimientoNoEncontradoExcepcion(
                        "Mantenimiento no encontrado con ID: " + request.getMantenimientoId()));

        if (!mantenimiento.isActivo()) {
            throw new IllegalStateException("El mantenimiento no está activo");
        }

        OrdenMantenimiento om = new OrdenMantenimiento();
        om.setOrden(orden);
        om.setMantenimiento(mantenimiento);
        om.setDescripcionProblema(request.getDescripcionProblema());
        om.setEstado(EstadoOrdenMantenimiento.EN_REVISION);
        om.setCosto(mantenimiento.getPrecioMinimo());

        OrdenMantenimiento omGuardado = ordenMantenimientoRepositorio.save(om);

        log.info("OrdenMantenimiento creado exitosamente con ID: {}", omGuardado.getId());
        return OrdenMantenimientoMapper.toResponse(omGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenMantenimientoResponse> readAll(String keyword, int page, int size) {
        Page<OrdenMantenimiento> ordenesPage = ordenMantenimientoRepositorio.searchOrdenMantenimiento(
                keyword, PageRequest.of(page, size));

        return ordenesPage.getContent().stream()
                .map(OrdenMantenimientoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenMantenimientoResponse readById(Long id) {
        OrdenMantenimiento om = ordenMantenimientoRepositorio.findById(id)
                .orElseThrow(() -> new OrdenMantenimientoNoEncontradaExcepcion(
                        "OrdenMantenimiento no encontrada con ID: " + id));

        return OrdenMantenimientoMapper.toResponse(om);
    }

    @Override
    public OrdenMantenimientoResponse update(Long id, OrdenMantenimientoRequest request) {
        log.info("Actualizando OrdenMantenimiento ID: {}", id);

        OrdenMantenimiento om = ordenMantenimientoRepositorio.findById(id)
                .orElseThrow(() -> new OrdenMantenimientoNoEncontradaExcepcion(
                        "OrdenMantenimiento no encontrada con ID: " + id));

        // Solo permitir actualización de descripción del problema
        // El estado se actualiza mediante OrdenService
        om.setDescripcionProblema(request.getDescripcionProblema());

        OrdenMantenimiento omActualizado = ordenMantenimientoRepositorio.save(om);

        log.info("OrdenMantenimiento actualizado exitosamente");
        return OrdenMantenimientoMapper.toResponse(omActualizado);
    }

    @Override
    public void delete(Long id) {
        log.info("Eliminando OrdenMantenimiento ID: {}", id);

        OrdenMantenimiento om = ordenMantenimientoRepositorio.findById(id)
                .orElseThrow(() -> new OrdenMantenimientoNoEncontradaExcepcion(
                        "OrdenMantenimiento no encontrada con ID: " + id));

        // Solo permitir eliminar si está en EN_REVISION o RECHAZADO
        if (om.getEstado() != EstadoOrdenMantenimiento.EN_REVISION &&
                om.getEstado() != EstadoOrdenMantenimiento.RECHAZADO) {
            throw new IllegalStateException(
                    "Solo se pueden eliminar OrdenMantenimiento en estado EN_REVISION o RECHAZADO");
        }

        ordenMantenimientoRepositorio.delete(om);
        log.info("OrdenMantenimiento eliminado exitosamente");
    }

    @Transactional(readOnly = true)
    public List<OrdenMantenimientoResponse> obtenerPorOrden(Long ordenId) {
        List<OrdenMantenimiento> ordenesMantenimiento = ordenMantenimientoRepositorio.findByOrdenId(ordenId);
        return ordenesMantenimiento.stream()
                .map(OrdenMantenimientoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
