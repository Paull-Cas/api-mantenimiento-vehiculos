package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response;

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
public class MantenimientoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private TipoMantenimiento tipo;
    private BigDecimal precioMinimo;
    private boolean activo;
}