package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoAsignacionMecanico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MecanicoAsignadoResponse {
    
    private Long id;
    private Long mecanicoId;
    private String nombreMecanico;
    private String especialidadMecanico;
    private LocalDate fechaAsignacion;
    private LocalDate fechaLiberacion;
    private EstadoAsignacionMecanico estado;
}
