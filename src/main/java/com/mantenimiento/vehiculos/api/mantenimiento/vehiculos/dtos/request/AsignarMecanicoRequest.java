package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignarMecanicoRequest {
    
    private Long ordenId;
    private List<Long> mecanicosIds; // Lista de IDs de mecánicos a asignar
}
