package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mecanico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoMecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MecanicoRepositorio extends JpaRepository<Mecanico, Long> {
    
    List<Mecanico> findByEstadoAndActivoTrue(EstadoMecanico estado);
    
    List<Mecanico> findByEspecialidadContainingIgnoreCaseAndActivoTrue(String especialidad);
    
    @Query("SELECT m FROM Mecanico m WHERE m.activo = true AND m.estado = :estado " +
           "AND (LOWER(m.especialidad) LIKE LOWER(CONCAT('%', :especialidad, '%')) " +
           "OR :especialidad IS NULL OR :especialidad = '')")
    List<Mecanico> findDisponiblesPorEspecialidad(@Param("estado") EstadoMecanico estado, 
                                                   @Param("especialidad") String especialidad);

    Mecanico findByUsuarioId(Long id);
}
