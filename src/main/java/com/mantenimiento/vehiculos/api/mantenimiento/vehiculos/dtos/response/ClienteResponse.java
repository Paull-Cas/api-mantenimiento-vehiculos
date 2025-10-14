package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ClienteResponse {
    private Long id;
    private String telefono;
    private String correoElectronico;
    private String direccion;
    private String tipo;
}
