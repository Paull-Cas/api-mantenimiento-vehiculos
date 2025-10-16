package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response;

import lombok.Data;

import java.util.Date;

@Data
public class MantenimientoPreventivoResponseDTO extends MantenimientoResponseDTO {
    private int kilometrajeRecomendado;
    private Date proximoMantenimientoSugerido;
    private boolean esCambioDeComponente;
}
