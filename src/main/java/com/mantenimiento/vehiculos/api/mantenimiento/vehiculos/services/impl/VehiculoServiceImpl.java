package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.VehiculoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.VehiculoPageReponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.VehiculoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Cliente;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Usuario;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Vehiculo;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.ClienteNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.VehiculoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.VehiculoMappers;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.ClienteRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.UsuarioRepository;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.VehiculoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.FusekiSyncService;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.VehiculoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class VehiculoServiceImpl implements VehiculoService {

    @Autowired
    private VehiculoRepositorio vehiculoRepositorio;

    @Autowired
    private VehiculoMappers vehiculoMapper;

    @Autowired
    private ClienteRepositorio clienteRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private FusekiSyncService fusekiSyncService;

    private static final String NS = "http://taller.com/ontologia/mantenimiento#";
    private static final String DATA = "http://taller.com/datos/";
    private static final String SCHEMA = "https://schema.org/";

    @Override
    public VehiculoResponse saveVehiculo(VehiculoRequest vehiculoDto) throws ClienteNoEncontradoExcepcion {
        log.info("Guardando vehiculo");
        Cliente cliente = clienteRepo.findById(vehiculoDto.getPropietarioId()).orElseThrow(() ->
                new ClienteNoEncontradoExcepcion("Cliente no encontrado con ID: " + vehiculoDto.getPropietarioId()));
        Vehiculo vehiculo = vehiculoMapper.mapearVheiculoDto(vehiculoDto, cliente);
        Vehiculo vheculoGuardado = vehiculoRepositorio.save(vehiculo);
        sincronizarVehiculo(vheculoGuardado);
        return vehiculoMapper.mapearVheiculo(vheculoGuardado);
    }

    @Override
    public VehiculoResponse getVehiculo(Long vehiculoId) throws VehiculoNoEncontradoExcepcion {
        Vehiculo vehiculo = vehiculoRepositorio.findById(vehiculoId).orElseThrow(() ->
                new VehiculoNoEncontradoExcepcion("Vehiculo no encontrado con ID: " + vehiculoId));
        return vehiculoMapper.mapearVheiculo(vehiculo);
    }

    @Override
    public VehiculoResponse updateVehiculo(Long id, VehiculoRequest vehiculoDto) throws VehiculoNoEncontradoExcepcion {
        log.info("Vehiculo Actualizado");
        Cliente cliente = clienteRepo.findById(vehiculoDto.getPropietarioId()).orElseThrow(() ->
                new ClienteNoEncontradoExcepcion("Cliente no encontrado con ID: " + vehiculoDto.getPropietarioId()));
        Vehiculo vehiculo = vehiculoMapper.mapearVheiculoDto(vehiculoDto, cliente);
        vehiculo.setId(id);
        Vehiculo vheculoGuardado = vehiculoRepositorio.save(vehiculo);
        sincronizarVehiculo(vheculoGuardado);
        return vehiculoMapper.mapearVheiculo(vheculoGuardado);
    }

    @Override
    public void deleteVehiculo(Long vehiculoId) throws VehiculoNoEncontradoExcepcion {
        if (!vehiculoRepositorio.existsById(vehiculoId)){
            throw new VehiculoNoEncontradoExcepcion("Vehiculo no encontrado");
        }
        vehiculoRepositorio.deleteById(vehiculoId);
        fusekiSyncService.deleteEntity(DATA + "vehiculo/" + vehiculoId);
        log.info("Vehiculo eliminado", vehiculoId);
    }

    @Override
    public List<VehiculoResponse> listVehiculos() {
        List<Vehiculo> vehiculos = vehiculoRepositorio.findAll();
        List<VehiculoResponse> vehiculoDtos = vehiculos.stream()
                .map(vehiculoMapper::mapearVheiculo)
                .collect(Collectors.toList());
        return vehiculoDtos;
    }

    @Override
    public List<VehiculoResponse> searchVehiculos(String keyword) {
        List<Vehiculo> vehiculos = vehiculoRepositorio.searchVehiculos(keyword);
        List<VehiculoResponse> vehiculoDtos = vehiculos.stream()
                .map(vehiculoMapper::mapearVheiculo)
                .collect(Collectors.toList());
        return vehiculoDtos;
    }

    @Override
    public List<VehiculoResponse> listByCliente(Long clienteId) {
        return vehiculoRepositorio.findByCliente_Id(clienteId).stream()
                .map(vehiculoMapper::mapearVheiculo)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehiculoResponse> misVehiculos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Usuario usuario = usuarioRepo.findUserEntityByUsername(username);
        log.info("USUARIO =========" + usuario.getUsername());
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Cliente cliente = clienteRepo.findAllByUsuarioId(usuario.getId());

        return vehiculoRepositorio.findByClienteId(cliente.getId()).stream()
                .map(vehiculoMapper::mapearVheiculo)
                .collect(Collectors.toList());
    }

    @Override
    public VehiculoPageReponse getVehiculoPageReponse(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vehiculo> pagevehiculos = vehiculoRepositorio.findAll(pageable);
        List<VehiculoResponse> vehiculosres = pagevehiculos.getContent().stream()
                .map(vehiculoMapper::mapearVheiculo)
                .collect(Collectors.toList());

        VehiculoPageReponse vehiculoPageReponse = new VehiculoPageReponse();
        vehiculoPageReponse.setVehiculos(vehiculosres);
        vehiculoPageReponse.setCurrentPage(pagevehiculos.getNumber());
        vehiculoPageReponse.setPageSize(pagevehiculos.getSize());
        vehiculoPageReponse.setTotalPages(pagevehiculos.getTotalPages());
        vehiculoPageReponse.setTotalElements(pagevehiculos.getTotalElements());

        return vehiculoPageReponse;
    }

    private void sincronizarVehiculo(Vehiculo v) {
        Model model = ModelFactory.createDefaultModel();
        String uri = DATA + "vehiculo/" + v.getId();
        Resource r = model.createResource(uri);

        r.addProperty(RDF.type, model.createResource(NS + "Vehiculo"));
        r.addProperty(RDF.type, model.createResource(SCHEMA + "Vehicle"));

        if(v.getPlaca() != null) r.addProperty(model.createProperty(NS + "placa"), v.getPlaca());
        if(v.getMarca() != null) r.addProperty(model.createProperty(NS + "marca"), v.getMarca());
        if(v.getModelo() != null) r.addProperty(model.createProperty(NS + "modelo"), v.getModelo());

        r.addProperty(model.createProperty(NS + "año"), model.createTypedLiteral(v.getAño()));
        r.addProperty(model.createProperty(NS + "kilometraje"), model.createTypedLiteral(v.getKilometraje()));
        if (v.getEstado() != null) r.addProperty(model.createProperty(NS + "estado"), v.getEstado().toString());

        if (v.getMarca() != null && v.getModelo() != null) {
            r.addProperty(model.createProperty(SCHEMA + "name"), v.getMarca() + " " + v.getModelo());
        }

        if (v.getCliente() != null) {
            r.addProperty(model.createProperty(NS + "perteneceACliente"),
                    model.createResource(DATA + "cliente/" + v.getCliente().getId()));
        }

        fusekiSyncService.syncEntity(uri, model);
    }
}