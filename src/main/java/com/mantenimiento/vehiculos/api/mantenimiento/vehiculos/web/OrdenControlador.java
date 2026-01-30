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

    @PostMapping("/crear")
    public ResponseEntity<OrdenResponse> crearOrden(@RequestBody OrdenRequest request) throws VehiculoNoEncontradoExcepcion, MantenimientoNoEncontradoExcepcion {
        OrdenResponse response = ordenService.crearOrden(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ========== ASIGNACIÓN DE MECÁNICOS ==========

    @PostMapping("/asignar-mecanicos")
    public ResponseEntity<OrdenResponse> asignarMecanicos(@RequestBody AsignarMecanicoRequest request) {
        OrdenResponse response = ordenService.asignarMecanicos(request);
        return ResponseEntity.ok(response);
    }

    // ========== GESTIÓN DE ESTADOS DE MANTENIMIENTO ==========

    @PutMapping("/mantenimiento/actualizar-estado")
    public ResponseEntity<OrdenMantenimientoResponse> actualizarEstadoMantenimiento(
            @RequestBody ActualizarEstadoOrdenMantenimientoRequest request) {
        OrdenMantenimientoResponse response = ordenService.actualizarEstadoMantenimiento(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{ordenId}/mecanico/{mecanicoId}/iniciar-trabajo")
    public ResponseEntity<OrdenResponse> iniciarTrabajo(
            @PathVariable Long ordenId,
            @PathVariable Long mecanicoId) {
        OrdenResponse response = ordenService.iniciarTrabajo(ordenId, mecanicoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{ordenId}/mecanico/{mecanicoId}/finalizar-trabajo")
    public ResponseEntity<OrdenResponse> finalizarTrabajoMecanico(
            @PathVariable Long ordenId,
            @PathVariable Long mecanicoId) {
        OrdenResponse response = ordenService.finalizarTrabajoMecanico(ordenId, mecanicoId);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/registrar-pago")
    public ResponseEntity<PagoResponse> registrarPago(@RequestBody PagoRequest request) {
        PagoResponse response = ordenService.registrarPago(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{ordenId}/entregar-vehiculo")
    public ResponseEntity<OrdenResponse> entregarVehiculo(@PathVariable Long ordenId) {
        OrdenResponse response = ordenService.entregarVehiculo(ordenId);
        return ResponseEntity.ok(response);
    }

    // ========== CANCELACIÓN ==========
    
    @PreAuthorize("hasAuthority('DELETE_ORDEN')")
    @PostMapping("/{ordenId}/cancelar")
    public ResponseEntity<OrdenResponse> cancelarOrden(
            @PathVariable Long ordenId,
            @RequestParam(required = false) String motivo) {
        OrdenResponse response = ordenService.cancelarOrden(ordenId, motivo);
        return ResponseEntity.ok(response);
    }

    // ========== CONSULTAS ==========
    
    @PreAuthorize("hasAuthority('READ_ORDEN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponse> obtenerOrdenPorId(@PathVariable Long id) {
        OrdenResponse response = ordenService.obtenerOrdenPorId(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('READ_ORDEN')")
    @GetMapping("/numero/{numeroOrden}")
    public ResponseEntity<OrdenResponse> obtenerOrdenPorNumero(@PathVariable String numeroOrden) {
        OrdenResponse response = ordenService.obtenerOrdenPorNumero(numeroOrden);
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

    @PreAuthorize("hasAuthority('READ_ORDEN')")
    @GetMapping("/buscar")
    public ResponseEntity<Page<OrdenResponse>> buscarOrdenes(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<OrdenResponse> response = ordenService.buscarOrdenes(keyword, page, size);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('READ_ORDEN')")
    @GetMapping("/{ordenId}/calcular-total")
    public ResponseEntity<OrdenResponse> calcularTotal(@PathVariable Long ordenId) {
        OrdenResponse response = ordenService.calcularTotal(ordenId);
        return ResponseEntity.ok(response);
    }
}
