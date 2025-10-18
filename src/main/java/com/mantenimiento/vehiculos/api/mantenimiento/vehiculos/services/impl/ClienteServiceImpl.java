package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteJuridicoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteNaturalRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteJuridicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteNaturalResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Cliente;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.ClienteJuridico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.ClienteNatural;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.ClienteNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.ClienteMapper;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.ClienteRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Override
    public ClienteNaturalResponse createClienteNatural(ClienteNaturalRequest clienteNaturalRequest) {
        ClienteNatural clienteNatural = ClienteMapper.toClienteNaturalEntity(clienteNaturalRequest);
        ClienteNatural clienteNaturalBD = clienteRepositorio.save(clienteNatural);
        return ClienteMapper.toClienteNaturalResponse(clienteNaturalBD);
    }

    @Override
    public ClienteNaturalResponse updateClienteNatural(Long id, ClienteNaturalRequest clienteRequest) throws ClienteNoEncontradoExcepcion {
        if (clienteRepositorio.existsById(id)) {
            ClienteNatural clienteNatural = ClienteMapper.toClienteNaturalEntity(clienteRequest);
            clienteNatural.setId(id);
            ClienteNatural clienteNaturalBD = clienteRepositorio.save(clienteNatural);
            return ClienteMapper.toClienteNaturalResponse(clienteNatural);
        }
        else throw new ClienteNoEncontradoExcepcion("Cliente no encontrado");
    }

    @Override
    public ClienteJuridicoResponse createClienteJuridico(ClienteJuridicoRequest clienteJuridicoRequest) {
        ClienteJuridico clienteJuridico = ClienteMapper.toClienteJuridicoEntity(clienteJuridicoRequest);
        ClienteJuridico clienteJuridicoBD = clienteRepositorio.save(clienteJuridico);
        return ClienteMapper.toClienteJuridicoResponse(clienteJuridicoBD);
    }


    @Override
    public ClienteJuridicoResponse updateClienteJuridico(Long id, ClienteJuridicoRequest clienteRequest) throws ClienteNoEncontradoExcepcion {
        if (clienteRepositorio.existsById(id)) {
            ClienteJuridico clienteJuridico = ClienteMapper.toClienteJuridicoEntity(clienteRequest);
            clienteJuridico.setId(id);
            ClienteJuridico clienteJuridicoBD = clienteRepositorio.save(clienteJuridico);
            return ClienteMapper.toClienteJuridicoResponse(clienteJuridicoBD);
        }
        else throw new ClienteNoEncontradoExcepcion("Cliente no encontrado");
    }

    @Override
    public List<ClienteResponse> readAllClientes(String tipo) {
        List<Cliente> clientes;
        if (tipo == null || tipo.isEmpty()) {
            clientes = clienteRepositorio.findAll();
        } else {
            clientes = clienteRepositorio.findByTipo(tipo);
        }

        List<ClienteResponse> clienteResponses = ClienteMapper.toClienteResponseList(clientes);
        return clienteResponses;
    }

    @Override
    public ClienteResponse readClienteById(Long id) throws ClienteNoEncontradoExcepcion {
        Cliente cliente = clienteRepositorio.findById(id).get();
        ClienteResponse clienteResponse = new ClienteResponse();
        if (cliente instanceof ClienteNatural) {
            ClienteNatural clienteNatural = (ClienteNatural) cliente;
            clienteResponse = ClienteMapper.toClienteNaturalResponse(clienteNatural);
        } else if (cliente instanceof ClienteJuridico) {
            ClienteJuridico clienteJuridico = (ClienteJuridico) cliente;
            clienteResponse = ClienteMapper.toClienteJuridicoResponse(clienteJuridico);
        }
        return clienteResponse;
    }

    @Override
    public void delete(Long id) throws ClienteNoEncontradoExcepcion {
        if (clienteRepositorio.existsById(id)) {
            clienteRepositorio.deleteById(id);
        }
        else throw new ClienteNoEncontradoExcepcion("Cliente no encontrado");
    }
}
