package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ClienteRequest {
    private String telefono;
    private String correoElectronico;
    private String direccion;
    private String tipo;
}
