package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ClienteRequest {
    @NotNull(message = "El usuario ID es obligatorio")
    private Long usuarioId;
    private String telefono;
    private String correoElectronico;
    private String direccion;
}
