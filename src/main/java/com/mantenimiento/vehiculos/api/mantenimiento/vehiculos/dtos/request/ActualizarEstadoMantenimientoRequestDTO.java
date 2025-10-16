package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import lombok.Data;

@Data
public class ActualizarEstadoMantenimientoRequestDTO {
    private EstadoMantenimiento nuevoEstado;
}
