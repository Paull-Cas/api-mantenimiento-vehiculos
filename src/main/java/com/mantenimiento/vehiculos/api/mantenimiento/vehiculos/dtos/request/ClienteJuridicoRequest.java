package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ClienteJuridicoRequest extends ClienteRequest {
    private String razonSocial;
    private String ruc;
    private String representanteLegal;
}
