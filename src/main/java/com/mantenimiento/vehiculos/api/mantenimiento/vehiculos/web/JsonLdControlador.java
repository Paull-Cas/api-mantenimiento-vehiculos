package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jsonld")
@RequiredArgsConstructor
public class JsonLdController {

    private final TallerRepositorio     tallerRepository;
    private final MecanicoRepositorio   mecanicoRepository;
    private final MantenimientoRepositorio mantenimientoRepository;
    private final ClienteRepositorio    clienteRepository;
    private final VehiculoRepositorio   vehiculoRepository;

    private static final String SCHEMA  = "https://schema.org/";
    private static final String DATA_NS = "http://taller.com/datos/";

    private Map<String, Object> contextoBase() {
        Map<String, Object> ctx = new LinkedHashMap<>();
        ctx.put("schema",  SCHEMA);
        ctx.put("taller",  "http://taller.com/ontologia/mantenimiento#");
        ctx.put("data",    DATA_NS);
        ctx.put("nombre",     "schema:name");
        ctx.put("direccion",  "schema:address");
        ctx.put("telefono",   "schema:telephone");
        ctx.put("horario",    "schema:openingHours");
        ctx.put("empleados",  "schema:employee");
        ctx.put("servicios",  "schema:hasOfferCatalog");
        ctx.put("precio",     "schema:price");
        ctx.put("descripcion","schema:description");
        return ctx;
    }

