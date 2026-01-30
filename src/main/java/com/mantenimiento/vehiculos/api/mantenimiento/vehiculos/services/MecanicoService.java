package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MecanicoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MecanicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoMecanico;

import java.util.List;

public interface MecanicoService {
    
    MecanicoResponse crearMecanico(MecanicoRequest request);
    
    MecanicoResponse actualizarMecanico(Long id, MecanicoRequest request);
    
    MecanicoResponse obtenerMecanicoPorId(Long id);
    
    List<MecanicoResponse> obtenerTodosMecanicos();
    
    List<MecanicoResponse> obtenerMecanicosDisponibles();
    
    List<MecanicoResponse> buscarPorEspecialidad(String especialidad);
    
    void desactivarMecanico(Long id);
    
    void activarMecanico(Long id);
    
    void cambiarEstado(Long id, EstadoMecanico nuevoEstado);
}
