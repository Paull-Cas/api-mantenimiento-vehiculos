package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoRequest {
    
    private Long ordenId;
    private String metodoPago; // Ej: EFECTIVO, TARJETA, TRANSFERENCIA
}
