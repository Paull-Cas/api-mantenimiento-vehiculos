package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

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

    private Long usuarioId;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String telefono;
    private LocalDate fechaIngreso;
}
