package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.TallerRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.TallerResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mecanico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Taller;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.TallerMapper;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.MantenimientoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.TallerRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.FusekiSyncService;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.TallerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class TallerServiceImpl implements TallerService {

    @Autowired
    private TallerRepositorio tallerRepositorio;

    @Autowired
    private MantenimientoRepositorio mantenimientoRepositorio;

    @Autowired
    private TallerMapper tallerMapper;

    @Autowired
    private FusekiSyncService fusekiSyncService;

    private static final String NS = "http://taller.com/ontologia/mantenimiento#";
    private static final String DATA = "http://taller.com/datos/";
    private static final String SCHEMA = "https://schema.org/";

    @Override
    public TallerResponseDTO crearTaller(TallerRequest request) {
        log.info("Creando nuevo taller: {}", request.getNombre());

        Taller taller = tallerMapper.toEntity(request);
        Taller tallerGuardado = tallerRepositorio.save(taller);

        if (request.getMantenimientosIds() != null && !request.getMantenimientosIds().isEmpty()) {
            List<Mantenimiento> mantenimientos = mantenimientoRepositorio
                    .findAllById(request.getMantenimientosIds());
            tallerGuardado.setMantenimientos(mantenimientos);
            tallerGuardado = tallerRepositorio.save(tallerGuardado);
        }

        sincronizarTaller(tallerGuardado);
        return tallerMapper.toResponse(tallerGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public TallerResponseDTO obtenerTallerPorId(Long id) {
        Taller taller = tallerRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado con ID: " + id));
        return tallerMapper.toResponse(taller);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TallerResponseDTO> obtenerTodos() {
        return tallerRepositorio.findAll().stream()
                .map(tallerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TallerResponseDTO> buscarPorCiudad(String ciudad) {
        return tallerRepositorio.findByCiudadIgnoreCase(ciudad).stream()
                .map(tallerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TallerResponseDTO> buscarPorDistrito(String distrito) {
        return tallerRepositorio.findByDistritoIgnoreCase(distrito).stream()
                .map(tallerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TallerResponseDTO> buscarPorKeyword(String keyword) {
        return tallerRepositorio.buscarPorKeyword(keyword).stream()
                .map(tallerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TallerResponseDTO actualizarTaller(Long id, TallerRequest request) {
        log.info("Actualizando taller con ID: {}", id);

        Taller taller = tallerRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado con ID: " + id));

        taller.setNombre(request.getNombre());
        taller.setCiudad(request.getCiudad());
        taller.setDistrito(request.getDistrito());
        taller.setDireccion(request.getDireccion());
        taller.setHorario(request.getHorario());

        if (request.getMantenimientosIds() != null) {
            List<Mantenimiento> mantenimientos = mantenimientoRepositorio
                    .findAllById(request.getMantenimientosIds());
            taller.setMantenimientos(mantenimientos);
        }

        Taller actualizado = tallerRepositorio.save(taller);
        sincronizarTaller(actualizado);
        return tallerMapper.toResponse(actualizado);
    }

    @Override
    public void eliminarTaller(Long id) {
        log.info("Eliminando taller con ID: {}", id);
        Taller taller = tallerRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado con ID: " + id));
        tallerRepositorio.delete(taller);
        fusekiSyncService.deleteEntity(DATA + "taller/" + id);
    }

    private void sincronizarTaller(Taller t) {
        Model m = ModelFactory.createDefaultModel();
        String uri = DATA + "taller/" + t.getId();
        Resource r = m.createResource(uri);

        r.addProperty(RDF.type, m.createResource(NS + "Taller"));
        r.addProperty(RDF.type, m.createResource(SCHEMA + "AutoRepair"));

        if(t.getNombre() != null) r.addProperty(m.createProperty(NS + "nombre"), t.getNombre());
        if(t.getCiudad() != null) r.addProperty(m.createProperty(NS + "ciudad"), t.getCiudad());
        if(t.getDistrito() != null) r.addProperty(m.createProperty(NS + "distrito"), t.getDistrito());
        if(t.getDireccion() != null) r.addProperty(m.createProperty(NS + "direccion"), t.getDireccion());
        if(t.getHorario() != null) r.addProperty(m.createProperty(NS + "horario"), t.getHorario());

        if (t.getMecanicos() != null) {
            for (Mecanico mec : t.getMecanicos()) {
                r.addProperty(m.createProperty(NS + "tieneMecanico"), m.createResource(DATA + "mecanico/" + mec.getId()));
            }
        }
        if (t.getMantenimientos() != null) {
            for (Mantenimiento mant : t.getMantenimientos()) {
                r.addProperty(m.createProperty(NS + "ofreceMantenimiento"), m.createResource(DATA + "mantenimiento/" + mant.getId()));
            }
        }

        fusekiSyncService.syncEntity(uri, m);
    }
}