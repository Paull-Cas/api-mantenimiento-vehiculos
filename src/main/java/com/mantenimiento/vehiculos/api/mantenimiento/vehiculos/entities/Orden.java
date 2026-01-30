package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoOrden;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String numeroOrden;

    @ManyToOne
    private Vehiculo vehiculo;

    private LocalDate fechaIngreso;
    private LocalDate fechaCierre;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    private BigDecimal total;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenMantenimiento> ordenesMantenimiento;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenMecanico> ordenesMecanicos;

    @OneToOne(mappedBy = "orden", cascade = CascadeType.ALL)
    private Pago pago;

    @PrePersist
    public void crear() {
        this.fechaIngreso = LocalDate.now();
        this.estado = EstadoOrden.ABIERTA;
    }
}
