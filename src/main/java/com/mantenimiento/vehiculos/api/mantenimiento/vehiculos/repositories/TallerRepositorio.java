package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Taller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TallerRepositorio extends JpaRepository<Taller, Long> {

    List<Taller> findByCiudadIgnoreCase(String ciudad);

    List<Taller> findByDistritoIgnoreCase(String distrito);

    @Query("SELECT t FROM Taller t WHERE " +
            "LOWER(t.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.ciudad) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.distrito) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.direccion) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Taller> buscarPorKeyword(@Param("keyword") String keyword);
}