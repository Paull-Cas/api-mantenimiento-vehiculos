package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.jena.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SparqlService {

    private static final String FUSEKI_URL = "http://localhost:3030/taller/sparql";

    private static final String PREFIX =
            "PREFIX taller: <http://taller.com/ontologia/mantenimiento#> " +
                    "PREFIX data:   <http://taller.com/datos/> " +
                    "PREFIX schema: <https://schema.org/> " +
                    "PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#> ";

    private static final String GRAPH = "FROM <http://taller.com/datos> ";

    public ResultSet ejecutarConsulta(String sparql) {
        String consulta = PREFIX + sparql;
        QueryExecution qe = QueryExecutionFactory.sparqlService(FUSEKI_URL, consulta);
        return qe.execSelect();
    }

    public String getCatalogoMantenimientos() {
        long inicio = System.currentTimeMillis();

        String sparql = "SELECT ?nombre ?precio ?tipo ?descripcion " +
                GRAPH +
                "WHERE { " +
                "  ?mant a taller:Mantenimiento ; " +
                "        taller:nombre ?nombre ; " +
                "        taller:precioMinimo ?precio . " +
                "  OPTIONAL { ?mant taller:tipo ?tipo } " +
                "  OPTIONAL { ?mant taller:descripcion ?descripcion } " +
                "} ORDER BY ASC(?precio)";

        ResultSet rs = ejecutarConsulta(sparql);
        StringBuilder sb = new StringBuilder();
        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            sb.append(sol.getLiteral("nombre").getString())
                    .append(" - S/").append(sol.getLiteral("precio").getFloat());
            if (sol.contains("tipo"))
                sb.append(" | Tipo: ").append(sol.getLiteral("tipo").getString());
            sb.append("\n");
        }

        long fin = System.currentTimeMillis();
        System.out.println("╔══════════════════════════════════════════");
        System.out.println("║ SPARQL - getCatalogoMantenimientos: " + (fin - inicio) + " ms");
        System.out.println("╚══════════════════════════════════════════");

        return sb.toString();
    }

    public String getMecanicosDisponibles() {
        String sparql = "SELECT ?nombre ?apellido ?especialidad ?taller ?ciudad " +
                GRAPH +
                "WHERE { " +
                "  ?mecanico a taller:Mecanico ; " +
                "            taller:nombre ?nombre ; " +
                "            taller:apellido ?apellido ; " +
                "            taller:especialidad ?especialidad ; " +
                "            taller:estado ?estado . " +
                "  FILTER(?estado = \"DISPONIBLE\") " +
                "  OPTIONAL { " +
                "    ?mecanico taller:trabajaEn ?t . " +
                "    ?t taller:nombre ?taller ; " +
                "       taller:ciudad ?ciudad . " +
                "  } " +
                "} ORDER BY ?nombre";

        ResultSet rs = ejecutarConsulta(sparql);
        StringBuilder sb = new StringBuilder();
        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            sb.append(sol.getLiteral("nombre").getString())
                    .append(" ").append(sol.getLiteral("apellido").getString())
                    .append(" - ").append(sol.getLiteral("especialidad").getString());
            if (sol.contains("ciudad"))
                sb.append(" | ").append(sol.getLiteral("ciudad").getString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getOrdenesPorEstado(String estado) {
        String sparql = "SELECT ?numeroOrden ?estado ?placa ?marca " +
                GRAPH +
                "WHERE { " +
                "  ?orden a taller:Orden ; " +
                "         taller:numeroOrden ?numeroOrden ; " +
                "         taller:estado ?estado ; " +
                "         taller:perteneceAVehiculo ?vehiculo . " +
                "  ?vehiculo taller:placa ?placa ; " +
                "            taller:marca ?marca . " +
                "  FILTER(?estado = \"" + estado + "\") " +
                "} ORDER BY ?numeroOrden";

        ResultSet rs = ejecutarConsulta(sparql);
        StringBuilder sb = new StringBuilder();
        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            sb.append(sol.getLiteral("numeroOrden").getString())
                    .append(" | ").append(sol.getLiteral("estado").getString())
                    .append(" | Placa: ").append(sol.getLiteral("placa").getString())
                    .append(" | Marca: ").append(sol.getLiteral("marca").getString())
                    .append("\n");
        }
        return sb.toString();
    }

    public String getHistorialPorCliente(Long clienteId) {
        long inicio = System.currentTimeMillis();

        String sparql = "SELECT ?numeroOrden ?estado ?placa ?marca " +
                GRAPH +
                "WHERE { " +
                "  ?vehiculo taller:perteneceACliente <http://taller.com/datos/cliente/" + clienteId + "> ; " +
                "            taller:placa ?placa ; " +
                "            taller:marca ?marca . " +
                "  ?orden a taller:Orden ; " +
                "         taller:numeroOrden ?numeroOrden ; " +
                "         taller:estado ?estado ; " +
                "         taller:perteneceAVehiculo ?vehiculo . " +
                "} ORDER BY ?numeroOrden";

        ResultSet rs = ejecutarConsulta(sparql);
        StringBuilder sb = new StringBuilder();
        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            sb.append(sol.getLiteral("numeroOrden").getString())
                    .append(" | ").append(sol.getLiteral("estado").getString())
                    .append(" | Placa: ").append(sol.getLiteral("placa").getString())
                    .append(" | Marca: ").append(sol.getLiteral("marca").getString())
                    .append("\n");
        }

        long fin = System.currentTimeMillis();
        System.out.println("╔══════════════════════════════════════════");
        System.out.println("║ SPARQL - getHistorialPorCliente (id=" + clienteId + "): " + (fin - inicio) + " ms");
        System.out.println("╚══════════════════════════════════════════");

        return sb.toString();
    }

    public String getMecanicosPorEspecialidad(String especialidad) {
        String sparql = "SELECT ?nombre ?apellido ?especialidad ?estado " +
                GRAPH +
                "WHERE { " +
                "  ?mecanico a taller:Mecanico ; " +
                "            taller:nombre ?nombre ; " +
                "            taller:apellido ?apellido ; " +
                "            taller:especialidad ?especialidad ; " +
                "            taller:estado ?estado . " +
                "  FILTER(CONTAINS(LCASE(STR(?especialidad)), LCASE(\"" + especialidad + "\"))) " +
                "} ORDER BY ?nombre";

        ResultSet rs = ejecutarConsulta(sparql);
        StringBuilder sb = new StringBuilder();
        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            sb.append(sol.getLiteral("nombre").getString())
                    .append(" ").append(sol.getLiteral("apellido").getString())
                    .append(" - ").append(sol.getLiteral("especialidad").getString())
                    .append(" | Estado: ").append(sol.getLiteral("estado").getString())
                    .append("\n");
        }
        return sb.toString();
    }

    /**
     * CONSULTA 1 — "mecánico de frenos en Cajamarca"
     * Busca mecánicos por especialidad + ciudad del taller donde trabajan.
     *
     * Ejemplo: buscarMecanicoEnCiudad("frenos", "Cajamarca")
     */
    public List<Map<String, String>> buscarMecanicoEnCiudad(String especialidad, String ciudad) {
        String sparql = "SELECT ?nombre ?apellido ?especialidad ?estado ?telefono " +
                "       ?tallerNombre ?ciudad ?distrito ?direccion ?horario " +
                GRAPH +
                "WHERE { " +
                "  ?mecanico a taller:Mecanico ; " +
                "            taller:nombre ?nombre ; " +
                "            taller:apellido ?apellido ; " +
                "            taller:especialidad ?especialidad ; " +
                "            taller:estado ?estado ; " +
                "            taller:trabajaEn ?taller . " +
                "  ?taller taller:nombre ?tallerNombre ; " +
                "          taller:ciudad ?ciudad . " +
                "  OPTIONAL { ?mecanico taller:telefono  ?telefono  } " +
                "  OPTIONAL { ?taller   taller:distrito  ?distrito  } " +
                "  OPTIONAL { ?taller   taller:direccion ?direccion } " +
                "  OPTIONAL { ?taller   taller:horario   ?horario   } " +
                "  FILTER(CONTAINS(LCASE(STR(?especialidad)), LCASE(\"" + especialidad + "\"))) " +
                // Busca por ciudad O por distrito — así "Baños del Inca" funciona
                "  FILTER( " +
                "    CONTAINS(LCASE(STR(?ciudad)),   LCASE(\"" + ciudad + "\")) || " +
                "    CONTAINS(LCASE(STR(?distrito)), LCASE(\"" + ciudad + "\")) " +
                "  ) " +
                "} ORDER BY ?nombre";

        return ejecutarComoLista(sparql,
                "nombre", "apellido", "especialidad", "estado", "telefono",
                "tallerNombre", "ciudad", "distrito", "direccion", "horario");
    }

    /**
     * CONSULTA 2 — "mantenimiento de motor en Baños del Inca"
     * Busca talleres en una ciudad que ofrezcan un tipo de mantenimiento.
     *
     * Ejemplo: buscarMantenimientoEnCiudad("motor", "Baños del Inca")
     */
    public List<Map<String, String>> buscarMantenimientoEnCiudad(String tipoMantenimiento, String ciudad) {
        String sparql = "SELECT ?tallerNombre ?ciudad ?distrito ?direccion ?horario " +
                "       ?mantenimientoNombre ?descripcion ?precio ?tipo " +
                GRAPH +
                "WHERE { " +
                "  ?taller a taller:Taller ; " +
                "          taller:nombre ?tallerNombre ; " +
                "          taller:ciudad ?ciudad ; " +
                "          taller:ofreceMantenimiento ?mantenimiento . " +
                "  ?mantenimiento taller:nombre ?mantenimientoNombre ; " +
                "                 taller:precioMinimo ?precio . " +
                "  OPTIONAL { ?taller       taller:distrito    ?distrito    } " +
                "  OPTIONAL { ?taller       taller:direccion   ?direccion   } " +
                "  OPTIONAL { ?taller       taller:horario     ?horario     } " +
                "  OPTIONAL { ?mantenimiento taller:descripcion ?descripcion } " +
                "  OPTIONAL { ?mantenimiento taller:tipo        ?tipo        } " +
                "  FILTER(CONTAINS(LCASE(STR(?mantenimientoNombre)), LCASE(\"" + tipoMantenimiento + "\")) || " +
                "         CONTAINS(LCASE(STR(?tipo)),                LCASE(\"" + tipoMantenimiento + "\"))) " +
                // Busca por ciudad O por distrito
                "  FILTER( " +
                "    CONTAINS(LCASE(STR(?ciudad)),   LCASE(\"" + ciudad + "\")) || " +
                "    CONTAINS(LCASE(STR(?distrito)), LCASE(\"" + ciudad + "\")) " +
                "  ) " +
                "} ORDER BY ?precio";

        return ejecutarComoLista(sparql,
                "tallerNombre", "ciudad", "distrito", "direccion", "horario",
                "mantenimientoNombre", "descripcion", "precio", "tipo");
    }

    /**
     * CONSULTA 3 — Todos los talleres de una ciudad con sus mecánicos disponibles
     * Ejemplo: buscarTalleresConMecanicosDisponiblesEnCiudad("Cajamarca")
     */
    public List<Map<String, String>> buscarTalleresConMecanicosDisponiblesEnCiudad(String ciudad) {
        String sparql = "SELECT ?tallerNombre ?ciudad ?distrito ?direccion ?horario " +
                "       ?mecNombre ?mecApellido ?especialidad " +
                GRAPH +
                "WHERE { " +
                "  ?taller a taller:Taller ; " +
                "          taller:nombre ?tallerNombre ; " +
                "          taller:ciudad ?ciudad ; " +
                "          taller:tieneMecanico ?mecanico . " +
                "  ?mecanico taller:nombre ?mecNombre ; " +
                "            taller:apellido ?mecApellido ; " +
                "            taller:especialidad ?especialidad ; " +
                "            taller:estado ?estado . " +
                "  OPTIONAL { ?taller taller:distrito  ?distrito  } " +
                "  OPTIONAL { ?taller taller:direccion ?direccion } " +
                "  OPTIONAL { ?taller taller:horario   ?horario   } " +
                "  FILTER(?estado = \"DISPONIBLE\") " +
                // Busca por ciudad O por distrito
                "  FILTER( " +
                "    CONTAINS(LCASE(STR(?ciudad)),   LCASE(\"" + ciudad + "\")) || " +
                "    CONTAINS(LCASE(STR(?distrito)), LCASE(\"" + ciudad + "\")) " +
                "  ) " +
                "} ORDER BY ?tallerNombre ?mecNombre";

        return ejecutarComoLista(sparql,
                "tallerNombre", "ciudad", "distrito", "direccion", "horario",
                "mecNombre", "mecApellido", "especialidad");
    }

    /**
     * CONSULTA 4 — Búsqueda combinada: especialidad + ciudad + estado del mecánico
     * Más genérica para el frontend.
     * Ejemplo: busquedaGeograficaAvanzada("frenos", "Cajamarca", "DISPONIBLE")
     */
    public List<Map<String, String>> busquedaGeograficaAvanzada(
            String especialidad, String ciudad, String estado) {

        String filtroEspecialidad = (especialidad != null && !especialidad.isBlank())
                ? "FILTER(CONTAINS(LCASE(STR(?especialidad)), LCASE(\"" + especialidad + "\"))) "
                : "";
        // Busca por ciudad O por distrito en la búsqueda avanzada también
        String filtroCiudad = (ciudad != null && !ciudad.isBlank())
                ? "FILTER( " +
                "  CONTAINS(LCASE(STR(?ciudad)),   LCASE(\"" + ciudad + "\")) || " +
                "  CONTAINS(LCASE(STR(?distrito)), LCASE(\"" + ciudad + "\")) " +
                ") "
                : "";
        String filtroEstado = (estado != null && !estado.isBlank())
                ? "FILTER(?estado = \"" + estado.toUpperCase() + "\") "
                : "";

        String sparql = "SELECT ?mecNombre ?mecApellido ?especialidad ?estado ?telefono " +
                "       ?tallerNombre ?ciudad ?distrito ?direccion ?horario " +
                GRAPH +
                "WHERE { " +
                "  ?mecanico a taller:Mecanico ; " +
                "            taller:nombre ?mecNombre ; " +
                "            taller:apellido ?mecApellido ; " +
                "            taller:especialidad ?especialidad ; " +
                "            taller:estado ?estado ; " +
                "            taller:trabajaEn ?taller . " +
                "  ?taller taller:nombre ?tallerNombre ; " +
                "          taller:ciudad ?ciudad . " +
                "  OPTIONAL { ?mecanico taller:telefono  ?telefono  } " +
                "  OPTIONAL { ?taller   taller:distrito  ?distrito  } " +
                "  OPTIONAL { ?taller   taller:direccion ?direccion } " +
                "  OPTIONAL { ?taller   taller:horario   ?horario   } " +
                filtroEspecialidad +
                filtroCiudad +
                filtroEstado +
                "} ORDER BY ?ciudad ?mecNombre";

        return ejecutarComoLista(sparql,
                "mecNombre", "mecApellido", "especialidad", "estado", "telefono",
                "tallerNombre", "ciudad", "distrito", "direccion", "horario");
    }

    /**
     * CONSULTA 5 — Resumen de talleres: cuántos mecánicos y servicios tiene cada taller
     * Útil para mostrar un mapa de talleres con capacidad.
     */
    public List<Map<String, String>> resumenTalleresPorCiudad() {
        String sparql = "SELECT ?tallerNombre ?ciudad ?distrito ?direccion ?horario " +
                "       (COUNT(DISTINCT ?mecanico)     AS ?totalMecanicos) " +
                "       (COUNT(DISTINCT ?mantenimiento) AS ?totalServicios) " +
                GRAPH +
                "WHERE { " +
                "  ?taller a taller:Taller ; " +
                "          taller:nombre ?tallerNombre ; " +
                "          taller:ciudad ?ciudad . " +
                "  OPTIONAL { ?taller taller:distrito          ?distrito      } " +
                "  OPTIONAL { ?taller taller:direccion         ?direccion     } " +
                "  OPTIONAL { ?taller taller:horario           ?horario       } " +
                "  OPTIONAL { ?taller taller:tieneMecanico     ?mecanico      } " +
                "  OPTIONAL { ?taller taller:ofreceMantenimiento ?mantenimiento } " +
                "} GROUP BY ?tallerNombre ?ciudad ?distrito ?direccion ?horario " +
                "  ORDER BY ?ciudad ?tallerNombre";

        return ejecutarComoLista(sparql,
                "tallerNombre", "ciudad", "distrito", "direccion", "horario",
                "totalMecanicos", "totalServicios");
    }

    /**
     * CONSULTA 6 — Historial de mantenimientos de un vehículo (por placa)
     * Ejemplo: historialMantenimientosPorPlaca("ABC-123")
     */
    public List<Map<String, String>> historialMantenimientosPorPlaca(String placa) {
        String sparql = "SELECT ?numeroOrden ?estadoOrden ?mantenimientoNombre ?estadoMant " +
                "       ?costo ?fechaInicio ?fechaFin ?mecNombre ?mecApellido " +
                GRAPH +
                "WHERE { " +
                "  ?vehiculo a taller:Vehiculo ; " +
                "            taller:placa \"" + placa + "\" . " +
                "  ?orden a taller:Orden ; " +
                "         taller:perteneceAVehiculo ?vehiculo ; " +
                "         taller:numeroOrden ?numeroOrden ; " +
                "         taller:estado ?estadoOrden . " +
                // Órden de mantenimiento (join tabla intermedia)
                "  OPTIONAL { " +
                "    ?om a taller:OrdenMantenimiento ; " +
                "        taller:perteneceAOrden ?orden ; " +
                "        taller:mantenimientoNombre ?mantenimientoNombre ; " +
                "        taller:estado ?estadoMant . " +
                "    OPTIONAL { ?om taller:costo     ?costo     } " +
                "    OPTIONAL { ?om taller:fechaInicio ?fechaInicio } " +
                "    OPTIONAL { ?om taller:fechaFin   ?fechaFin   } " +
                "  } " +
                "  OPTIONAL { " +
                "    ?ome a taller:OrdenMecanico ; " +
                "         taller:perteneceAOrden ?orden ; " +
                "         taller:tieneMecanico ?mec . " +
                "    ?mec taller:nombre   ?mecNombre ; " +
                "         taller:apellido ?mecApellido . " +
                "  } " +
                "} ORDER BY ?numeroOrden";

        return ejecutarComoLista(sparql,
                "numeroOrden", "estadoOrden", "mantenimientoNombre", "estadoMant",
                "costo", "fechaInicio", "fechaFin", "mecNombre", "mecApellido");
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ════════════════════════════════════════════════════════════════════════════

    /**
     * Ejecuta una consulta SPARQL y devuelve los resultados como lista de mapas.
     * Cada mapa corresponde a una fila, con clave = nombre de variable SPARQL.
     */
    public List<Map<String, String>> ejecutarComoLista(String sparql, String... variables) {
        List<Map<String, String>> resultados = new ArrayList<>();
        ResultSet rs = ejecutarConsulta(sparql);

        while (rs.hasNext()) {
            QuerySolution sol = rs.nextSolution();
            Map<String, String> fila = new java.util.LinkedHashMap<>();
            for (String var : variables) {
                if (sol.contains(var) && sol.get(var) != null) {
                    fila.put(var, sol.get(var).isLiteral()
                            ? sol.getLiteral(var).getString()
                            : sol.getResource(var).getURI());
                } else {
                    fila.put(var, "");
                }
            }
            resultados.add(fila);
        }
        return resultados;
    }

    /**
     * Convierte una lista de mapas a JSON string (para los endpoints).
     */
    public String listaAJson(List<Map<String, String>> lista) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode array = mapper.createArrayNode();
            for (Map<String, String> fila : lista) {
                ObjectNode node = mapper.createObjectNode();
                fila.forEach(node::put);
                array.add(node);
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(array);
        } catch (Exception e) {
            return "[]";
        }
    }
}