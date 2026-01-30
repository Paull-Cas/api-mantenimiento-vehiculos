package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Orden;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoOrden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepositorio extends JpaRepository<Orden, Long> {
    
    Optional<Orden> findByNumeroOrden(String numeroOrden);
    
    List<Orden> findByVehiculoId(Long vehiculoId);
    
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.id = :vehiculoId AND o.estado IN :estados")
    List<Orden> findByVehiculoIdAndEstadoIn(@Param("vehiculoId") Long vehiculoId, 
                                            @Param("estados") List<EstadoOrden> estados);
    
    @Query("SELECT o FROM Orden o WHERE o.numeroOrden LIKE %:keyword% OR o.vehiculo.placa LIKE %:keyword%")
    Page<Orden> searchOrdenes(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.cliente.id = :clienteId")
    List<Orden> findByClienteId(@Param("clienteId") Long clienteId);
}
