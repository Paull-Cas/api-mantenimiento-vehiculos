package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Collection<Rol> findRoleEntitiesByRoleEnumIn(List<String> rolesRequest);
}
