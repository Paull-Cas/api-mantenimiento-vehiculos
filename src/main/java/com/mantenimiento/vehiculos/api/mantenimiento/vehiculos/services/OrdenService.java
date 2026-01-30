package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.*;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.VehiculoNoEncontradoExcepcion;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrdenService {

    OrdenResponse crearOrden(OrdenRequest request) throws MantenimientoNoEncontradoExcepcion, VehiculoNoEncontradoExcepcion;

    OrdenResponse asignarMecanicos(AsignarMecanicoRequest request);

    OrdenMantenimientoResponse actualizarEstadoMantenimiento(ActualizarEstadoOrdenMantenimientoRequest request);

    OrdenResponse iniciarTrabajo(Long ordenId, Long mecanicoId);

    OrdenResponse finalizarTrabajoMecanico(Long ordenId, Long mecanicoId);

    PagoResponse registrarPago(PagoRequest request);

    OrdenResponse entregarVehiculo(Long ordenId);

    OrdenResponse cancelarOrden(Long ordenId);

    OrdenResponse obtenerOrdenPorId(Long id);
    List<OrdenResponse> obtenerOrdenesPorVehiculo(Long vehiculoId);
    List<OrdenResponse> obtenerOrdenesPorCliente(Long clienteId);
    Page<OrdenResponse> buscarOrdenes(String keyword, int page, int size);

    OrdenResponse calcularTotal(Long ordenId);


    List<OrdenResponse> obtenerOrdenesDelClienteAutenticado();
    List<OrdenResponse> obtenerOrdenesDelMecanicoAutenticado();


}
