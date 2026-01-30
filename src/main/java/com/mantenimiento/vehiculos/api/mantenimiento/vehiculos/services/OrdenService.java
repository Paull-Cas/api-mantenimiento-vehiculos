package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.VehiculoNoEncontradoExcepcion;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrdenService {
    
    // 1️⃣ RECEPCIÓN Y CREACIÓN DE ORDEN
    /**
     * Crea una nueva orden de mantenimiento.
     * - Verifica el vehículo y lo pone EN_MANTENIMIENTO
     * - Crea la orden con estado ABIERTA
     * - Crea las OrdenMantenimiento con estado EN_REVISION
     */
    OrdenResponse crearOrden(OrdenRequest request) throws MantenimientoNoEncontradoExcepcion, VehiculoNoEncontradoExcepcion;
    
    // 2️⃣ ASIGNACIÓN DE MECÁNICOS
    /**
     * Asigna mecánicos a una orden.
     * - Cambia estado de mecánicos a OCUPADO
     * - Crea OrdenMecanico con estado ASIGNADO
     * - Cambia orden a EN_PROCESO
     */
    OrdenResponse asignarMecanicos(AsignarMecanicoRequest request);
    
    // 3️⃣ GESTIÓN DE ESTADOS DE MANTENIMIENTO
    /**
     * Actualiza el estado de un mantenimiento específico dentro de una orden.
     * - Permite transiciones: EN_REVISION -> REVISADO -> COTIZADO -> ACEPTADO/RECHAZADO -> EN_MANTENIMIENTO -> COMPLETADO
     */
    OrdenMantenimientoResponse actualizarEstadoMantenimiento(ActualizarEstadoOrdenMantenimientoRequest request);
    
    /**
     * Inicia el trabajo de mantenimiento por un mecánico
     * - Cambia OrdenMecanico a EN_PROCESO
     * - Cambia OrdenMantenimiento a EN_MANTENIMIENTO
     */
    OrdenResponse iniciarTrabajo(Long ordenId, Long mecanicoId);
    
    /**
     * Finaliza el trabajo de un mecánico en una orden
     * - Cambia OrdenMecanico a FINALIZADO
     * - Cambia Mecanico a DISPONIBLE
     * - Si todos los mantenimientos están completados, cierra la orden
     */
    OrdenResponse finalizarTrabajoMecanico(Long ordenId, Long mecanicoId);
    
    // 4️⃣ CIERRE Y PAGO
    /**
     * Registra el pago de una orden.
     * - Crea el Pago
     * - Cambia vehiculo a LISTO_PARA_ENTREGA si todos los mantenimientos están completados
     */
    PagoResponse registrarPago(PagoRequest request);
    
    /**
     * Entrega el vehículo al cliente
     * - Cambia vehiculo a ENTREGADO
     * - Confirma el pago
     */
    OrdenResponse entregarVehiculo(Long ordenId);
    
    // 5️⃣ CANCELACIÓN
    /**
     * Cancela una orden
     * - Cambia orden a CANCELADA
     * - Libera mecánicos
     * - Cambia vehiculo a DISPONIBLE
     */
    OrdenResponse cancelarOrden(Long ordenId, String motivo);
    
    // 6️⃣ CONSULTAS
    OrdenResponse obtenerOrdenPorId(Long id);
    OrdenResponse obtenerOrdenPorNumero(String numeroOrden);
    List<OrdenResponse> obtenerOrdenesPorVehiculo(Long vehiculoId);
    List<OrdenResponse> obtenerOrdenesPorCliente(Long clienteId);
    Page<OrdenResponse> buscarOrdenes(String keyword, int page, int size);
    
    /**
     * Calcula y actualiza el total de la orden basándose en los costos de los mantenimientos
     */
    OrdenResponse calcularTotal(Long ordenId);
}
