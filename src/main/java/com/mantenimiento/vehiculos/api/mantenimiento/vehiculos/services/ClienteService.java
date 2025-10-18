package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteJuridicoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteNaturalRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteJuridicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteNaturalResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.ClienteNoEncontradoExcepcion;

import java.util.List;

public interface ClienteService {
    ClienteNaturalResponse createClienteNatural(ClienteNaturalRequest clienteNaturalRequest);
    ClienteNaturalResponse updateClienteNatural(Long id, ClienteNaturalRequest clienteRequest) throws ClienteNoEncontradoExcepcion;

    ClienteJuridicoResponse createClienteJuridico(ClienteJuridicoRequest clienteJuridicoRequest);
    ClienteJuridicoResponse updateClienteJuridico(Long id, ClienteJuridicoRequest clienteRequest) throws ClienteNoEncontradoExcepcion;


    List<ClienteResponse> readAllClientes(String tipo);
    ClienteResponse readClienteById(Long id) throws ClienteNoEncontradoExcepcion;
    void delete(Long id) throws ClienteNoEncontradoExcepcion;
}
