package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.TipoMantenimiento;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MantenimientoResponseDTO {
    private Long id;
    private String tipoServicio;
    private TipoMantenimiento tipoMantenimiento;
    private String descripcion;
    private BigDecimal tiempoEmpleadoHoras;
}
