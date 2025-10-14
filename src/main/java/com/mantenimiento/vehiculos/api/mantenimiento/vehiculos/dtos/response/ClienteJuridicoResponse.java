package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteJuridicoResponse extends ClienteResponse {
    private String razonSocial;
    private String ruc;
    private String representanteLegal;
}
