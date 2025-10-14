package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteJuridicoRequest extends ClienteRequest {
    private String razonSocial;
    private String ruc;
    private String representanteLegal;
}
