package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoOrdenMantenimiento;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenMantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Orden orden;

    @ManyToOne
    private Mantenimiento mantenimiento;

    private String descripcionProblema;

    @Enumerated(EnumType.STRING)
    private EstadoOrdenMantenimiento estado;

    private BigDecimal costo;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @PrePersist
    public void crear() {
        this.estado = EstadoOrdenMantenimiento.EN_REVISION;
        this.fechaInicio = LocalDate.now();
    }
}
