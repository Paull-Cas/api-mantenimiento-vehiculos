package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoOrden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenResponse {
    
    private Long id;
    private String numeroOrden;
    private Long vehiculoId;
    private String placaVehiculo;
    private LocalDate fechaIngreso;
    private LocalDate fechaCierre;
    private EstadoOrden estado;
    private BigDecimal total;
    private List<OrdenMantenimientoResponse> mantenimientos;
    private List<MecanicoAsignadoResponse> mecanicosAsignados;
    private boolean pagado;
}
