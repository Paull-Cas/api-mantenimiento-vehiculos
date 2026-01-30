package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.OrdenMantenimientoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.OrdenMantenimientoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.OrdenMantenimientoNoEncontradaExcepcion;

import java.util.List;

public interface OrdenMantenimientoService {
    OrdenMantenimientoResponse create(OrdenMantenimientoRequest ordenMantenimientoRequest) throws MantenimientoNoEncontradoExcepcion;
    List<OrdenMantenimientoResponse> readAll(String nroOrden, int page, int size);
    OrdenMantenimientoResponse readById(Long id) throws OrdenMantenimientoNoEncontradaExcepcion;
    OrdenMantenimientoResponse update(Long id, OrdenMantenimientoRequest ordenMantenimientoRequest) throws OrdenMantenimientoNoEncontradaExcepcion;
    void delete(Long id) throws OrdenMantenimientoNoEncontradaExcepcion;
}
