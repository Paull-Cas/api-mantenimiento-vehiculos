package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponsePageDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.TipoMantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.MantenimientoMapper;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.MantenimientoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.FusekiSyncService;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.MantenimientoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class MantenimientoServiceImpl implements MantenimientoService {

    @Autowired
    private MantenimientoRepositorio mantenimientoRepositorio;

    @Autowired
    private MantenimientoMapper mantenimientoMapper;

    @Autowired
    private FusekiSyncService fusekiSyncService;

    private static final String NS = "http://taller.com/ontologia/mantenimiento#";
    private static final String DATA = "http://taller.com/datos/";
    private static final String SCHEMA = "https://schema.org/";

    @Override
    @Transactional
    public MantenimientoResponseDTO saveMantenimiento(MantenimientoRequest mantenimientoRequest) {
        log.info("Guardar mantenimiento de tipo: {}", mantenimientoRequest.getTipo());
        Mantenimiento mantenimiento = mantenimientoMapper.mapearDeRequest(mantenimientoRequest);
        Mantenimiento mantenimientoBD = mantenimientoRepositorio.save(mantenimiento);
        sincronizarMantenimiento(mantenimientoBD);
        return mantenimientoMapper.mapearDeResponseDTO(mantenimientoBD);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MantenimientoResponseDTO> getTodosLosMantenimientos() {
        long inicio = System.currentTimeMillis();

        List<MantenimientoResponseDTO> resultado = mantenimientoRepositorio.findAll()
                .stream()
                .map(mantenimientoMapper::mapearDeResponseDTO)
                .collect(Collectors.toList());

        long fin = System.currentTimeMillis();
        System.out.println("╔══════════════════════════════════════════");
        System.out.println("║ MySQL - getTodosLosMantenimientos: " + (fin - inicio) + " ms");
        System.out.println("╚══════════════════════════════════════════");

        return resultado;
    }

    @Override
    @Transactional(readOnly = true)
    public MantenimientoResponseDTO getMantenimientoPorId(Long id) throws MantenimientoNoEncontradoExcepcion {
        Mantenimiento mantenimiento = mantenimientoRepositorio.findById(id)
                .orElseThrow(() -> new MantenimientoNoEncontradoExcepcion("Mantenimiento no encontrado con ID: " + id));
        return mantenimientoMapper.mapearDeResponseDTO(mantenimiento);
    }

    @Override
    @Transactional
    public MantenimientoResponseDTO updateMantenimiento(Long idMantenimiento, MantenimientoRequest mantenimientoRequest) throws MantenimientoNoEncontradoExcepcion {
        log.info("Actualizar mantenimiento con ID: {}", idMantenimiento);
        Mantenimiento mantenimientoExistente = mantenimientoRepositorio.findById(idMantenimiento)
                .orElseThrow(() -> new MantenimientoNoEncontradoExcepcion("Mantenimiento no encontrado con ID: " + idMantenimiento));

        mantenimientoExistente.setNombre(mantenimientoRequest.getNombre());
        mantenimientoExistente.setDescripcion(mantenimientoRequest.getDescripcion());
        mantenimientoExistente.setTipo(mantenimientoRequest.getTipo());
        mantenimientoExistente.setPrecioMinimo(mantenimientoRequest.getPrecioMinimo());
        mantenimientoExistente.setActivo(mantenimientoRequest.isActivo());

        Mantenimiento mantenimientoActualizado = mantenimientoRepositorio.save(mantenimientoExistente);
        sincronizarMantenimiento(mantenimientoActualizado);
        return mantenimientoMapper.mapearDeResponseDTO(mantenimientoActualizado);
    }

    @Override
    @Transactional
    public void deleteMantenimiento(Long id) throws MantenimientoNoEncontradoExcepcion {
        log.info("Eliminar mantenimiento con ID: {}", id);
        if (!mantenimientoRepositorio.existsById(id)) {
            throw new MantenimientoNoEncontradoExcepcion("No se puede eliminar. Mantenimiento no encontrado con ID: " + id);
        }
        mantenimientoRepositorio.deleteById(id);
        fusekiSyncService.deleteEntity(DATA + "mantenimiento/" + id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MantenimientoResponseDTO> buscarMantenimientosPorTipo(TipoMantenimiento tipo) {
        List<Mantenimiento> mantenimientos = mantenimientoRepositorio.findByTipo(tipo);
        return mantenimientos.stream()
                .map(mantenimientoMapper::mapearDeResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MantenimientoResponsePageDTO getMantenimientosPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Mantenimiento> pageMantenimientos = mantenimientoRepositorio.findAll(pageable);

        List<MantenimientoResponseDTO> mantenimientoResponseDTOS = pageMantenimientos.getContent()
                .stream()
                .map(mantenimientoMapper::mapearDeResponseDTO)
                .collect(Collectors.toList());

        MantenimientoResponsePageDTO mantenimientoPageDTO = new MantenimientoResponsePageDTO();
        mantenimientoPageDTO.setMantenimientos(mantenimientoResponseDTOS);
        mantenimientoPageDTO.setCurrentPage(pageMantenimientos.getNumber());
        mantenimientoPageDTO.setPageSize(pageMantenimientos.getSize());
        mantenimientoPageDTO.setTotalPages(pageMantenimientos.getTotalPages());
        mantenimientoPageDTO.setTotalElements(pageMantenimientos.getTotalElements());

        return mantenimientoPageDTO;
    }

    private void sincronizarMantenimiento(Mantenimiento m) {
        Model model = ModelFactory.createDefaultModel();
        String uri = DATA + "mantenimiento/" + m.getId();
        Resource r = model.createResource(uri);

        r.addProperty(RDF.type, model.createResource(NS + "Mantenimiento"));
        r.addProperty(RDF.type, model.createResource(SCHEMA + "Service"));

        if(m.getNombre() != null) r.addProperty(model.createProperty(NS + "nombre"), m.getNombre());
        if(m.getDescripcion() != null) r.addProperty(model.createProperty(NS + "descripcion"), m.getDescripcion());
        if(m.getPrecioMinimo() != null) r.addProperty(model.createProperty(NS + "precioMinimo"), model.createTypedLiteral(m.getPrecioMinimo().floatValue()));
        if(m.getTipo() != null) r.addProperty(model.createProperty(NS + "tipo"), m.getTipo().toString());

        fusekiSyncService.syncEntity(uri, model);
    }
}