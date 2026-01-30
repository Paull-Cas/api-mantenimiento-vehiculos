package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.TipoMantenimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MantenimientoRequest {
    private String nombre;
    private String descripcion;
    private TipoMantenimiento tipo;
    private BigDecimal precioMinimo;
    private boolean activo;
}