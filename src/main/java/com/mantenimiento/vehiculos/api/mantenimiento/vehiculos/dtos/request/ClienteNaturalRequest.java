package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteNaturalRequest extends ClienteRequest {
    private String nombre;
    private String apellido;
    private String dni;
}
