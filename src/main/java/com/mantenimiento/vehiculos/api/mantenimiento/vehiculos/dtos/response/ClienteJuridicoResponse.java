package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ClienteJuridicoResponse extends ClienteResponse {
    private String razonSocial;
    private String ruc;
    private String representanteLegal;
}
