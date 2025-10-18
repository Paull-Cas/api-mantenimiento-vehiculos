package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VehiculoRequest {
    private String placa;
    private String marca;
    private String modelo;
    private int año;
    private String color;
    private String tipoCombustible;
    private String numeroSerie;
    private int kilometraje;
    private Long propietarioId;
}
