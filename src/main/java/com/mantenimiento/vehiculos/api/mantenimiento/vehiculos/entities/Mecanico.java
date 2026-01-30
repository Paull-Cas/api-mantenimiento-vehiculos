package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoMecanico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mecanico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", unique = true, nullable = false)
    private Long usuarioId;

    private String nombre;
    private String apellido;
    private String especialidad;
    private String telefono;

    @Enumerated(EnumType.STRING)
    private EstadoMecanico estado = EstadoMecanico.DISPONIBLE;

    private boolean activo = true;

    private LocalDate fechaIngreso;

    @OneToMany(mappedBy = "mecanico", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenMecanico> ordenesMecanicos;
}

