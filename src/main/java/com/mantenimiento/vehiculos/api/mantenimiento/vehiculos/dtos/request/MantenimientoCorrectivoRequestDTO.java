package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.TipoMantenimiento;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MantenimientoCorrectivoRequestDTO {
    private Long ordenMantenimientoId;
    private TipoMantenimiento tipoMantenimiento;
    private String descripcion;
    private double costo;
    private BigDecimal tiempoEmpleadoHoras;

    private String diagnosticoInicial;
    private String causaDeFallaDetectada;
    private boolean esReparacionUrgente;
}
