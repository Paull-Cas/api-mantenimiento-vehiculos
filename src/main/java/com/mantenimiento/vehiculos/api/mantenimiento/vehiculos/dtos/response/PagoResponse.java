package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response;

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
public class PagoResponse {
    
    private Long id;
    private Long ordenId;
    private String numeroOrden;
    private BigDecimal montoTotal;
    private String metodoPago;
    private LocalDate fechaPago;
    private boolean confirmado;
}
