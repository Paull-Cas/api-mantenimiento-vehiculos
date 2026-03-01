package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mecanico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoMecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MecanicoRepositorio extends JpaRepository<Mecanico, Long> {

    List<Mecanico> findByEstadoAndActivoTrue(EstadoMecanico estado);

    List<Mecanico> findByEspecialidadContainingIgnoreCaseAndActivoTrue(String especialidad);

    Mecanico findByUsuarioId(Long id);
}