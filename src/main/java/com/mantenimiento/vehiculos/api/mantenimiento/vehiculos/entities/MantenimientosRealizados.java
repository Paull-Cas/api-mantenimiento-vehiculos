package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import jakarta.persistence.*;

import java.util.Date;

public class MantenimientosRealizados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date fechaMantenimiento;
    private Double monto;

    @ManyToOne
    private Vehiculo vehiculo;

    @ManyToOne
    private Mantenimiento mantenimiento;


}
