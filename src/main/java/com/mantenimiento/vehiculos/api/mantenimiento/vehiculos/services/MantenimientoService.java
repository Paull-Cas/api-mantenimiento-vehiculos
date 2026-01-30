package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponsePageDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.TipoMantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;

import java.util.List;

public interface MantenimientoService {
    MantenimientoResponseDTO saveMantenimiento(MantenimientoRequest mantenimientoRequest);
    List<MantenimientoResponseDTO> getTodosLosMantenimientos();
    MantenimientoResponseDTO getMantenimientoPorId(Long id) throws MantenimientoNoEncontradoExcepcion;
    MantenimientoResponseDTO updateMantenimiento(Long idMantenimiento, MantenimientoRequest mantenimientoRequest) throws MantenimientoNoEncontradoExcepcion;
    void deleteMantenimiento(Long id) throws MantenimientoNoEncontradoExcepcion;
    List<MantenimientoResponseDTO> buscarMantenimientosPorTipo(TipoMantenimiento tipo);
    MantenimientoResponsePageDTO getMantenimientosPaginado(int page, int size);
}