    /**
     * GET /api/jsonld/talleres
     * Lista todos los talleres como JSON-LD (schema:AutoRepair).
     */
    @GetMapping(value = "/talleres", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getTalleres() {
        List<Map<String, Object>> items = new ArrayList<>();

        for (Taller t : tallerRepository.findAll()) {
            items.add(tallerAJsonLd(t));
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("@context", contextoBase());
        response.put("@type",    "schema:ItemList");
        response.put("@id",      DATA_NS + "talleres");
        response.put("schema:name", "Talleres de Mantenimiento Vehicular");
        response.put("schema:numberOfItems", items.size());
        response.put("schema:itemListElement", items);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/jsonld/talleres/{id}
     * Un taller específico como JSON-LD.
     */
    @GetMapping(value = "/talleres/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getTaller(@PathVariable Long id) {
        return tallerRepository.findById(id)
                .map(t -> {
                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("@context", contextoBase());
                    response.putAll(tallerAJsonLd(t));
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/jsonld/mecanicos
     * Lista todos los mecánicos como JSON-LD (schema:Person).
     */
    @GetMapping(value = "/mecanicos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getMecanicos() {
        List<Map<String, Object>> items = new ArrayList<>();

        for (Mecanico m : mecanicoRepository.findAll()) {
            items.add(mecanicoAJsonLd(m));
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("@context", contextoBase());
        response.put("@type",    "schema:ItemList");
        response.put("@id",      DATA_NS + "mecanicos");
        response.put("schema:name", "Mecánicos del Taller");
        response.put("schema:numberOfItems", items.size());
        response.put("schema:itemListElement", items);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/jsonld/mecanicos/{id}
     * Un mecánico específico como JSON-LD.
     */
    @GetMapping(value = "/mecanicos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getMecanico(@PathVariable Long id) {
        return mecanicoRepository.findById(id)
                .map(m -> {
                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("@context", contextoBase());
                    response.putAll(mecanicoAJsonLd(m));
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/jsonld/servicios
     * Lista todos los mantenimientos como JSON-LD (schema:Service).
     */
    @GetMapping(value = "/servicios", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getServicios() {
        List<Map<String, Object>> items = new ArrayList<>();

        for (Mantenimiento m : mantenimientoRepository.findAll()) {
            items.add(mantenimientoAJsonLd(m));
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("@context", contextoBase());
        response.put("@type",    "schema:ItemList");
        response.put("@id",      DATA_NS + "servicios");
        response.put("schema:name", "Catálogo de Servicios de Mantenimiento");
        response.put("schema:numberOfItems", items.size());
        response.put("schema:itemListElement", items);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/jsonld/servicios/{id}
     */
    @GetMapping(value = "/servicios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getServicio(@PathVariable Long id) {
        return mantenimientoRepository.findById(id)
                .map(m -> {
                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("@context", contextoBase());
                    response.putAll(mantenimientoAJsonLd(m));
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * GET /api/jsonld/clientes/{id}
     * Un cliente como JSON-LD (schema:Person o schema:Organization).
     */
    @GetMapping(value = "/clientes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getCliente(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(c -> {
                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("@context", contextoBase());
                    response.putAll(clienteAJsonLd(c));
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * GET /api/jsonld/vehiculos/{id}
     * Un vehículo como JSON-LD (schema:Vehicle).
     */
    @GetMapping(value = "/vehiculos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getVehiculo(@PathVariable Long id) {
        return vehiculoRepository.findById(id)
                .map(v -> {
                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("@context", contextoBase());
                    response.putAll(vehiculoAJsonLd(v));
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private Map<String, Object> tallerAJsonLd(Taller t) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("@type", List.of("schema:AutoRepair", "taller:Taller"));
        map.put("@id",   DATA_NS + "taller/" + t.getId());
        map.put("nombre", t.getNombre());
        map.put("horario", t.getHorario());
        map.put("taller:ciudad",    t.getCiudad());
        map.put("taller:distrito",  t.getDistrito());

        if (t.getDireccion() != null || t.getCiudad() != null) {
            Map<String, Object> addr = new LinkedHashMap<>();
            addr.put("@type",              "schema:PostalAddress");
            addr.put("schema:streetAddress",   t.getDireccion());
            addr.put("schema:addressLocality", t.getCiudad());
            addr.put("schema:addressRegion",   t.getDistrito());
            addr.put("schema:addressCountry",  "PE");
            map.put("direccion", addr);
        }

        if (t.getMecanicos() != null && !t.getMecanicos().isEmpty()) {
            List<Map<String, Object>> mecanicos = new ArrayList<>();
            for (Mecanico m : t.getMecanicos()) {
                Map<String, Object> mec = new LinkedHashMap<>();
                mec.put("@type", "schema:Person");
                mec.put("@id",   DATA_NS + "mecanico/" + m.getId());
                mec.put("schema:name",     m.getNombre() + " " + m.getApellido());
                mec.put("schema:jobTitle", m.getEspecialidad());
                mecanicos.add(mec);
            }
            map.put("empleados", mecanicos);
        }

        if (t.getMantenimientos() != null && !t.getMantenimientos().isEmpty()) {
            List<Map<String, Object>> servicios = new ArrayList<>();
            for (Mantenimiento mant : t.getMantenimientos()) {
                Map<String, Object> svc = new LinkedHashMap<>();
                svc.put("@type", "schema:Service");
                svc.put("@id",   DATA_NS + "mantenimiento/" + mant.getId());
                svc.put("nombre", mant.getNombre());
                if (mant.getPrecioMinimo() != null)
                    svc.put("precio", mant.getPrecioMinimo());
                servicios.add(svc);
            }
            map.put("servicios", servicios);
        }

        return map;
    }

    private Map<String, Object> mecanicoAJsonLd(Mecanico m) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("@type", List.of("schema:Person", "taller:Mecanico"));
        map.put("@id",   DATA_NS + "mecanico/" + m.getId());
        map.put("schema:name",      m.getNombre() + " " + m.getApellido());
        map.put("schema:givenName", m.getNombre());
        map.put("schema:familyName", m.getApellido());
        map.put("schema:jobTitle",  m.getEspecialidad());
        map.put("telefono",         m.getTelefono());
        map.put("taller:estado",    m.getEstado() != null ? m.getEstado().toString() : null);
        map.put("taller:activo",    m.isActivo());

        if (m.getFechaIngreso() != null)
            map.put("schema:startDate", m.getFechaIngreso().toString());

        if (m.getTaller() != null) {
            Map<String, Object> tallerRef = new LinkedHashMap<>();
            tallerRef.put("@type", "schema:AutoRepair");
            tallerRef.put("@id",   DATA_NS + "taller/" + m.getTaller().getId());
            tallerRef.put("nombre", m.getTaller().getNombre());
            tallerRef.put("taller:ciudad", m.getTaller().getCiudad());
            map.put("schema:worksFor", tallerRef);
        }

        return map;
    }

    private Map<String, Object> mantenimientoAJsonLd(Mantenimiento m) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("@type", List.of("schema:Service", "taller:Mantenimiento"));
        map.put("@id",   DATA_NS + "mantenimiento/" + m.getId());
        map.put("nombre",      m.getNombre());
        map.put("descripcion", m.getDescripcion());
        map.put("taller:tipo", m.getTipo() != null ? m.getTipo().toString() : null);
        map.put("taller:activo", m.isActivo());

        // Precio como Offer de Schema.org
        if (m.getPrecioMinimo() != null) {
            Map<String, Object> offer = new LinkedHashMap<>();
            offer.put("@type",          "schema:Offer");
            offer.put("schema:price",   m.getPrecioMinimo());
            offer.put("schema:priceCurrency", "PEN");
            offer.put("schema:priceSpecification", "Precio mínimo referencial");
            map.put("schema:offers", offer);
        }

        return map;
    }

    private Map<String, Object> clienteAJsonLd(Cliente c) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("@id", DATA_NS + "cliente/" + c.getId());
        map.put("schema:telephone", c.getTelefono());
        map.put("schema:email",     c.getCorreoElectronico());
        map.put("schema:address",   c.getDireccion());

        if (c instanceof ClienteNatural cn) {
            map.put("@type", List.of("schema:Person", "taller:ClienteNatural"));
            map.put("schema:name",       cn.getNombre() + " " + cn.getApellido());
            map.put("schema:givenName",  cn.getNombre());
            map.put("schema:familyName", cn.getApellido());
            map.put("taller:dni",        cn.getDni());

        } else if (c instanceof ClienteJuridico cj) {
            map.put("@type", List.of("schema:Organization", "taller:ClienteJuridico"));
            map.put("schema:name",        cj.getRazonSocial());
            map.put("schema:taxID",       cj.getRuc());
            map.put("taller:representanteLegal", cj.getRepresentanteLegal());
        }

        return map;
    }

    private Map<String, Object> vehiculoAJsonLd(Vehiculo v) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("@type", List.of("schema:Vehicle", "taller:Vehiculo"));
        map.put("@id",   DATA_NS + "vehiculo/" + v.getId());
        map.put("schema:name",         v.getMarca() + " " + v.getModelo());
        map.put("schema:brand",        v.getMarca());
        map.put("schema:model",        v.getModelo());
        map.put("schema:modelDate",    v.getAño());
        map.put("schema:color",        v.getColor());
        map.put("schema:fuelType",     v.getTipoCombustible());
        map.put("schema:mileageFromOdometer", v.getKilometraje());
        map.put("schema:vehicleIdentificationNumber", v.getNumeroSerie());
        map.put("taller:placa",        v.getPlaca());
        map.put("taller:estado",       v.getEstado() != null ? v.getEstado().toString() : null);

        // Dueño
        if (v.getCliente() != null) {
            map.put("schema:owns", Map.of(
                    "@id",  DATA_NS + "cliente/" + v.getCliente().getId(),
                    "@type", "schema:Person"
            ));
        }

        return map;
    }
}