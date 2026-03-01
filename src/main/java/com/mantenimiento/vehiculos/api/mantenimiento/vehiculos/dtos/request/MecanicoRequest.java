package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MecanicoRequest {
    @NotNull(message = "El usuario ID es obligatorio")
    private Long usuarioId;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String telefono;
    private LocalDate fechaIngreso;
    private Long tallerId;
}
