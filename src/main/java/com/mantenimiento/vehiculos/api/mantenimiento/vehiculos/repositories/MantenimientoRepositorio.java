package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.TipoMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoRepositorio extends JpaRepository<Mantenimiento, Long> {

    List<Mantenimiento> findByTipo(TipoMantenimiento tipo);
}
