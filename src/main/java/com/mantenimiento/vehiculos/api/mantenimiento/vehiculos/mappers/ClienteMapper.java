package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteJuridicoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteNaturalRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteJuridicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteNaturalResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Cliente;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.ClienteJuridico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.ClienteNatural;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ClienteMapper {

    public static ClienteNatural toClienteNaturalEntity(ClienteNaturalRequest clienteNaturalRequest) {
        ClienteNatural clienteNatural = new ClienteNatural();
        BeanUtils.copyProperties(clienteNaturalRequest, clienteNatural);
        return clienteNatural;
    }

    public static ClienteNaturalResponse toClienteNaturalResponse(ClienteNatural clienteNatural) {
        ClienteNaturalResponse clienteNaturalResponse = new ClienteNaturalResponse();
        BeanUtils.copyProperties(clienteNatural, clienteNaturalResponse);
        return clienteNaturalResponse;
    }

    public static ClienteJuridico toClienteJuridicoEntity(ClienteJuridicoRequest clienteJuridicoRequest) {
        ClienteJuridico clienteJuridico = new ClienteJuridico();
        BeanUtils.copyProperties(clienteJuridicoRequest, clienteJuridico);
        return clienteJuridico;
    }

    public static ClienteJuridicoResponse toClienteJuridicoResponse(ClienteJuridico clienteJuridico) {
        ClienteJuridicoResponse clienteJuridicoResponse = new ClienteJuridicoResponse();
        BeanUtils.copyProperties(clienteJuridico, clienteJuridicoResponse);
        return clienteJuridicoResponse;
    }

    public static List<ClienteResponse> toClienteResponseList(List<Cliente> clientes) {
        return clientes.stream()
                .map(cliente -> {
                    if (cliente instanceof ClienteJuridico) {
                        return toClienteJuridicoResponse((ClienteJuridico) cliente);
                    }
                    if (cliente instanceof ClienteNatural) {
                        return toClienteNaturalResponse((ClienteNatural) cliente);
                    }
                    else return null;
                })
                .collect(Collectors.toList());
    }
}
