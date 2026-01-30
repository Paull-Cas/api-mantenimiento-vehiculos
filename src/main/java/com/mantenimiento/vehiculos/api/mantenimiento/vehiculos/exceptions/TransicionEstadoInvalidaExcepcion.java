package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions;

public class TransicionEstadoInvalidaExcepcion extends RuntimeException {
    public TransicionEstadoInvalidaExcepcion(String message) {
        super(message);
    }
}
