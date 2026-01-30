package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepositorio extends JpaRepository<Pago, Long> {
    
    Optional<Pago> findByOrdenId(Long ordenId);
    
    boolean existsByOrdenId(Long ordenId);
}
