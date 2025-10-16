package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;


public class VehiculoRequest {
    private String placa;
    private String marca;
    private String modelo;
    private int año;
    private String color;
    private String tipoCombustible;
    private String numeroSerie;
    private int kilometraje;
    private ClienteRequest propietarioId;
}
