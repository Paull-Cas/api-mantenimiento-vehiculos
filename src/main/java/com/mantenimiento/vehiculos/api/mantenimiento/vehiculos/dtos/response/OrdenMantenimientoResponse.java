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
    private Long ordenId;
    private String numeroOrden;
    private Long mantenimientoId;
    private String nombreMantenimiento;
    private String descripcionProblema;
    private EstadoOrdenMantenimiento estado;
    private BigDecimal costo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
