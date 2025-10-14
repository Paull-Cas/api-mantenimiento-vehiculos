package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteResponse {
    private Long id;
    private String telefono;
    private String correoElectronico;
    private String direccion;
    private String tipo;
}
