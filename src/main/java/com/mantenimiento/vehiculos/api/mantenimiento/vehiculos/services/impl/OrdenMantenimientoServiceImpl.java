package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.OrdenMantenimientoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.OrdenMantenimientoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.OrdenMantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Vehiculo;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.OrdenMantenimientoNoEncontradaExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.OrdenMantenimientoMapper;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.MantenimientoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.OrdenMantenimientoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.VehiculoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.OrdenMantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenMantenimientoServiceImpl implements OrdenMantenimientoService {

    @Autowired
    private VehiculoRepositorio vehiculoRepositorio;

    @Autowired
    private MantenimientoRepositorio mantenimientoRepositorio;

    @Autowired
    private OrdenMantenimientoRepositorio ordenMantenimientoRepositorio;

    @Override
    public OrdenMantenimientoResponse create(OrdenMantenimientoRequest ordenMantenimientoRequest) {
        Vehiculo vehiculo = vehiculoRepositorio.getReferenceById(ordenMantenimientoRequest.getIdVehiculo());
        Mantenimiento mantenimiento = mantenimientoRepositorio.getReferenceById(ordenMantenimientoRequest.getIdMantenimiento());
        OrdenMantenimiento ordenMantenimiento = OrdenMantenimientoMapper.toEntity(ordenMantenimientoRequest, vehiculo, mantenimiento);
        OrdenMantenimiento ordenMantenimientoBD = ordenMantenimientoRepositorio.save(ordenMantenimiento);
        return OrdenMantenimientoMapper.toResponse(ordenMantenimientoBD);
    }

    @Override
    public List<OrdenMantenimientoResponse> readAll(String keyword, int page, int size) {
        Page<OrdenMantenimiento> ordenesMantenimiento = ordenMantenimientoRepositorio.searchOrdenMantenimiento(keyword, PageRequest.of(page, size));
        List<OrdenMantenimientoResponse> ordenesMantenimientoResponse = OrdenMantenimientoMapper.toListResponse(ordenesMantenimiento);
        return ordenesMantenimientoResponse;
    }

    @Override
    public OrdenMantenimientoResponse readById(Long id) {
        OrdenMantenimiento ordenMantenimiento = ordenMantenimientoRepositorio.getReferenceById(id);
        OrdenMantenimientoResponse ordenMantenimientoResponse = OrdenMantenimientoMapper.toResponse(ordenMantenimiento);
        return ordenMantenimientoResponse;
    }

    @Override
    public OrdenMantenimientoResponse update(Long id, OrdenMantenimientoRequest ordenMantenimientoRequest) {
        if (ordenMantenimientoRepositorio.existsById(id)) {
            OrdenMantenimiento ordenMantenimientoActual = ordenMantenimientoRepositorio.getReferenceById(id);
            Vehiculo vehiculo = vehiculoRepositorio.getReferenceById(ordenMantenimientoRequest.getIdVehiculo());
            Mantenimiento mantenimiento = mantenimientoRepositorio.getReferenceById(ordenMantenimientoRequest.getIdMantenimiento());
            OrdenMantenimiento ordenMantenimiento = OrdenMantenimientoMapper.toEntity(ordenMantenimientoRequest, vehiculo, mantenimiento);
            ordenMantenimiento.setId(ordenMantenimientoActual.getId());
            ordenMantenimiento.setFechaIngreso(ordenMantenimientoActual.getFechaIngreso());
            OrdenMantenimiento ordenMantenimientoDB = ordenMantenimientoRepositorio.save(ordenMantenimiento);
            return OrdenMantenimientoMapper.toResponse(ordenMantenimientoDB);
        }
        else throw new OrdenMantenimientoNoEncontradaExcepcion("Orden Mantenimiento con id: " + id + " no encontrado");
    }

    @Override
    public void delete(Long id) {
        if (ordenMantenimientoRepositorio.existsById(id)) {
            ordenMantenimientoRepositorio.deleteById(id);
        }
        else throw new OrdenMantenimientoNoEncontradaExcepcion("Orden Mantenimiento con id: " + id + " no encontrado");
    }
}
