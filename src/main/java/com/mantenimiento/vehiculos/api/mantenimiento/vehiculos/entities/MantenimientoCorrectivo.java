package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("CORRECTIVO")
public class MantenimientoCorrectivo extends Mantenimiento{
    private String diagnosticoInicial;

    private String causaDeFallaDetectada;

    private boolean esReparacionUrgente;
}
