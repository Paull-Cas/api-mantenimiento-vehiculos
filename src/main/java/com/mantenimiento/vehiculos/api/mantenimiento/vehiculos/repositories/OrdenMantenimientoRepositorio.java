package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.OrdenMantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoOrdenMantenimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenMantenimientoRepositorio extends JpaRepository<OrdenMantenimiento, Long> {

    List<OrdenMantenimiento> findByOrdenId(Long ordenId);
    
    List<OrdenMantenimiento> findByOrdenIdAndEstado(Long ordenId, EstadoOrdenMantenimiento estado);
    
    @Query("SELECT om FROM OrdenMantenimiento om WHERE om.orden.id = :ordenId AND om.estado IN :estados")
    List<OrdenMantenimiento> findByOrdenIdAndEstadoIn(@Param("ordenId") Long ordenId, 
                                                       @Param("estados") List<EstadoOrdenMantenimiento> estados);
    
    @Query("SELECT om FROM OrdenMantenimiento om WHERE om.orden.numeroOrden LIKE %:keyword% " +
           "OR om.mantenimiento.nombre LIKE %:keyword%")
    Page<OrdenMantenimiento> searchOrdenMantenimiento(@Param("keyword") String keyword, Pageable pageable);
}
