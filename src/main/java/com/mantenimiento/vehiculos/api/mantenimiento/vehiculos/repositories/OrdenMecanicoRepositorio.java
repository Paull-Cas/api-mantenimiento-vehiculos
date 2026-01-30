package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.OrdenMecanico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoAsignacionMecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenMecanicoRepositorio extends JpaRepository<OrdenMecanico, Long> {
    
    List<OrdenMecanico> findByOrdenId(Long ordenId);

    @Query("SELECT om FROM OrdenMecanico om WHERE om.orden.id = :ordenId AND om.estado IN :estados")
    List<OrdenMecanico> findByOrdenIdAndEstadoIn(@Param("ordenId") Long ordenId, 
                                                  @Param("estados") List<EstadoAsignacionMecanico> estados);
}
