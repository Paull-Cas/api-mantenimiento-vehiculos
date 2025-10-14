package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoOrdenMantenimiento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(unique = true)
    private String numeroOrden;

    @ManyToOne
    private Vehiculo vehiculo;

    @ManyToOne
    private Mantenimiento mantenimiento;

    private LocalDate fechaIngreso;

    private LocalDate fechaEntregaEstimada;

    private String descripcionProblema;

    @Enumerated(EnumType.STRING)
    private EstadoOrdenMantenimiento estado;

    private BigDecimal costoTotal;

    private String observaciones;

    private LocalDate fechaUltimaActualizacion;

    @PrePersist
    public void crear(){
        this.fechaIngreso = LocalDate.now();
        this.estado = EstadoOrdenMantenimiento.PENDIENTE;
    }

    @PreUpdate
    public void actualizar() {
        this.fechaUltimaActualizacion = LocalDate.now();
    }
}
