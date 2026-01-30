package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

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
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Orden orden;

    @ManyToOne
    private Cliente cliente;

    private BigDecimal montoTotal;
    private String metodoPago;
    private LocalDate fechaPago;
    private boolean confirmado;
}
