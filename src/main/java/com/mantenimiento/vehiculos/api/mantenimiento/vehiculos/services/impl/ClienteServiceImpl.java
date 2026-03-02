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
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.FusekiSyncService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FusekiSyncService fusekiSyncService;

    private static final String NS = "http://taller.com/ontologia/mantenimiento#";
    private static final String DATA = "http://taller.com/datos/";
    private static final String SCHEMA = "https://schema.org/";

    @Override
    public ClienteNaturalResponse createClienteNatural(ClienteNaturalRequest clienteNaturalRequest) {
        if (!verificarExistenciaUsuario(clienteNaturalRequest.getUsuarioId())) throw new EntityNotFoundException("Usuario no encontrado");

        ClienteNatural clienteNatural = ClienteMapper.toClienteNaturalEntity(clienteNaturalRequest);
        ClienteNatural clienteNaturalBD = clienteRepositorio.save(clienteNatural);
        sincronizarCliente(clienteNaturalBD);
        return ClienteMapper.toClienteNaturalResponse(clienteNaturalBD);
    }

    @Override
    public ClienteNaturalResponse updateClienteNatural(Long id, ClienteNaturalRequest clienteRequest) throws ClienteNoEncontradoExcepcion {
        if (clienteRepositorio.existsById(id)) {
            if (!verificarExistenciaUsuario(clienteRequest.getUsuarioId())) throw new EntityNotFoundException("Usuario no encontrado");
            ClienteNatural clienteNatural = ClienteMapper.toClienteNaturalEntity(clienteRequest);
            clienteNatural.setId(id);
            ClienteNatural clienteNaturalBD = clienteRepositorio.save(clienteNatural);
            sincronizarCliente(clienteNaturalBD);
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
        sincronizarCliente(clienteJuridicoBD);
        return ClienteMapper.toClienteJuridicoResponse(clienteJuridicoBD);
    }

    @Override
    public ClienteJuridicoResponse updateClienteJuridico(Long id, ClienteJuridicoRequest clienteRequest) throws ClienteNoEncontradoExcepcion {
        if (clienteRepositorio.existsById(id)) {
            if (!verificarExistenciaUsuario(clienteRequest.getUsuarioId())) return null;
            ClienteJuridico clienteJuridico = ClienteMapper.toClienteJuridicoEntity(clienteRequest);
            clienteJuridico.setId(id);
            ClienteJuridico clienteJuridicoBD = clienteRepositorio.save(clienteJuridico);
            sincronizarCliente(clienteJuridicoBD);
            return ClienteMapper.toClienteJuridicoResponse(clienteJuridicoBD);
        }
        else throw new ClienteNoEncontradoExcepcion("Cliente no encontrado");
    }

    @Override
    public List<ClienteResponse> readAllClientes(String keyword, String tipo,  int page, int size) {
        Page<Cliente> clientes = clienteRepositorio.searchCliente(keyword, tipo, PageRequest.of(page, size));
        return ClienteMapper.toClienteResponseList(clientes);
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
            fusekiSyncService.deleteEntity(DATA + "cliente/" + id);
        }
        else throw new ClienteNoEncontradoExcepcion("Cliente no encontrado");
    }

    private void sincronizarCliente(Cliente c) {
        Model m = ModelFactory.createDefaultModel();
        String uri = DATA + "cliente/" + c.getId();
        Resource r = m.createResource(uri);

        if (c instanceof ClienteNatural cn) {
            r.addProperty(RDF.type, m.createResource(NS + "ClienteNatural"));
            r.addProperty(RDF.type, m.createResource(SCHEMA + "Person"));
            if (cn.getNombre() != null) r.addProperty(m.createProperty(NS + "nombre"), cn.getNombre());
            if (cn.getApellido() != null) r.addProperty(m.createProperty(NS + "apellido"), cn.getApellido());
            if (cn.getDni() != null) r.addProperty(m.createProperty(NS + "dni"), cn.getDni());
        } else if (c instanceof ClienteJuridico cj) {
            r.addProperty(RDF.type, m.createResource(NS + "ClienteJuridico"));
            r.addProperty(RDF.type, m.createResource(SCHEMA + "Organization"));
            if (cj.getRazonSocial() != null) r.addProperty(m.createProperty(NS + "razonSocial"), cj.getRazonSocial());
            if (cj.getRuc() != null) r.addProperty(m.createProperty(NS + "ruc"), cj.getRuc());
        }

        if (c.getTelefono() != null) r.addProperty(m.createProperty(NS + "telefono"), c.getTelefono());
        if (c.getCorreoElectronico() != null) r.addProperty(m.createProperty(NS + "correoElectronico"), c.getCorreoElectronico());
        if (c.getDireccion() != null) r.addProperty(m.createProperty(NS + "direccion"), c.getDireccion());

        fusekiSyncService.syncEntity(uri, m);
    }
}