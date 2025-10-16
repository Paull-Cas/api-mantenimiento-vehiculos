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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_SERVICIO", length = 12)
public class Mantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoMantenimiento tipoMantenimiento;

    private String descripcion;

    private BigDecimal tiempoEmpleadoHoras;

    @OneToMany(mappedBy = "mantenimiento", fetch = FetchType.LAZY)
    private List<OrdenMantenimiento> ordenesMantenimiento;
}
