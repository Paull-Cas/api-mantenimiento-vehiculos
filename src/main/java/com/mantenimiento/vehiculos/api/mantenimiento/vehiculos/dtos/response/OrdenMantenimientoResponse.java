package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoOrdenMantenimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenMantenimientoResponse {

    private Long id;

    private String numeroOrden;

    private Long idVehiculo;

    private Long idMantenimiento;

    private LocalDate fechaIngreso;

    private LocalDate fechaEntregaEstimada;

    private String descripcionProblema;

    private EstadoOrdenMantenimiento estado;

    private BigDecimal costoTotal;

    private String observaciones;

    private LocalDate fechaUltimaActualizacion;
}
