package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoCorrectivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoPreventivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.MantenimientoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MantenimientoServiceImpl implements MantenimientoService {


    @Override
    public List<MantenimientoResponseDTO> obtenerTodosLosMantenimientos() {
        return List.of();
    }

    @Override
    public MantenimientoResponseDTO obtenerMantenimientoPorId(Long id) throws MantenimientoNoEncontradoExcepcion {
        return null;
    }

    @Override
    public MantenimientoResponseDTO crearMantenimientoPreventivo(MantenimientoPreventivoRequestDTO mantPrevenRequestDTO) {
        return null;
    }

    @Override
    public MantenimientoResponseDTO crearMantenimientoCorrectivo(MantenimientoCorrectivoRequestDTO mantCorrecRequestDTO) {
        return null;
    }

    @Override
    public MantenimientoResponseDTO actualizarMantenimientoPreventivo(Long idMantenimiento, MantenimientoPreventivoRequestDTO mantPrevenRequestDTO) throws MantenimientoNoEncontradoExcepcion {
        return null;
    }

    @Override
    public MantenimientoResponseDTO actualizarMantenimientoCorrectivo(Long idMantenimiento, MantenimientoCorrectivoRequestDTO mantCorrecRequestDTO) throws MantenimientoNoEncontradoExcepcion {
        return null;
    }

    @Override
    public void eliminarMantenimiento(Long id) throws MantenimientoNoEncontradoExcepcion {

    }
}
