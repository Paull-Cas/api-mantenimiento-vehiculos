package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.OrdenMantenimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenMantenimientoRepositorio extends JpaRepository<OrdenMantenimiento, Long> {

    @Query(value="""
        SELECT * FROM orden_mantenimiento
            WHERE LOWER(numero_orden) LIKE LOWER(CONCAT('%', :kw, '%'))
    """, nativeQuery = true)
    Page<OrdenMantenimiento> searchOrdenMantenimiento(@Param("kw") String keyword, Pageable pageable);
}
