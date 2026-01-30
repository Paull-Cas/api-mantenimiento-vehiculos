package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.OrdenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepositorio ordenRepositorio;
    private final VehiculoRepositorio vehiculoRepositorio;
    private final MantenimientoRepositorio mantenimientoRepositorio;
    private final OrdenMantenimientoRepositorio ordenMantenimientoRepositorio;
    private final MecanicoRepositorio mecanicoRepositorio;
    private final OrdenMecanicoRepositorio ordenMecanicoRepositorio;
    private final PagoRepositorio pagoRepositorio;
    
    private final OrdenMapper ordenMapper;
    private final MecanicoMapper mecanicoMapper;
    private final PagoMapper pagoMapper;

    
    @Override
    public OrdenResponse crearOrden(OrdenRequest request) throws MantenimientoNoEncontradoExcepcion, VehiculoNoEncontradoExcepcion {
        log.info("Creando nueva orden para vehículo ID: {}", request.getVehiculoId());
        
        // Validar que el vehículo existe
        Vehiculo vehiculo = vehiculoRepositorio.findById(request.getVehiculoId())
                .orElseThrow(() -> new VehiculoNoEncontradoExcepcion(
                        "Vehículo no encontrado con ID: " + request.getVehiculoId()));
        
        // Verificar que el vehículo no está ya en mantenimiento
        validarVehiculoDisponible(vehiculo);
        
        // Cambiar estado del vehículo a EN_MANTENIMIENTO
        vehiculo.setEstado(EstadoVehiculo.EN_MANTENIMIENTO);
        vehiculoRepositorio.save(vehiculo);
        
        // Crear la orden
        Orden orden = new Orden();
        orden.setNumeroOrden(generarNumeroOrden());
        orden.setVehiculo(vehiculo);
        orden.setFechaIngreso(LocalDate.now());
        orden.setEstado(EstadoOrden.ABIERTA);
        orden.setTotal(BigDecimal.ZERO);
        
        Orden ordenGuardada = ordenRepositorio.save(orden);
        
        // Crear OrdenMantenimiento para cada servicio solicitado
        List<OrdenMantenimiento> ordenesMantenimiento = new ArrayList<>();
        for (Long mantenimientoId : request.getMantenimientosIds()) {
            Mantenimiento mantenimiento = mantenimientoRepositorio.findById(mantenimientoId)
                    .orElseThrow(() -> new MantenimientoNoEncontradoExcepcion(
                            "Mantenimiento no encontrado con ID: " + mantenimientoId));
            
            if (!mantenimiento.isActivo()) {
                throw new IllegalStateException("El mantenimiento con ID " + mantenimientoId + " no está activo");
            }
            
            OrdenMantenimiento om = new OrdenMantenimiento();
            om.setOrden(ordenGuardada);
            om.setMantenimiento(mantenimiento);
            om.setEstado(EstadoOrdenMantenimiento.EN_REVISION);
            om.setCosto(mantenimiento.getPrecioMinimo()); // Costo inicial estimado
            
            ordenesMantenimiento.add(ordenMantenimientoRepositorio.save(om));
        }
        
        ordenGuardada.setOrdenesMantenimiento(ordenesMantenimiento);
        
        log.info("Orden creada exitosamente: {}", ordenGuardada.getNumeroOrden());
        return ordenMapper.toResponse(ordenGuardada);
    }

    // ========== 2️⃣ ASIGNACIÓN DE MECÁNICOS ==========
    
    @Override
    public OrdenResponse asignarMecanicos(AsignarMecanicoRequest request) {
        log.info("Asignando mecánicos a orden ID: {}", request.getOrdenId());
        
        Orden orden = ordenRepositorio.findById(request.getOrdenId())
                .orElseThrow(() -> new OrdenNoEncontradaExcepcion(
                        "Orden no encontrada con ID: " + request.getOrdenId()));
        
        // Validar que la orden está en estado adecuado
        if (orden.getEstado() == EstadoOrden.CERRADA || orden.getEstado() == EstadoOrden.CANCELADA) {
            throw new TransicionEstadoInvalidaExcepcion(
                    "No se pueden asignar mecánicos a una orden " + orden.getEstado());
        }
        
        List<OrdenMecanico> ordenMecanicos = new ArrayList<>();
        
        for (Long mecanicoId : request.getMecanicosIds()) {
            Mecanico mecanico = mecanicoRepositorio.findById(mecanicoId)
                    .orElseThrow(() -> new MecanicoNoEncontradoExcepcion(
                            "Mecánico no encontrado con ID: " + mecanicoId));
            
            if (!mecanico.isActivo()) {
                throw new IllegalStateException("El mecánico con ID " + mecanicoId + " no está activo");
            }
            
            if (mecanico.getEstado() != EstadoMecanico.DISPONIBLE) {
                log.warn("Mecánico ID {} no está disponible, estado actual: {}", 
                        mecanicoId, mecanico.getEstado());
                throw new IllegalStateException("El mecánico " + mecanico.getNombre() + 
                        " " + mecanico.getApellido() + " no está disponible");
            }
            
            // Cambiar estado del mecánico a OCUPADO
            mecanico.setEstado(EstadoMecanico.OCUPADO);
            mecanicoRepositorio.save(mecanico);
            
            // Crear OrdenMecanico
            OrdenMecanico om = new OrdenMecanico();
            om.setOrden(orden);
            om.setMecanico(mecanico);
            om.setFechaAsignacion(LocalDate.now());
            om.setEstado(EstadoAsignacionMecanico.ASIGNADO);
            
            ordenMecanicos.add(ordenMecanicoRepositorio.save(om));
        }
        
        // Cambiar estado de la orden a EN_PROCESO
        if (orden.getEstado() == EstadoOrden.ABIERTA) {
            orden.setEstado(EstadoOrden.EN_PROCESO);
            ordenRepositorio.save(orden);
        }
        
        orden.setOrdenesMecanicos(ordenMecanicos);
        
        log.info("Mecánicos asignados exitosamente a orden: {}", orden.getNumeroOrden());
        return ordenMapper.toResponse(orden);
    }

    // ========== 3️⃣ GESTIÓN DE ESTADOS DE MANTENIMIENTO ==========
    
    @Override
    public OrdenMantenimientoResponse actualizarEstadoMantenimiento(
            ActualizarEstadoOrdenMantenimientoRequest request) {
        
        log.info("Actualizando estado de OrdenMantenimiento ID: {} a estado: {}", 
                request.getOrdenMantenimientoId(), request.getNuevoEstado());
        
        OrdenMantenimiento om = ordenMantenimientoRepositorio.findById(request.getOrdenMantenimientoId())
                .orElseThrow(() -> new OrdenMantenimientoNoEncontradaExcepcion(
                        "OrdenMantenimiento no encontrada con ID: " + request.getOrdenMantenimientoId()));
        
        // Validar transición de estado
        validarTransicionEstadoMantenimiento(om.getEstado(), request.getNuevoEstado());
        
        // Actualizar estado
        om.setEstado(request.getNuevoEstado());
        
        // Si se está cotizando, actualizar el costo
        if (request.getNuevoEstado() == EstadoOrdenMantenimiento.COTIZADO && request.getCosto() != null) {
            om.setCosto(request.getCosto());
        }
        
        // Si se inicia el mantenimiento, registrar fecha de inicio
        if (request.getNuevoEstado() == EstadoOrdenMantenimiento.EN_MANTENIMIENTO && om.getFechaInicio() == null) {
            om.setFechaInicio(LocalDate.now());
        }
        
        // Si se completa el mantenimiento, registrar fecha de fin
        if (request.getNuevoEstado() == EstadoOrdenMantenimiento.MANTENIMIENTO_COMPLETADO && om.getFechaFin() == null) {
            om.setFechaFin(LocalDate.now());
        }
        
        OrdenMantenimiento omActualizado = ordenMantenimientoRepositorio.save(om);
        
        // Verificar si todos los mantenimientos están completados para cerrar la orden
        verificarYCerrarOrdenSiCompleta(om.getOrden().getId());
        
        log.info("Estado de OrdenMantenimiento actualizado exitosamente");
        return OrdenMantenimientoMapper.toResponse(omActualizado);
    }
    @Override
    public OrdenResponse iniciarTrabajo(Long ordenId, Long mecanicoId) {
        log.info("Mecánico ID {} iniciando trabajo en orden ID {}", mecanicoId, ordenId);
        
        Orden orden = ordenRepositorio.findById(ordenId)
                .orElseThrow(() -> new OrdenNoEncontradaExcepcion("Orden no encontrada con ID: " + ordenId));
        
        // Buscar la asignación del mecánico
        OrdenMecanico ordenMecanico = ordenMecanicoRepositorio.findByOrdenIdAndEstadoIn(
                ordenId, 
                Arrays.asList(EstadoAsignacionMecanico.ASIGNADO)
        ).stream()
        .filter(om -> om.getMecanico().getId().equals(mecanicoId))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
                "Mecánico no asignado a esta orden o ya está trabajando"));
        
        // Cambiar estado de OrdenMecanico a EN_PROCESO
        ordenMecanico.setEstado(EstadoAsignacionMecanico.EN_PROCESO);
        ordenMecanicoRepositorio.save(ordenMecanico);
        
        // Cambiar OrdenMantenimiento a EN_MANTENIMIENTO (si aún no lo está)
        orden.getOrdenesMantenimiento().stream()
                .filter(om -> om.getEstado() == EstadoOrdenMantenimiento.ACEPTADO)
                .forEach(om -> {
                    om.setEstado(EstadoOrdenMantenimiento.EN_MANTENIMIENTO);
                    if (om.getFechaInicio() == null) {
                        om.setFechaInicio(LocalDate.now());
                    }
                    ordenMantenimientoRepositorio.save(om);
                });
        
        log.info("Trabajo iniciado exitosamente");
        return ordenMapper.toResponse(orden);
    }

    @Override
    public OrdenResponse finalizarTrabajoMecanico(Long ordenId, Long mecanicoId) {
        log.info("Finalizando trabajo de mecánico ID {} en orden ID {}", mecanicoId, ordenId);
        
        Orden orden = ordenRepositorio.findById(ordenId)
                .orElseThrow(() -> new OrdenNoEncontradaExcepcion("Orden no encontrada con ID: " + ordenId));
        
        Mecanico mecanico = mecanicoRepositorio.findById(mecanicoId)
                .orElseThrow(() -> new MecanicoNoEncontradoExcepcion("Mecánico no encontrado con ID: " + mecanicoId));
        
        // Buscar la asignación del mecánico en proceso
        OrdenMecanico ordenMecanico = ordenMecanicoRepositorio.findByOrdenIdAndEstadoIn(
                ordenId,
                Arrays.asList(EstadoAsignacionMecanico.EN_PROCESO, EstadoAsignacionMecanico.ASIGNADO)
        ).stream()
        .filter(om -> om.getMecanico().getId().equals(mecanicoId))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No se encontró asignación activa del mecánico"));
        
        // Cambiar estado de OrdenMecanico a FINALIZADO
        ordenMecanico.setEstado(EstadoAsignacionMecanico.FINALIZADO);
        ordenMecanico.setFechaLiberacion(LocalDate.now());
        ordenMecanicoRepositorio.save(ordenMecanico);
        
        // Cambiar estado del mecánico a DISPONIBLE
        mecanico.setEstado(EstadoMecanico.DISPONIBLE);
        mecanicoRepositorio.save(mecanico);
        
        // Verificar si todos los mecánicos terminaron y cerrar orden si corresponde
        verificarYCerrarOrdenSiCompleta(ordenId);
        
        log.info("Trabajo del mecánico finalizado exitosamente");
        return ordenMapper.toResponse(orden);
    }

    // ========== 4️⃣ CIERRE Y PAGO ==========
    
    @Override
    public PagoResponse registrarPago(PagoRequest request) {
        log.info("Registrando pago para orden ID: {}", request.getOrdenId());
        
        Orden orden = ordenRepositorio.findById(request.getOrdenId())
                .orElseThrow(() -> new OrdenNoEncontradaExcepcion(
                        "Orden no encontrada con ID: " + request.getOrdenId()));
        
        // Validar que la orden esté cerrada
        if (orden.getEstado() != EstadoOrden.CERRADA) {
            throw new TransicionEstadoInvalidaExcepcion(
                    "Solo se puede registrar pago para órdenes CERRADAS. Estado actual: " + orden.getEstado());
        }
        
        // Verificar que no exista ya un pago
        if (pagoRepositorio.existsByOrdenId(request.getOrdenId())) {
            throw new IllegalStateException("Ya existe un pago registrado para esta orden");
        }
        
        // Calcular el total si no está actualizado
        calcularTotalInterno(orden);
        
        // Crear el pago
        Pago pago = new Pago();
        pago.setOrden(orden);
        pago.setMontoTotal(orden.getTotal());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setFechaPago(LocalDate.now());
        pago.setConfirmado(true);
        
        Pago pagoGuardado = pagoRepositorio.save(pago);
        
        // Cambiar estado del vehículo a LISTO_PARA_ENTREGA
        orden.getVehiculo().setEstado(EstadoVehiculo.LISTO_PARA_ENTREGA);
        vehiculoRepositorio.save(orden.getVehiculo());
        
        log.info("Pago registrado exitosamente para orden: {}", orden.getNumeroOrden());
        return pagoMapper.toResponse(pagoGuardado);
    }

    @Override
    public OrdenResponse entregarVehiculo(Long ordenId) {
        log.info("Entregando vehículo de orden ID: {}", ordenId);
        
        Orden orden = ordenRepositorio.findById(ordenId)
                .orElseThrow(() -> new OrdenNoEncontradaExcepcion("Orden no encontrada con ID: " + ordenId));
        
        // Validar que existe un pago confirmado
        Pago pago = pagoRepositorio.findByOrdenId(ordenId)
                .orElseThrow(() -> new PagoNoEncontradoExcepcion(
                        "No se puede entregar el vehículo sin un pago registrado"));
        
        if (!pago.isConfirmado()) {
            throw new IllegalStateException("El pago no está confirmado");
        }
        
        // Validar que el vehículo está LISTO_PARA_ENTREGA
        if (orden.getVehiculo().getEstado() != EstadoVehiculo.LISTO_PARA_ENTREGA) {
            throw new TransicionEstadoInvalidaExcepcion(
                    "El vehículo no está listo para entrega. Estado actual: " + orden.getVehiculo().getEstado());
        }
        
        // Cambiar estado del vehículo a ENTREGADO
        orden.getVehiculo().setEstado(EstadoVehiculo.ENTREGADO);
        vehiculoRepositorio.save(orden.getVehiculo());
        
        log.info("Vehículo entregado exitosamente");
        return ordenMapper.toResponse(orden);
    }

    // ========== 5️⃣ CANCELACIÓN ==========
    
    @Override
    public OrdenResponse cancelarOrden(Long ordenId, String motivo) {
        log.info("Cancelando orden ID: {} - Motivo: {}", ordenId, motivo);
        
        Orden orden = ordenRepositorio.findById(ordenId)
                .orElseThrow(() -> new OrdenNoEncontradaExcepcion("Orden no encontrada con ID: " + ordenId));
        
        // Validar que la orden no está cerrada o ya cancelada
        if (orden.getEstado() == EstadoOrden.CERRADA) {
            throw new TransicionEstadoInvalidaExcepcion("No se puede cancelar una orden ya cerrada");
        }
        
        if (orden.getEstado() == EstadoOrden.CANCELADA) {
            throw new TransicionEstadoInvalidaExcepcion("La orden ya está cancelada");
        }
        
        // Liberar mecánicos asignados
        List<OrdenMecanico> ordenMecanicos = ordenMecanicoRepositorio.findByOrdenId(ordenId);
        for (OrdenMecanico om : ordenMecanicos) {
            if (om.getEstado() != EstadoAsignacionMecanico.FINALIZADO) {
                om.setEstado(EstadoAsignacionMecanico.FINALIZADO);
                om.setFechaLiberacion(LocalDate.now());
                ordenMecanicoRepositorio.save(om);
                
                // Liberar mecánico
                Mecanico mecanico = om.getMecanico();
                if (mecanico.getEstado() == EstadoMecanico.OCUPADO) {
                    mecanico.setEstado(EstadoMecanico.DISPONIBLE);
                    mecanicoRepositorio.save(mecanico);
                }
            }
        }
        
        // Cambiar estado de la orden a CANCELADA
        orden.setEstado(EstadoOrden.CANCELADA);
        ordenRepositorio.save(orden);
        
        // Cambiar estado del vehículo a DISPONIBLE
        orden.getVehiculo().setEstado(EstadoVehiculo.DISPONIBLE);
        vehiculoRepositorio.save(orden.getVehiculo());
        
        log.info("Orden cancelada exitosamente");
        return ordenMapper.toResponse(orden);
    }

    // ========== 6️⃣ CONSULTAS ==========
    
    @Override
    @Transactional(readOnly = true)
    public OrdenResponse obtenerOrdenPorId(Long id) {
        Orden orden = ordenRepositorio.findById(id)
                .orElseThrow(() -> new OrdenNoEncontradaExcepcion("Orden no encontrada con ID: " + id));
        return ordenMapper.toResponse(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenResponse obtenerOrdenPorNumero(String numeroOrden) {
        Orden orden = ordenRepositorio.findByNumeroOrden(numeroOrden)
                .orElseThrow(() -> new OrdenNoEncontradaExcepcion(
                        "Orden no encontrada con número: " + numeroOrden));
        return ordenMapper.toResponse(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenResponse> obtenerOrdenesPorVehiculo(Long vehiculoId) {
        List<Orden> ordenes = ordenRepositorio.findByVehiculoId(vehiculoId);
        return ordenes.stream()
                .map(ordenMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenResponse> obtenerOrdenesPorCliente(Long clienteId) {
        List<Orden> ordenes = ordenRepositorio.findByClienteId(clienteId);
        return ordenes.stream()
                .map(ordenMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrdenResponse> buscarOrdenes(String keyword, int page, int size) {
        Page<Orden> ordenes = ordenRepositorio.searchOrdenes(keyword, PageRequest.of(page, size));
        return ordenes.map(ordenMapper::toResponse);
    }

    @Override
    public OrdenResponse calcularTotal(Long ordenId) {
        Orden orden = ordenRepositorio.findById(ordenId)
                .orElseThrow(() -> new OrdenNoEncontradaExcepcion("Orden no encontrada con ID: " + ordenId));
        
        calcularTotalInterno(orden);
        return ordenMapper.toResponse(orden);
    }
    // ========== MÉTODOS PRIVADOS AUXILIARES ==========
    
    private void validarVehiculoDisponible(Vehiculo vehiculo) {
        if (vehiculo.getEstado() == EstadoVehiculo.EN_MANTENIMIENTO) {
            // Verificar si tiene órdenes abiertas o en proceso
            List<Orden> ordenesActivas = ordenRepositorio.findByVehiculoIdAndEstadoIn(
                    vehiculo.getId(),
                    Arrays.asList(EstadoOrden.ABIERTA, EstadoOrden.EN_PROCESO)
            );
            
            if (!ordenesActivas.isEmpty()) {
                throw new IllegalStateException(
                        "El vehículo ya tiene una orden de mantenimiento activa: " + 
                        ordenesActivas.get(0).getNumeroOrden());
            }
        }
    }
    
    private String generarNumeroOrden() {
        // Genera número único: ORD-YYYYMMDD-UUID
        String fecha = LocalDate.now().toString().replace("-", "");
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD-" + fecha + "-" + uuid;
    }
    
    private void validarTransicionEstadoMantenimiento(
            EstadoOrdenMantenimiento estadoActual, 
            EstadoOrdenMantenimiento nuevoEstado) {
        
        // Definir transiciones válidas
        boolean transicionValida = false;
        
        switch (estadoActual) {
            case EN_REVISION:
                transicionValida = (nuevoEstado == EstadoOrdenMantenimiento.REVISADO);
                break;
            case REVISADO:
                transicionValida = (nuevoEstado == EstadoOrdenMantenimiento.COTIZADO);
                break;
            case COTIZADO:
                transicionValida = (nuevoEstado == EstadoOrdenMantenimiento.ACEPTADO || 
                                   nuevoEstado == EstadoOrdenMantenimiento.RECHAZADO);
                break;
            case ACEPTADO:
                transicionValida = (nuevoEstado == EstadoOrdenMantenimiento.EN_MANTENIMIENTO);
                break;
            case EN_MANTENIMIENTO:
                transicionValida = (nuevoEstado == EstadoOrdenMantenimiento.MANTENIMIENTO_COMPLETADO);
                break;
            case RECHAZADO:
            case MANTENIMIENTO_COMPLETADO:
                transicionValida = false; // Estados finales
                break;
        }
        
        if (!transicionValida) {
            throw new TransicionEstadoInvalidaExcepcion(
                    String.format("Transición inválida de %s a %s", estadoActual, nuevoEstado));
        }
    }
    
    private void verificarYCerrarOrdenSiCompleta(Long ordenId) {
        Orden orden = ordenRepositorio.findById(ordenId).orElse(null);
        if (orden == null) return;
        
        // Verificar si todos los mantenimientos están completados o rechazados
        List<OrdenMantenimiento> mantenimientos = ordenMantenimientoRepositorio.findByOrdenId(ordenId);
        
        boolean todosCompletados = mantenimientos.stream()
                .allMatch(om -> om.getEstado() == EstadoOrdenMantenimiento.MANTENIMIENTO_COMPLETADO ||
                               om.getEstado() == EstadoOrdenMantenimiento.RECHAZADO);
        
        if (todosCompletados && orden.getEstado() != EstadoOrden.CERRADA) {
            // Calcular total
            calcularTotalInterno(orden);
            
            // Cerrar orden
            orden.setEstado(EstadoOrden.CERRADA);
            orden.setFechaCierre(LocalDate.now());
            ordenRepositorio.save(orden);
            
            log.info("Orden {} cerrada automáticamente - Todos los mantenimientos completados", 
                    orden.getNumeroOrden());
        }
    }
    
    private void calcularTotalInterno(Orden orden) {
        BigDecimal total = orden.getOrdenesMantenimiento().stream()
                .filter(om -> om.getEstado() == EstadoOrdenMantenimiento.MANTENIMIENTO_COMPLETADO)
                .map(OrdenMantenimiento::getCosto)
                .filter(costo -> costo != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        orden.setTotal(total);
        ordenRepositorio.save(orden);
    }
}
