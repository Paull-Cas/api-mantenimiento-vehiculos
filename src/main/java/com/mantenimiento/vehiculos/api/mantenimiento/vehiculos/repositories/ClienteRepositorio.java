package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente,Long> {

    @Query(value = """
        SELECT * FROM cliente
            WHERE tipo = :tipo 
                AND (
                    CASE 
                        WHEN :tipo = 'juridico' THEN LOWER(razon_social) LIKE LOWER(CONCAT('%', :kw, '%'))
                        WHEN :tipo = 'natural' THEN LOWER(nombre) LIKE LOWER(CONCAT('%', :kw, '%'))
                        ELSE 1=1
                    END
                    )
    """, nativeQuery = true)
    Page<Cliente> searchCliente(@Param("kw") String keyword, @Param("tipo") String tipo, Pageable pageable);
    }
