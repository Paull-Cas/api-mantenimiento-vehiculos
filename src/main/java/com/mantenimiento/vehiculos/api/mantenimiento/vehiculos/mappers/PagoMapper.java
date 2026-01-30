package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.PagoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    public PagoResponse toResponse(Pago pago) {
        return PagoResponse.builder()
                .id(pago.getId())
                .ordenId(pago.getOrden() != null ? pago.getOrden().getId() : null)
                .numeroOrden(pago.getOrden() != null ? pago.getOrden().getNumeroOrden() : null)
                .montoTotal(pago.getMontoTotal())
                .metodoPago(pago.getMetodoPago())
                .fechaPago(pago.getFechaPago())
                .confirmado(pago.isConfirmado())
                .build();
    }
}
