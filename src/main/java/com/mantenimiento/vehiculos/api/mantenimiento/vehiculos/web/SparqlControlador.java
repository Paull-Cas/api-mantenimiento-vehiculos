package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.RdfExportService;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.SparqlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/semantica")
@RequiredArgsConstructor
public class SparqlController {

    private final SparqlService sparqlService;
    private final RdfExportService rdfExportService;

    /**
     * POST /api/semantica/exportar
     * Re-exporta todos los datos de MySQL a Fuseki manualmente.
     */
    @PostMapping("/exportar")
    public ResponseEntity<String> exportarDatos() {
        rdfExportService.exportarTodo();
        return ResponseEntity.ok("Datos exportados a Fuseki correctamente");
    }

    /**
     * GET /api/semantica/catalogo
     * Catálogo de mantenimientos ordenados por precio.
     */
    @GetMapping("/catalogo")
    public ResponseEntity<String> getCatalogo() {
        return ResponseEntity.ok(sparqlService.getCatalogoMantenimientos());
    }

    /**
     * GET /api/semantica/mecanicos-disponibles
     * Mecánicos con estado DISPONIBLE.
     */
    @GetMapping("/mecanicos-disponibles")
    public ResponseEntity<String> getMecanicosDisponibles() {
        return ResponseEntity.ok(sparqlService.getMecanicosDisponibles());
    }

    /**
     * GET /api/semantica/ordenes?estado=ABIERTA
     * Órdenes filtradas por estado.
     */
    @GetMapping("/ordenes")
    public ResponseEntity<String> getOrdenesPorEstado(@RequestParam String estado) {
        return ResponseEntity.ok(sparqlService.getOrdenesPorEstado(estado));
    }

    /**
     * GET /api/semantica/historial/{clienteId}
     * Historial de órdenes de un cliente.
     */
    @GetMapping("/historial/{clienteId}")
    public ResponseEntity<String> getHistorialCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(sparqlService.getHistorialPorCliente(clienteId));
    }

    /**
     * GET /api/semantica/mecanicos?especialidad=frenos
     * Mecánicos filtrados por especialidad.
     */
    @GetMapping("/mecanicos")
    public ResponseEntity<String> getMecanicosPorEspecialidad(@RequestParam String especialidad) {
        return ResponseEntity.ok(sparqlService.getMecanicosPorEspecialidad(especialidad));
    }

    /**
     * GET /api/semantica/geo/mecanico-en-ciudad?especialidad=frenos&ciudad=Cajamarca
     *
     * Ejemplo: "quiero un mecánico de frenos en Cajamarca"
     * Devuelve: mecánicos que tienen esa especialidad en talleres de esa ciudad.
     */
    @GetMapping(value = "/geo/mecanico-en-ciudad", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> buscarMecanicoEnCiudad(
            @RequestParam String especialidad,
            @RequestParam String ciudad) {

        List<Map<String, String>> resultados =
                sparqlService.buscarMecanicoEnCiudad(especialidad, ciudad);
        return ResponseEntity.ok(sparqlService.listaAJson(resultados));
    }

    /**
     * GET /api/semantica/geo/mantenimiento-en-ciudad?tipo=motor&ciudad=Baños del Inca
     *
     * Ejemplo: "necesito mantenimiento de motor en Baños del Inca"
     * Devuelve: talleres en esa ciudad que ofrecen ese tipo de mantenimiento.
     */
    @GetMapping(value = "/geo/mantenimiento-en-ciudad", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> buscarMantenimientoEnCiudad(
            @RequestParam String tipo,
            @RequestParam String ciudad) {

        List<Map<String, String>> resultados =
                sparqlService.buscarMantenimientoEnCiudad(tipo, ciudad);
        return ResponseEntity.ok(sparqlService.listaAJson(resultados));
    }

    /**
     * GET /api/semantica/geo/talleres-con-disponibles?ciudad=Cajamarca
     *
     * Todos los talleres de una ciudad con sus mecánicos disponibles.
     */
    @GetMapping(value = "/geo/talleres-con-disponibles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> buscarTalleresConMecanicosDisponibles(
            @RequestParam String ciudad) {

        List<Map<String, String>> resultados =
                sparqlService.buscarTalleresConMecanicosDisponiblesEnCiudad(ciudad);
        return ResponseEntity.ok(sparqlService.listaAJson(resultados));
    }

    /**
     * GET /api/semantica/geo/busqueda-avanzada?especialidad=motor&ciudad=Cajamarca&estado=DISPONIBLE
     *
     * Búsqueda combinada con todos los parámetros opcionales.
     * Cualquiera de los tres puede omitirse para búsquedas más amplias.
     */
    @GetMapping(value = "/geo/busqueda-avanzada", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> busquedaGeograficaAvanzada(
            @RequestParam(required = false, defaultValue = "") String especialidad,
            @RequestParam(required = false, defaultValue = "") String ciudad,
            @RequestParam(required = false, defaultValue = "") String estado) {

        List<Map<String, String>> resultados =
                sparqlService.busquedaGeograficaAvanzada(especialidad, ciudad, estado);
        return ResponseEntity.ok(sparqlService.listaAJson(resultados));
    }

    /**
     * GET /api/semantica/geo/resumen-talleres
     *
     * Resumen de todos los talleres: ciudad, dirección,
     * cantidad de mecánicos y servicios disponibles.
     */
    @GetMapping(value = "/geo/resumen-talleres", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> resumenTalleresPorCiudad() {
        List<Map<String, String>> resultados = sparqlService.resumenTalleresPorCiudad();
        return ResponseEntity.ok(sparqlService.listaAJson(resultados));
    }

    /**
     * GET /api/semantica/geo/historial-vehiculo?placa=ABC-123
     *
     * Historial completo de mantenimientos de un vehículo por placa.
     */
    @GetMapping(value = "/geo/historial-vehiculo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> historialMantenimientosPorPlaca(
            @RequestParam String placa) {

        List<Map<String, String>> resultados =
                sparqlService.historialMantenimientosPorPlaca(placa);
        return ResponseEntity.ok(sparqlService.listaAJson(resultados));
    }
}