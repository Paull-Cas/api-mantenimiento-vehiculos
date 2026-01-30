package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenMantenimientoRequest {

    private Long ordenId;
    private Long mantenimientoId;
    private String descripcionProblema;
}
