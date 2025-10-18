package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoCorrectivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoPreventivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponsePageDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.OrdenMantenimientoNoEncontradaExcepcion;

import java.util.List;

public interface MantenimientoService {

    MantenimientoResponseDTO saveMantenimientoPreventivo(MantenimientoPreventivoRequestDTO mantPrevenRequestDTO);
    MantenimientoResponseDTO saveMantenimientoCorrectivo(MantenimientoCorrectivoRequestDTO mantCorrecRequestDTO);
    List<MantenimientoResponseDTO> getTodosLosMantenimientos();
    MantenimientoResponseDTO getMantenimientoPorId(Long id) throws MantenimientoNoEncontradoExcepcion;
    MantenimientoResponseDTO updateMantenimientoPreventivo(Long idMantenimiento, MantenimientoPreventivoRequestDTO mantPrevenRequestDTO) throws MantenimientoNoEncontradoExcepcion;
    MantenimientoResponseDTO updateMantenimientoCorrectivo(Long idMantenimiento, MantenimientoCorrectivoRequestDTO mantCorrecRequestDTO) throws MantenimientoNoEncontradoExcepcion;
    void deleteMantenimiento(Long id) throws MantenimientoNoEncontradoExcepcion;
    List<MantenimientoResponseDTO> buscarMantenimientosPorTipo(String tipo);
    MantenimientoResponsePageDTO getMantenimientosPaginado(int page, int size);
}
