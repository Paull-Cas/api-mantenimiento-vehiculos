package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MecanicoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MecanicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mecanico;
import org.springframework.stereotype.Component;

@Component
public class MecanicoMapper {

    public Mecanico toEntity(MecanicoRequest request) {
        return Mecanico.builder()
                .usuarioId(request.getUsuarioId())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .especialidad(request.getEspecialidad())
                .telefono(request.getTelefono())
                .fechaIngreso(request.getFechaIngreso())
                .build();
    }

    public MecanicoResponse toResponse(Mecanico mecanico) {
        return MecanicoResponse.builder()
                .id(mecanico.getId())
                .usuarioId(mecanico.getUsuarioId())
                .nombre(mecanico.getNombre())
                .apellido(mecanico.getApellido())
                .nombreCompleto(mecanico.getNombre() + " " + mecanico.getApellido())
                .especialidad(mecanico.getEspecialidad())
                .telefono(mecanico.getTelefono())
                .estado(mecanico.getEstado())
                .activo(mecanico.isActivo())
                .fechaIngreso(mecanico.getFechaIngreso())
                .tallerId(mecanico.getTaller() != null ? mecanico.getTaller().getId() : null)
                .build();
    }
}