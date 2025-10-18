package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.OrdenMantenimientoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.OrdenMantenimientoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.OrdenMantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Vehiculo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class OrdenMantenimientoMapper {

    public static OrdenMantenimiento toEntity(OrdenMantenimientoRequest ordenMantenimientoRequest, Vehiculo vehiculo, Mantenimiento mantenimiento) {
        OrdenMantenimiento ordenMantenimiento = new OrdenMantenimiento();
        BeanUtils.copyProperties(ordenMantenimientoRequest, ordenMantenimiento);
        ordenMantenimiento.setVehiculo(vehiculo);
        ordenMantenimiento.setMantenimiento(mantenimiento);
        return ordenMantenimiento;
    }

    public static OrdenMantenimientoResponse toResponse(OrdenMantenimiento ordenMantenimiento) {
        OrdenMantenimientoResponse ordenMantenimientoResponse = new OrdenMantenimientoResponse();
        BeanUtils.copyProperties(ordenMantenimiento, ordenMantenimientoResponse);
        ordenMantenimientoResponse.setIdMantenimiento(ordenMantenimiento.getId());
        ordenMantenimientoResponse.setIdVehiculo(ordenMantenimiento.getId());
        return ordenMantenimientoResponse;
    }

    public static List<OrdenMantenimientoResponse> toListResponse(Page<OrdenMantenimiento> ordenMantenimientoList) {
        return ordenMantenimientoList.stream()
                .map(ordenMantenimiento -> toResponse(ordenMantenimiento))
                .collect(Collectors.toList());
    }
}
