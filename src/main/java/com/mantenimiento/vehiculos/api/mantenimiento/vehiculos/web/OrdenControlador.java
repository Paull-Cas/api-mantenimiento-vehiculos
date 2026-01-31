package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.VehiculoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenControlador {

    private final OrdenService ordenService;

    // ========== RECEPCIÓN Y CREACIÓN DE ORDEN ==========
    @PreAuthorize("hasAuthority('CREATE_ORDEN')")
    @PostMapping("/crear")
    public ResponseEntity<OrdenResponse> crearOrden(@RequestBody OrdenRequest request)
            throws VehiculoNoEncontradoExcepcion, MantenimientoNoEncontradoExcepcion {
        OrdenResponse response = ordenService.crearOrden(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // agrega método de actualizacion PERMISO = ACTUALIZAR_ORDEN

    // ========== CANCELACIÓN ==========
    @PreAuthorize("hasAuthority('CANCELAR_ORDEN')")
    @PostMapping("/cancelar/{ordenId}")
    public ResponseEntity<OrdenResponse> cancelarOrden(@PathVariable Long ordenId) {
        OrdenResponse response = ordenService.cancelarOrden(ordenId);
        return ResponseEntity.ok(response);
    }

    // ========== ASIGNACIÓN DE MECÁNICOS ==========
    @PreAuthorize("hasAuthority('ASIGNAR_MECANICO')")
    @PostMapping("/asignar-mecanicos")
    public ResponseEntity<OrdenResponse> asignarMecanicos(@RequestBody AsignarMecanicoRequest request) {
        OrdenResponse response = ordenService.asignarMecanicos(request);
        return ResponseEntity.ok(response);
    }

    // agregar método de actualización de asignación permiso = ACTUALIZAR_ASIGNACION//
    

    // ========== GESTIÓN DE ESTADOS DE MANTENIMIENTO ==========
    @PreAuthorize("hasAuthority('CAMBIAR_ESTADO_ORDEN')")
    @PutMapping("/mantenimiento/actualizar-estado")
    public ResponseEntity<OrdenMantenimientoResponse> actualizarEstadoMantenimiento(
            @RequestBody ActualizarEstadoOrdenMantenimientoRequest request) {
        OrdenMantenimientoResponse response = ordenService.actualizarEstadoMantenimiento(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('INICIAR_TRABAJO')")
    @PostMapping("/{ordenId}/mecanico/{mecanicoId}/iniciar-trabajo")
    public ResponseEntity<OrdenResponse> iniciarTrabajo(
            @PathVariable Long ordenId,
            @PathVariable Long mecanicoId) {
        OrdenResponse response = ordenService.iniciarTrabajo(ordenId, mecanicoId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('FINALIZAR_TRABAJO')")
    @PostMapping("/{ordenId}/mecanico/{mecanicoId}/finalizar-trabajo")
    public ResponseEntity<OrdenResponse> finalizarTrabajoMecanico(
            @PathVariable Long ordenId,
            @PathVariable Long mecanicoId) {
        OrdenResponse response = ordenService.finalizarTrabajoMecanico(ordenId, mecanicoId);
        return ResponseEntity.ok(response);
    }

    // ========== PAGOS ==========
    // Roles: Encargado de Pagos, Administrador
    @PreAuthorize("hasAuthority('CREATE_PAGO')")
    @PostMapping("/registrar-pago")
    public ResponseEntity<PagoResponse> registrarPago(@RequestBody PagoRequest request) {
        PagoResponse response = ordenService.registrarPago(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ========== ENTREGA ==========
    // Roles: Recepcionista, Supervisor de Mecánicos, Administrador
    @PreAuthorize("hasAuthority('ENTREGAR_VEHICULO')")
    @PostMapping("/entregar-vehiculo/{ordenId}")
    public ResponseEntity<OrdenResponse> entregarVehiculo(@PathVariable Long ordenId) {
        OrdenResponse response = ordenService.entregarVehiculo(ordenId);
        return ResponseEntity.ok(response);
    }

    // =========================================================================================
    // =========================================================================================
    // =========================================================================================
    // ========== CONSULTAS GENERALES ==========

    @PreAuthorize("hasAuthority('READ_ORDEN')")
    @GetMapping("/listar/con-busqueda")
    public ResponseEntity<Page<OrdenResponse>> buscarOrdenes(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<OrdenResponse> response = ordenService.buscarOrdenes(keyword, page, size);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('READ_ORDEN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponse> obtenerOrdenPorId(@PathVariable Long id) {
        OrdenResponse response = ordenService.obtenerOrdenPorId(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('READ_ORDEN')")
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<OrdenResponse>> obtenerOrdenesPorVehiculo(@PathVariable Long vehiculoId) {
        List<OrdenResponse> response = ordenService.obtenerOrdenesPorVehiculo(vehiculoId);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAuthority('READ_ORDEN')")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<OrdenResponse>> obtenerOrdenesPorCliente(@PathVariable Long clienteId) {
        List<OrdenResponse> response = ordenService.obtenerOrdenesPorCliente(clienteId);
        return ResponseEntity.ok(response);
    }


    // ========== ENDPOINTS PARA CLIENTE ==========
    @PreAuthorize("hasAuthority('READ_ORDEN_PROPIA')")
    @GetMapping("/mis-ordenes")
    public ResponseEntity<List<OrdenResponse>> obtenerMisOrdenes() {
        // El servicio debe obtener el cliente del usuario autenticado
        List<OrdenResponse> response = ordenService.obtenerOrdenesDelClienteAutenticado();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('READ_PAGO_PROPIO')")
    @GetMapping("/mis-pagos")
    public ResponseEntity<List<PagoResponse>> obtenerMisPagos() {
        List<PagoResponse> response = ordenService.obtenerPagosDelClienteAutenticado();
        return ResponseEntity.ok(response);
    }

    // ========== ENDPOINTS PARA MECÁNICO ==========
    @PreAuthorize("hasAuthority('READ_MIS_TRABAJOS')")
    @GetMapping("/mis-trabajos")
    public ResponseEntity<List<OrdenResponse>> obtenerMisTrabajos() {
        // El servicio debe obtener las órdenes asignadas al mecánico autenticado
        List<OrdenResponse> response = ordenService.obtenerOrdenesDelMecanicoAutenticado();
        return ResponseEntity.ok(response);
    }
}