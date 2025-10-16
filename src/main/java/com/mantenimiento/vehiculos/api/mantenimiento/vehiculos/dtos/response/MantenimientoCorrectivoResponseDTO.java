package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response;

import lombok.Data;

@Data
public class MantenimientoCorrectivoResponseDTO extends MantenimientoResponseDTO {
    private String diagnosticoInicial;
    private String causaDeFallaDetectada;
    private boolean esReparacionUrgente;
}
