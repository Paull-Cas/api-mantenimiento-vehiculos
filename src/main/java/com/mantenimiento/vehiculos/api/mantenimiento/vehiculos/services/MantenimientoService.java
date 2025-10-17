package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoCorrectivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoPreventivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.OrdenMantenimientoNoEncontradaExcepcion;

import java.util.List;

public interface MantenimientoService {

    List<MantenimientoResponseDTO> obtenerTodosLosMantenimientos();
    MantenimientoResponseDTO obtenerMantenimientoPorId(Long id) throws MantenimientoNoEncontradoExcepcion;
    MantenimientoResponseDTO crearMantenimientoPreventivo(MantenimientoPreventivoRequestDTO mantPrevenRequestDTO);
    MantenimientoResponseDTO crearMantenimientoCorrectivo(MantenimientoCorrectivoRequestDTO mantCorrecRequestDTO);
    MantenimientoResponseDTO actualizarMantenimientoPreventivo(Long idMantenimiento, MantenimientoPreventivoRequestDTO mantPrevenRequestDTO) throws MantenimientoNoEncontradoExcepcion;
    MantenimientoResponseDTO actualizarMantenimientoCorrectivo(Long idMantenimiento, MantenimientoCorrectivoRequestDTO mantCorrecRequestDTO) throws MantenimientoNoEncontradoExcepcion;
    void eliminarMantenimiento(Long id) throws MantenimientoNoEncontradoExcepcion;
}
