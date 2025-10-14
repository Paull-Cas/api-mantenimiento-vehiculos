package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepositorio extends JpaRepository<Vehiculo, Long> {
    @Query("SELECT c FROM Vehiculo c WHERE c.placa LIKE :kw")
    List<Vehiculo> searchVehiculos(@Param(value = "kw") String keyword);

}
