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
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.UsuarioRepository;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public ClienteNaturalResponse createClienteNatural(ClienteNaturalRequest clienteNaturalRequest) {
        if (!verificarExistenciaUsuario(clienteNaturalRequest.getUsuarioId())) return null;

        ClienteNatural clienteNatural = ClienteMapper.toClienteNaturalEntity(clienteNaturalRequest);
        ClienteNatural clienteNaturalBD = clienteRepositorio.save(clienteNatural);
        return ClienteMapper.toClienteNaturalResponse(clienteNaturalBD);
    }


    @Override
    public ClienteNaturalResponse updateClienteNatural(Long id, ClienteNaturalRequest clienteRequest) throws ClienteNoEncontradoExcepcion {
        if (clienteRepositorio.existsById(id)) {
            if (!verificarExistenciaUsuario(clienteRequest.getUsuarioId())) return null;
            ClienteNatural clienteNatural = ClienteMapper.toClienteNaturalEntity(clienteRequest);
            clienteNatural.setId(id);
            ClienteNatural clienteNaturalBD = clienteRepositorio.save(clienteNatural);
            return ClienteMapper.toClienteNaturalResponse(clienteNaturalBD);
        }
        else throw new ClienteNoEncontradoExcepcion("Cliente no encontrado");
    }

    private boolean verificarExistenciaUsuario(Long idUsuario) {
        return usuarioRepository.existsById(idUsuario);
    }

    @Override
    public ClienteJuridicoResponse createClienteJuridico(ClienteJuridicoRequest clienteJuridicoRequest) {
        if (!verificarExistenciaUsuario(clienteJuridicoRequest.getUsuarioId())) return null;
        ClienteJuridico clienteJuridico = ClienteMapper.toClienteJuridicoEntity(clienteJuridicoRequest);
        ClienteJuridico clienteJuridicoBD = clienteRepositorio.save(clienteJuridico);
        return ClienteMapper.toClienteJuridicoResponse(clienteJuridicoBD);
    }


    @Override
    public ClienteJuridicoResponse updateClienteJuridico(Long id, ClienteJuridicoRequest clienteRequest) throws ClienteNoEncontradoExcepcion {
        if (clienteRepositorio.existsById(id)) {
            if (!verificarExistenciaUsuario(clienteRequest.getUsuarioId())) return null;
            ClienteJuridico clienteJuridico = ClienteMapper.toClienteJuridicoEntity(clienteRequest);
            clienteJuridico.setId(id);
            ClienteJuridico clienteJuridicoBD = clienteRepositorio.save(clienteJuridico);
            return ClienteMapper.toClienteJuridicoResponse(clienteJuridicoBD);
        }
        else throw new ClienteNoEncontradoExcepcion("Cliente no encontrado");
    }

    @Override
    public List<ClienteResponse> readAllClientes(String keyword, String tipo,  int page, int size) {
        Page<Cliente> clientes = clienteRepositorio.searchCliente(keyword, tipo, PageRequest.of(page, size));
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
