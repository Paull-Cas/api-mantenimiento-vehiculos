package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoAsignacionMecanico;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenMecanico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Orden orden;

    @ManyToOne
    private Mecanico mecanico;

    private LocalDate fechaAsignacion;
    private LocalDate fechaLiberacion;

    @Enumerated(EnumType.STRING)
    private EstadoAsignacionMecanico estado;

    @PrePersist
    public void crear() {
        this.fechaAsignacion = LocalDate.now();
    }
}
