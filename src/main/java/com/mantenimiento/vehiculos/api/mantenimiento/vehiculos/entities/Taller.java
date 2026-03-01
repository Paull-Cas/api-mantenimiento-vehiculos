package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Taller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String ciudad;
    private String distrito;
    private String direccion;
    private String horario;

    @OneToMany(mappedBy = "taller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mecanico> mecanicos;

    @ManyToMany
    @JoinTable(
            name = "taller_mantenimiento",
            joinColumns = @JoinColumn(name = "taller_id"),
            inverseJoinColumns = @JoinColumn(name = "mantenimiento_id")
    )
    private List<Mantenimiento> mantenimientos;
}