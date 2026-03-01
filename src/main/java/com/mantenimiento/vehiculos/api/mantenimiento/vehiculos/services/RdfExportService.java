package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.*;
import lombok.RequiredArgsConstructor;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RdfExportService {

    // ── Repositories ────────────────────────────────────────────────────────────
    private final ClienteRepositorio       clienteRepository;
    private final VehiculoRepositorio      vehiculoRepository;
    private final OrdenRepositorio         ordenRepository;
    private final MecanicoRepositorio      mecanicoRepository;
    private final MantenimientoRepositorio mantenimientoRepository;
    private final TallerRepositorio        tallerRepository;   // NUEVO

    // ── Namespaces ───────────────────────────────────────────────────────────────
    /** Ontología OWL (clases y propiedades definidas en Protégé) */
    private static final String NS     = "http://taller.com/ontologia/mantenimiento#";
    /** Instancias de datos reales */
    private static final String DATA   = "http://taller.com/datos/";
    /** Schema.org para interoperabilidad web semántica estándar */
    private static final String SCHEMA = "https://schema.org/";

    // ── Fuseki ───────────────────────────────────────────────────────────────────
    private static final String FUSEKI_DATA_ENDPOINT =
            "http://localhost:3030/taller/data?graph=http://taller.com/datos";

    // ════════════════════════════════════════════════════════════════════════════
    //  MÉTODO PRINCIPAL
    // ════════════════════════════════════════════════════════════════════════════

    @Transactional(readOnly = true)
    public void exportarTodo() {
        Model model = ModelFactory.createDefaultModel();

        // Prefijos que aparecerán en el Turtle generado
        model.setNsPrefix("taller", NS);
        model.setNsPrefix("data",   DATA);
        model.setNsPrefix("schema", SCHEMA);

        exportarClientes(model);
        exportarVehiculos(model);
        exportarOrdenes(model);
        exportarMecanicos(model);
        exportarMantenimientos(model);
        exportarTalleres(model);   // NUEVO

        cargarEnFuseki(model);
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  EXPORTADORES POR ENTIDAD
    // ════════════════════════════════════════════════════════════════════════════

    // ── Clientes ─────────────────────────────────────────────────────────────────

    private void exportarClientes(Model model) {
        Property pNombre    = prop(model, "nombre");
        Property pApellido  = prop(model, "apellido");
        Property pDni       = prop(model, "dni");
        Property pRazon     = prop(model, "razonSocial");
        Property pRuc       = prop(model, "ruc");
        Property pTelefono  = prop(model, "telefono");
        Property pCorreo    = prop(model, "correoElectronico");
        Property pDireccion = prop(model, "direccion");

        // Schema.org equivalentes
        Property sName    = schemaProp(model, "name");
        Property sEmail   = schemaProp(model, "email");
        Property sPhone   = schemaProp(model, "telephone");
        Property sAddress = schemaProp(model, "address");

        for (Cliente c : clienteRepository.findAll()) {
            Resource r = model.createResource(DATA + "cliente/" + c.getId());

            if (c instanceof ClienteNatural cn) {
                r.addProperty(RDF.type, res(model, "ClienteNatural"));
                r.addProperty(RDF.type, schemaRes(model, "Person"));
                safeAdd(r, pNombre,   cn.getNombre());
                safeAdd(r, pApellido, cn.getApellido());
                safeAdd(r, pDni,      cn.getDni());
                if (cn.getNombre() != null && cn.getApellido() != null)
                    r.addProperty(sName, cn.getNombre() + " " + cn.getApellido());

            } else if (c instanceof ClienteJuridico cj) {
                r.addProperty(RDF.type, res(model, "ClienteJuridico"));
                r.addProperty(RDF.type, schemaRes(model, "Organization"));
                safeAdd(r, pRazon, cj.getRazonSocial());
                safeAdd(r, pRuc,   cj.getRuc());
                safeAdd(r, sName,  cj.getRazonSocial());
            }

            safeAdd(r, pTelefono,  c.getTelefono());
            safeAdd(r, pCorreo,    c.getCorreoElectronico());
            safeAdd(r, pDireccion, c.getDireccion());
            safeAdd(r, sEmail,     c.getCorreoElectronico());
            safeAdd(r, sPhone,     c.getTelefono());
            safeAdd(r, sAddress,   c.getDireccion());
        }
    }

    // ── Vehículos ────────────────────────────────────────────────────────────────

    private void exportarVehiculos(Model model) {
        Property pPlaca   = prop(model, "placa");
        Property pMarca   = prop(model, "marca");
        Property pModelo  = prop(model, "modelo");
        Property pAnio    = prop(model, "año");
        Property pKm      = prop(model, "kilometraje");
        Property pEstado  = prop(model, "estado");
        Property pCliente = prop(model, "perteneceACliente");

        Property sName    = schemaProp(model, "name");
        Property sMileage = schemaProp(model, "mileageFromOdometer");

        for (Vehiculo v : vehiculoRepository.findAll()) {
            Resource r = model.createResource(DATA + "vehiculo/" + v.getId());
            r.addProperty(RDF.type, res(model, "Vehiculo"));
            r.addProperty(RDF.type, schemaRes(model, "Vehicle"));

            safeAdd(r, pPlaca,  v.getPlaca());
            safeAdd(r, pMarca,  v.getMarca());
            safeAdd(r, pModelo, v.getModelo());
            r.addProperty(pAnio, model.createTypedLiteral(v.getAño()));
            r.addProperty(pKm,   model.createTypedLiteral(v.getKilometraje()));
            if (v.getEstado() != null)
                r.addProperty(pEstado, v.getEstado().toString());

            if (v.getMarca() != null && v.getModelo() != null)
                r.addProperty(sName, v.getMarca() + " " + v.getModelo());
            r.addProperty(sMileage, model.createTypedLiteral(v.getKilometraje()));

            if (v.getCliente() != null)
                r.addProperty(pCliente,
                        model.createResource(DATA + "cliente/" + v.getCliente().getId()));
        }
    }

    // ── Órdenes ──────────────────────────────────────────────────────────────────

    private void exportarOrdenes(Model model) {
        Property pNumero   = prop(model, "numeroOrden");
        Property pEstado   = prop(model, "estado");
        Property pTotal    = prop(model, "total");
        Property pVehiculo = prop(model, "perteneceAVehiculo");

        Property sIdentifier = schemaProp(model, "identifier");
        Property sPrice      = schemaProp(model, "price");

        for (Orden o : ordenRepository.findAll()) {
            Resource r = model.createResource(DATA + "orden/" + o.getId());
            r.addProperty(RDF.type, res(model, "Orden"));
            r.addProperty(RDF.type, schemaRes(model, "Order"));

            safeAdd(r, pNumero, o.getNumeroOrden());
            if (o.getEstado() != null)
                r.addProperty(pEstado, o.getEstado().toString());
            if (o.getTotal() != null) {
                r.addProperty(pTotal, model.createTypedLiteral(o.getTotal().floatValue()));
                r.addProperty(sPrice, model.createTypedLiteral(o.getTotal().floatValue()));
            }
            safeAdd(r, sIdentifier, o.getNumeroOrden());

            if (o.getVehiculo() != null)
                r.addProperty(pVehiculo,
                        model.createResource(DATA + "vehiculo/" + o.getVehiculo().getId()));
        }
    }

    // ── Mecánicos ────────────────────────────────────────────────────────────────

    private void exportarMecanicos(Model model) {
        Property pNombre       = prop(model, "nombre");
        Property pApellido     = prop(model, "apellido");
        Property pEspecialidad = prop(model, "especialidad");
        Property pEstado       = prop(model, "estado");
        Property pTelefono     = prop(model, "telefono");
        Property pTaller       = prop(model, "trabajaEn");

        Property sName     = schemaProp(model, "name");
        Property sJobTitle = schemaProp(model, "jobTitle");
        Property sPhone    = schemaProp(model, "telephone");
        Property sWorksFor = schemaProp(model, "worksFor");

        for (Mecanico m : mecanicoRepository.findAll()) {
            Resource r = model.createResource(DATA + "mecanico/" + m.getId());
            r.addProperty(RDF.type, res(model, "Mecanico"));
            r.addProperty(RDF.type, schemaRes(model, "Person"));

            safeAdd(r, pNombre,       m.getNombre());
            safeAdd(r, pApellido,     m.getApellido());
            safeAdd(r, pEspecialidad, m.getEspecialidad());
            safeAdd(r, pTelefono,     m.getTelefono());
            if (m.getEstado() != null)
                r.addProperty(pEstado, m.getEstado().toString());

            if (m.getNombre() != null && m.getApellido() != null)
                r.addProperty(sName, m.getNombre() + " " + m.getApellido());
            safeAdd(r, sJobTitle, m.getEspecialidad());
            safeAdd(r, sPhone,    m.getTelefono());

            // Relación Mecanico → Taller
            if (m.getTaller() != null) {
                Resource tallerRes = model.createResource(DATA + "taller/" + m.getTaller().getId());
                r.addProperty(pTaller,   tallerRes);
                r.addProperty(sWorksFor, tallerRes);
            }
        }
    }

    // ── Mantenimientos ───────────────────────────────────────────────────────────

    private void exportarMantenimientos(Model model) {
        Property pNombre      = prop(model, "nombre");
        Property pDescripcion = prop(model, "descripcion");
        Property pPrecio      = prop(model, "precioMinimo");
        Property pTipo        = prop(model, "tipo");

        Property sName        = schemaProp(model, "name");
        Property sDescription = schemaProp(model, "description");

        for (Mantenimiento m : mantenimientoRepository.findAll()) {
            Resource r = model.createResource(DATA + "mantenimiento/" + m.getId());
            r.addProperty(RDF.type, res(model, "Mantenimiento"));
            r.addProperty(RDF.type, schemaRes(model, "Service"));

            safeAdd(r, pNombre,      m.getNombre());
            safeAdd(r, pDescripcion, m.getDescripcion());
            if (m.getPrecioMinimo() != null)
                r.addProperty(pPrecio, model.createTypedLiteral(m.getPrecioMinimo().floatValue()));
            if (m.getTipo() != null)
                r.addProperty(pTipo, m.getTipo().toString());

            safeAdd(r, sName,        m.getNombre());
            safeAdd(r, sDescription, m.getDescripcion());
        }
    }

    // ── Talleres (NUEVO) ─────────────────────────────────────────────────────────

    private void exportarTalleres(Model model) {
        Property pNombre    = prop(model, "nombre");
        Property pCiudad    = prop(model, "ciudad");
        Property pDistrito  = prop(model, "distrito");
        Property pDireccion = prop(model, "direccion");
        Property pHorario   = prop(model, "horario");
        Property pTieneMec  = prop(model, "tieneMecanico");
        Property pOfrece    = prop(model, "ofreceMantenimiento");

        Property sName       = schemaProp(model, "name");
        Property sAddress    = schemaProp(model, "address");
        Property sCity       = schemaProp(model, "addressLocality");
        Property sOpenHours  = schemaProp(model, "openingHours");
        Property sEmployee   = schemaProp(model, "employee");
        Property sHasCatalog = schemaProp(model, "hasOfferCatalog");
        Property sStreet     = schemaProp(model, "streetAddress");
        Property sRegion     = schemaProp(model, "addressRegion");
        Property sCountry    = schemaProp(model, "addressCountry");

        for (Taller t : tallerRepository.findAll()) {
            Resource r = model.createResource(DATA + "taller/" + t.getId());
            r.addProperty(RDF.type, res(model, "Taller"));
            r.addProperty(RDF.type, schemaRes(model, "AutoRepair")); // Schema.org específico

            safeAdd(r, pNombre,    t.getNombre());
            safeAdd(r, pCiudad,    t.getCiudad());
            safeAdd(r, pDistrito,  t.getDistrito());
            safeAdd(r, pDireccion, t.getDireccion());
            safeAdd(r, pHorario,   t.getHorario());

            safeAdd(r, sName,      t.getNombre());
            safeAdd(r, sOpenHours, t.getHorario());

            // Dirección estructurada Schema.org (PostalAddress)
            if (t.getCiudad() != null || t.getDireccion() != null) {
                Resource addr = model.createResource(DATA + "taller/" + t.getId() + "/direccion");
                addr.addProperty(RDF.type, schemaRes(model, "PostalAddress"));
                safeAdd(addr, sCity,    t.getCiudad());
                safeAdd(addr, sStreet,  t.getDireccion());
                safeAdd(addr, sRegion,  t.getDistrito());
                addr.addProperty(sCountry, "PE");
                r.addProperty(sAddress, addr);
            }

            // ── Relación Taller → Mecánicos ──────────────────────────────────────
            if (t.getMecanicos() != null) {
                for (Mecanico m : t.getMecanicos()) {
                    Resource mecRes = model.createResource(DATA + "mecanico/" + m.getId());
                    r.addProperty(pTieneMec, mecRes);
                    r.addProperty(sEmployee, mecRes);
                }
            }

            // ── Relación Taller → Mantenimientos ─────────────────────────────────
            if (t.getMantenimientos() != null) {
                for (Mantenimiento mant : t.getMantenimientos()) {
                    Resource mantRes = model.createResource(DATA + "mantenimiento/" + mant.getId());
                    r.addProperty(pOfrece,    mantRes);
                    r.addProperty(sHasCatalog, mantRes);
                }
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  CARGA EN FUSEKI
    // ════════════════════════════════════════════════════════════════════════════

    private void cargarEnFuseki(Model model) {
        try {
            StringWriter sw = new StringWriter();
            model.write(sw, "TURTLE");

            HttpClient http = HttpClient.newHttpClient();

            // 1. Borrar grafo anterior
            http.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(FUSEKI_DATA_ENDPOINT))
                            .DELETE()
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            // 2. Cargar nuevos datos
            HttpResponse<String> resp = http.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(FUSEKI_DATA_ENDPOINT))
                            .header("Content-Type", "text/turtle")
                            .POST(HttpRequest.BodyPublishers.ofString(sw.toString()))
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("Fuseki status: " + resp.statusCode()
                    + " | Triples exportados: " + model.size());

        } catch (Exception e) {
            throw new RuntimeException("Error cargando datos en Fuseki: " + e.getMessage(), e);
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ════════════════════════════════════════════════════════════════════════════

    private Property prop(Model m, String local) {
        return m.createProperty(NS + local);
    }

    private Property schemaProp(Model m, String local) {
        return m.createProperty(SCHEMA + local);
    }

    private Resource res(Model m, String local) {
        return m.createResource(NS + local);
    }

    private Resource schemaRes(Model m, String local) {
        return m.createResource(SCHEMA + local);
    }

    /** Agrega una literal solo si el valor no es null ni vacío */
    private void safeAdd(Resource r, Property p, String value) {
        if (value != null && !value.isBlank()) {
            r.addProperty(p, value);
        }
    }
}