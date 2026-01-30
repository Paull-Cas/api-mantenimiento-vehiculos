package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoOrdenMantenimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarEstadoOrdenMantenimientoRequest {
    
    private Long ordenMantenimientoId;
    private EstadoOrdenMantenimiento nuevoEstado;
    private BigDecimal costo; // Opcional: para cuando se cotiza
    private String observaciones;
}
