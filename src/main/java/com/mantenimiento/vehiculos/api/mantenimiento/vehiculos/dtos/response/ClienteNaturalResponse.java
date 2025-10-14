package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteNaturalResponse extends ClienteResponse {
    private String nombre;
    private String apellido;
    private String dni;
}
