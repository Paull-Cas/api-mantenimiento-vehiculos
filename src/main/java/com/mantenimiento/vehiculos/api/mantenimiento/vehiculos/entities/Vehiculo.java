package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String placa;

    private String marca;
    private String modelo;
    private int año;
    private String color;
    private String tipoCombustible;

    @Column(unique = true)
    private String numeroSerie;

    private int kilometraje;

    @ManyToOne
    private Cliente cliente;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenMantenimiento> mantenimientos;
}
