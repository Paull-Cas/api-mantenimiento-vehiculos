package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteNaturalRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteJuridicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteNaturalResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Cliente;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.ClienteJuridico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.ClienteNatural;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteMapper {

    public static ClienteNatural toClienteNaturalEntity(ClienteNaturalRequest clienteNaturalRequest) {
        return ClienteNatural.builder()
                .telefono(clienteNaturalRequest.getTelefono())
                .correoElectronico(clienteNaturalRequest.getCorreoElectronico())
                .direccion(clienteNaturalRequest.getDireccion())
                .nombre(clienteNaturalRequest.getNombre())
                .apellido(clienteNaturalRequest.getApellido())
                .dni(clienteNaturalRequest.getDni())
                .build();
    }

    public static ClienteNaturalResponse toClienteNaturalResponse(ClienteNatural clienteNatural) {
        return ClienteNaturalResponse.builder()
                .id(clienteNatural.getId())
                .telefono(clienteNatural.getTelefono())
                .correoElectronico(clienteNatural.getCorreoElectronico())
                .direccion(clienteNatural.getDireccion())
                .tipo("natural")
                .nombre(clienteNatural.getNombre())
                .apellido(clienteNatural.getApellido())
                .dni(clienteNatural.getDni())
                .build();
    }

    public static List<ClienteNaturalResponse> toClienteNaturalResponseList(List<ClienteNatural> clientesNaturales) {
        List<ClienteNaturalResponse> clientesNaturalResponses = new ArrayList<>();
        for (ClienteNatural clienteNatural : clientesNaturales) {
            clientesNaturalResponses.add(toClienteNaturalResponse(clienteNatural));
        }
        return clientesNaturalResponses;
    }


    public static ClienteJuridico toClienteJuridicoEntity(ClienteJuridico clienteJuridicoRequest) {
        return ClienteJuridico.builder()
                .telefono(clienteJuridicoRequest.getTelefono())
                .correoElectronico(clienteJuridicoRequest.getCorreoElectronico())
                .direccion(clienteJuridicoRequest.getDireccion())
                .razonSocial(clienteJuridicoRequest.getRazonSocial())
                .ruc(clienteJuridicoRequest.getRuc())
                .representanteLegal(clienteJuridicoRequest.getRepresentanteLegal())
                .build();
    }

    public static ClienteJuridicoResponse toClienteJuridicoResponse(ClienteJuridico clienteJuridico) {
        return ClienteJuridicoResponse.builder()
                .id(clienteJuridico.getId())
                .telefono(clienteJuridico.getTelefono())
                .correoElectronico(clienteJuridico.getCorreoElectronico())
                .direccion(clienteJuridico.getDireccion())
                .tipo("juridico")
                .razonSocial(clienteJuridico.getRazonSocial())
                .ruc(clienteJuridico.getRuc())
                .representanteLegal(clienteJuridico.getRepresentanteLegal())
                .build();
    }

    public static List<ClienteJuridicoResponse> toClienteJuridicoResponseList(List<ClienteJuridico> clientesJuridicos) {
        return clientesJuridicos.stream()
                .map(clienteJuridico -> toClienteJuridicoResponse(clienteJuridico))
                .toList();
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
