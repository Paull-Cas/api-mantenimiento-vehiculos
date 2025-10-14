package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.response;

import lombok.Data;

@Data
public class VehiculoDto {
    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private int año;
    private String color;
    private String tipoCombustible;
    private String numeroSerie;
    private int kilometraje;
    private ClienteDto propietarioId;
}
