package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoMantenimiento;
import lombok.Data;

@Data
public class ActualizarEstadoMantenimientoRequestDTO {
    private EstadoMantenimiento nuevoEstado;
}
