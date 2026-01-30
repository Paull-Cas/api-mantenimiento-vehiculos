package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MecanicoAsignadoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.OrdenMecanico;

public class OrdenMecanicoMapper {

    public static MecanicoAsignadoResponse toMecanicoAsignadoResponse(OrdenMecanico om) {
        return MecanicoAsignadoResponse.builder()
                .id(om.getId())
                .mecanicoId(om.getMecanico() != null ? om.getMecanico().getId() : null)
                .nombreMecanico(om.getMecanico() != null 
                    ? om.getMecanico().getNombre() + " " + om.getMecanico().getApellido() 
                    : null)
                .especialidadMecanico(om.getMecanico() != null ? om.getMecanico().getEspecialidad() : null)
                .fechaAsignacion(om.getFechaAsignacion())
                .fechaLiberacion(om.getFechaLiberacion())
                .estado(om.getEstado())
                .build();
    }
}
