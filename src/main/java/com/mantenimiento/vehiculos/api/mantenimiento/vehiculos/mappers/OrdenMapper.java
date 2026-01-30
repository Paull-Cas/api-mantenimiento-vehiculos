package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.OrdenResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Orden;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrdenMapper {

    public OrdenResponse toResponse(Orden orden) {
        return OrdenResponse.builder()
                .id(orden.getId())
                .numeroOrden(orden.getNumeroOrden())
                .vehiculoId(orden.getVehiculo().getId())
                .placaVehiculo(orden.getVehiculo().getPlaca())
                .fechaIngreso(orden.getFechaIngreso())
                .fechaCierre(orden.getFechaCierre())
                .estado(orden.getEstado())
                .total(orden.getTotal())
                .mantenimientos(orden.getOrdenesMantenimiento() != null
                        ? orden.getOrdenesMantenimiento().stream()
                        .map(OrdenMantenimientoMapper::toResponse)
                        .collect(Collectors.toList())
                        : null)
                .mecanicosAsignados(orden.getOrdenesMecanicos() != null
                        ? orden.getOrdenesMecanicos().stream()
                        .map(OrdenMecanicoMapper::toMecanicoAsignadoResponse)
                        .collect(Collectors.toList())
                        : null)
                .pagado(orden.getPago() != null && orden.getPago().isConfirmado())
                .build();
    }

    public OrdenResponse mapearOrden(Orden orden) {
        return toResponse(orden);
    }
}