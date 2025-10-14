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
    List<ClienteNaturalResponse> readAllClienteNatural();
    ClienteNaturalResponse readClienteNaturalById(Long id) throws ClienteNoEncontradoExcepcion;
    ClienteNaturalResponse updateClienteNatural(Long id, ClienteNaturalRequest clienteRequest) throws ClienteNoEncontradoExcepcion;

    ClienteJuridicoResponse createClienteJuridico(ClienteJuridicoRequest clienteJuridicoRequest);
    List<ClienteJuridicoResponse> readAllClienteJuridico();
    ClienteJuridicoResponse readClienteJuridicoById(Long id) throws ClienteNoEncontradoExcepcion;
    ClienteJuridicoResponse updateClienteJuridico(Long id, ClienteJuridicoRequest clienteRequest) throws ClienteNoEncontradoExcepcion;


    List<ClienteResponse> readAllCliente();
    void delete(Long id) throws ClienteNoEncontradoExcepcion;
}
