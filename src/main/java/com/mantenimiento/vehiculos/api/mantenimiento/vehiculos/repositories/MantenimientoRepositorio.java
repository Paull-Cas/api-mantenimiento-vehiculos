package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoRepositorio extends JpaRepository<Mantenimiento, Long> {
    @Query("SELECT m FROM Mantenimiento m WHERE m.class = :tipo")
    List<Mantenimiento> buscarPorTipo(@Param("tipo") Class<? extends Mantenimiento> tipo);

}
