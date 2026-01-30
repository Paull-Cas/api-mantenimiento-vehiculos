package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.TipoMantenimiento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private TipoMantenimiento tipo;

    private BigDecimal precioMinimo;

    private boolean activo = true;

    @OneToMany(mappedBy = "mantenimiento", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenMantenimiento> ordenesMantenimiento;
}
