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
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ClienteMapper {

    // ---------- NATURAL ----------

    public static ClienteNatural toClienteNaturalEntity(ClienteNaturalRequest request) {
        ClienteNatural entity = new ClienteNatural();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static ClienteNaturalResponse toClienteNaturalResponse(ClienteNatural entity) {
        ClienteNaturalResponse response = new ClienteNaturalResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    // ---------- JURÍDICO ----------

    public static ClienteJuridico toClienteJuridicoEntity(ClienteJuridicoRequest request) {
        ClienteJuridico entity = new ClienteJuridico();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static ClienteJuridicoResponse toClienteJuridicoResponse(ClienteJuridico entity) {
        ClienteJuridicoResponse response = new ClienteJuridicoResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    // ---------- GENÉRICO ----------

    public static ClienteResponse toClienteResponse(Cliente cliente) {
        if (cliente instanceof ClienteNatural) {
            return toClienteNaturalResponse((ClienteNatural) cliente);
        }
        if (cliente instanceof ClienteJuridico) {
            return toClienteJuridicoResponse((ClienteJuridico) cliente);
        }
        ClienteResponse response = new ClienteResponse();
        BeanUtils.copyProperties(cliente, response);
        return response;
    }

    public static List<ClienteResponse> toClienteResponseList(Page<Cliente> clientes) {
        return clientes.stream()
                .map(ClienteMapper::toClienteResponse)
                .collect(Collectors.toList());
    }
}
