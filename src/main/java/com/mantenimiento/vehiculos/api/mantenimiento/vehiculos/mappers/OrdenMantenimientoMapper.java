package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.OrdenMantenimientoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.OrdenMantenimiento;

public class OrdenMantenimientoMapper {

    public static OrdenMantenimientoResponse toResponse(OrdenMantenimiento om) {
        return OrdenMantenimientoResponse.builder()
                .id(om.getId())
                .ordenId(om.getOrden() != null ? om.getOrden().getId() : null)
                .numeroOrden(om.getOrden() != null ? om.getOrden().getNumeroOrden() : null)
                .mantenimientoId(om.getMantenimiento() != null ? om.getMantenimiento().getId() : null)
                .nombreMantenimiento(om.getMantenimiento() != null ? om.getMantenimiento().getNombre() : null)
                .descripcionProblema(om.getDescripcionProblema())
                .estado(om.getEstado())
                .costo(om.getCosto())
                .fechaInicio(om.getFechaInicio())
                .fechaFin(om.getFechaFin())
                .build();
    }
}
