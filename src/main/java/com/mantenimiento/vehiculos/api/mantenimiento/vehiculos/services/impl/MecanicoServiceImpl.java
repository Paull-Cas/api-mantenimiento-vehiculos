package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MecanicoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MecanicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mecanico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Taller;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoMecanico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MecanicoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.MecanicoMapper;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.MecanicoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.TallerRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.UsuarioRepository;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.FusekiSyncService;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.MecanicoService;
import jakarta.persistence.EntityNotFoundException;
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
public class MecanicoServiceImpl implements MecanicoService {

    @Autowired
    private MecanicoRepositorio mecanicoRepositorio;

    @Autowired
    private MecanicoMapper mecanicoMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TallerRepositorio tallerRepositorio;

    @Autowired
    private FusekiSyncService fusekiSyncService;

    private static final String NS = "http://taller.com/ontologia/mantenimiento#";
    private static final String DATA = "http://taller.com/datos/";
    private static final String SCHEMA = "https://schema.org/";

    @Override
    public MecanicoResponse crearMecanico(MecanicoRequest request) {
        if (!verificarExistenciaUsuario(request.getUsuarioId())) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + request.getUsuarioId());
        }

        Taller taller = tallerRepositorio.findById(request.getTallerId())
                .orElseThrow(() -> new EntityNotFoundException("El Taller con ID " + request.getTallerId() + " no existe."));

        Mecanico mecanico = mecanicoMapper.toEntity(request);
        mecanico.setTaller(taller);
        mecanico.setEstado(EstadoMecanico.DISPONIBLE);
        mecanico.setActivo(true);

        Mecanico mecanicoGuardado = mecanicoRepositorio.save(mecanico);
        sincronizarMecanico(mecanicoGuardado);

        return mecanicoMapper.toResponse(mecanicoGuardado);
    }

    @Override
    public MecanicoResponse actualizarMecanico(Long id, MecanicoRequest request) {
        if (!verificarExistenciaUsuario(request.getUsuarioId())) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + request.getUsuarioId());
        }

        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion("Mecánico no encontrado con ID: " + id));

        if (request.getTallerId() != null && (mecanico.getTaller() == null || !mecanico.getTaller().getId().equals(request.getTallerId()))) {
            Taller nuevoTaller = tallerRepositorio.findById(request.getTallerId())
                    .orElseThrow(() -> new EntityNotFoundException("El nuevo Taller con ID " + request.getTallerId() + " no existe."));
            mecanico.setTaller(nuevoTaller);
        }

        mecanico.setNombre(request.getNombre());
        mecanico.setApellido(request.getApellido());
        mecanico.setEspecialidad(request.getEspecialidad());
        mecanico.setTelefono(request.getTelefono());
        mecanico.setFechaIngreso(request.getFechaIngreso());

        Mecanico mecanicoActualizado = mecanicoRepositorio.save(mecanico);
        sincronizarMecanico(mecanicoActualizado);

        return mecanicoMapper.toResponse(mecanicoActualizado);
    }

    private boolean verificarExistenciaUsuario(Long idUsuario) {
        return usuarioRepository.existsById(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public MecanicoResponse obtenerMecanicoPorId(Long id) {
        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion("Mecánico no encontrado con ID: " + id));
        return mecanicoMapper.toResponse(mecanico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MecanicoResponse> obtenerTodosMecanicos() {
        return mecanicoRepositorio.findAll().stream()
                .map(mecanicoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MecanicoResponse> obtenerMecanicosDisponibles() {
        return mecanicoRepositorio.findByEstadoAndActivoTrue(EstadoMecanico.DISPONIBLE).stream()
                .map(mecanicoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MecanicoResponse> buscarPorEspecialidad(String especialidad) {
        return mecanicoRepositorio.findByEspecialidadContainingIgnoreCaseAndActivoTrue(especialidad).stream()
                .map(mecanicoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void desactivarMecanico(Long id) {
        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion("Mecánico no encontrado con ID: " + id));

        if (mecanico.getEstado() == EstadoMecanico.OCUPADO) {
            throw new IllegalStateException("No se puede desactivar un mecánico que está ocupado. Finalice sus trabajos primero.");
        }

        mecanico.setActivo(false);
        mecanico.setEstado(EstadoMecanico.INACTIVO);
        Mecanico actualizado = mecanicoRepositorio.save(mecanico);
        sincronizarMecanico(actualizado);
    }

    @Override
    public void activarMecanico(Long id) {
        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion("Mecánico no encontrado con ID: " + id));

        mecanico.setActivo(true);
        mecanico.setEstado(EstadoMecanico.DISPONIBLE);
        Mecanico actualizado = mecanicoRepositorio.save(mecanico);
        sincronizarMecanico(actualizado);
    }

    @Override
    public void cambiarEstado(Long id, EstadoMecanico nuevoEstado) {
        log.info("Cambiando estado de mecánico ID: {} a {}", id, nuevoEstado);

        Mecanico mecanico = mecanicoRepositorio.findById(id)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion("Mecánico no encontrado con ID: " + id));

        mecanico.setEstado(nuevoEstado);
        Mecanico actualizado = mecanicoRepositorio.save(mecanico);
        sincronizarMecanico(actualizado);
    }

    @Override
    public void eliminarMencanico(Long id) {
        if (!mecanicoRepositorio.existsById(id)) {
            throw new EntityNotFoundException("No existe el mecánico con id: " + id);
        }
        mecanicoRepositorio.deleteById(id);
        fusekiSyncService.deleteEntity(DATA + "mecanico/" + id);
    }

    private void sincronizarMecanico(Mecanico m) {
        Model model = ModelFactory.createDefaultModel();
        String uri = DATA + "mecanico/" + m.getId();
        Resource r = model.createResource(uri);

        r.addProperty(RDF.type, model.createResource(NS + "Mecanico"));
        r.addProperty(RDF.type, model.createResource(SCHEMA + "Person"));

        if(m.getNombre() != null) r.addProperty(model.createProperty(NS + "nombre"), m.getNombre());
        if(m.getApellido() != null) r.addProperty(model.createProperty(NS + "apellido"), m.getApellido());
        if(m.getEspecialidad() != null) r.addProperty(model.createProperty(NS + "especialidad"), m.getEspecialidad());
        if(m.getTelefono() != null) r.addProperty(model.createProperty(NS + "telefono"), m.getTelefono());
        if(m.getEstado() != null) r.addProperty(model.createProperty(NS + "estado"), m.getEstado().toString());

        if (m.getTaller() != null) {
            r.addProperty(model.createProperty(NS + "trabajaEn"), model.createResource(DATA + "taller/" + m.getTaller().getId()));
        }

        fusekiSyncService.syncEntity(uri, model);
    }
}