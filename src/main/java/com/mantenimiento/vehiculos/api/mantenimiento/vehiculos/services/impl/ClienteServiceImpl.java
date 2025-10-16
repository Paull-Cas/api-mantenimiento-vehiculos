package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteJuridicoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteNaturalRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteJuridicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteNaturalResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.ClienteNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.ClienteService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClienteServiceImpl implements ClienteService {

    @Override
    public ClienteNaturalResponse createClienteNatural(ClienteNaturalRequest clienteNaturalRequest) {
        return null;
    }

    @Override
    public List<ClienteNaturalResponse> readAllClienteNatural() {
        return List.of();
    }

    @Override
    public ClienteNaturalResponse readClienteNaturalById(Long id) throws ClienteNoEncontradoExcepcion {
        return null;
    }

    @Override
    public ClienteNaturalResponse updateClienteNatural(Long id, ClienteNaturalRequest clienteRequest) throws ClienteNoEncontradoExcepcion {
        return null;
    }

    @Override
    public ClienteJuridicoResponse createClienteJuridico(ClienteJuridicoRequest clienteJuridicoRequest) {
        return null;
    }

    @Override
    public List<ClienteJuridicoResponse> readAllClienteJuridico() {
        return List.of();
    }

    @Override
    public ClienteJuridicoResponse readClienteJuridicoById(Long id) throws ClienteNoEncontradoExcepcion {
        return null;
    }

    @Override
    public ClienteJuridicoResponse updateClienteJuridico(Long id, ClienteJuridicoRequest clienteRequest) throws ClienteNoEncontradoExcepcion {
        return null;
    }

    @Override
    public List<ClienteResponse> readAllCliente() {
        return List.of();
    }

    @Override
    public void delete(Long id) throws ClienteNoEncontradoExcepcion {

    }
}
