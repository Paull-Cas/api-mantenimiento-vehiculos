package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TallerResponseDTO {
    private Long id;
    private String nombre;
    private String ciudad;
    private String distrito;
    private String direccion;
    private String horario;
    private List<MantenimientoResponseDTO> mantenimientos;
}