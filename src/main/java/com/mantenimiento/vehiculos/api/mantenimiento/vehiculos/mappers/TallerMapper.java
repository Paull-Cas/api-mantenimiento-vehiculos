package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.TallerRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.TallerResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Taller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class TallerMapper {

    @Autowired
    private MantenimientoMapper mantenimientoMapper;

    // AÑADIDO: Mapea de Request a Entity
    public Taller toEntity(TallerRequest request) {
        return Taller.builder()
                .nombre(request.getNombre())
                .ciudad(request.getCiudad())
                .distrito(request.getDistrito())
                .direccion(request.getDireccion())
                .horario(request.getHorario())
                .mantenimientos(new ArrayList<>())
                .build();
    }

    public TallerResponseDTO toResponse(Taller taller) {
        return TallerResponseDTO.builder()
                .id(taller.getId())
                .nombre(taller.getNombre())
                .ciudad(taller.getCiudad())
                .distrito(taller.getDistrito())
                .direccion(taller.getDireccion())
                .horario(taller.getHorario())
                .mantenimientos(taller.getMantenimientos() != null ?
                        taller.getMantenimientos().stream()
                                .map(mantenimientoMapper::mapearDeResponseDTO)
                                .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }
}