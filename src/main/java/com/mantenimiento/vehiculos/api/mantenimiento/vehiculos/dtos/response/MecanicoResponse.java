package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoMecanico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MecanicoResponse {

    private Long id;
    private Long usuarioId;
    private String nombre;
    private String apellido;
    private String nombreCompleto;
    private String especialidad;
    private String telefono;
    private EstadoMecanico estado;
    private boolean activo;
    private LocalDate fechaIngreso;
    private Long tallerId;
}
