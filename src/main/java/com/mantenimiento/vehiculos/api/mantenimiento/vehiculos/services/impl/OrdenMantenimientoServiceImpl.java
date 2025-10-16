package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.OrdenMantenimientoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.OrdenMantenimientoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.OrdenMantenimientoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenMantenimientoServiceImpl implements OrdenMantenimientoService {

    @Override
    public OrdenMantenimientoResponse create(OrdenMantenimientoRequest ordenMantenimientoRequest) {
        return null;
    }

    @Override
    public List<OrdenMantenimientoResponse> readAll() {
        return List.of();
    }

    @Override
    public OrdenMantenimientoResponse readById(Long id) {
        return null;
    }

    @Override
    public OrdenMantenimientoResponse update(Long id, OrdenMantenimientoRequest ordenMantenimientoRequest) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
