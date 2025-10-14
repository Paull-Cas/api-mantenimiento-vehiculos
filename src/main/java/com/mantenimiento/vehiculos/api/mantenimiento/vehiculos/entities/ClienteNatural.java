package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@DiscriminatorValue("natural")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ClienteNatural extends Cliente{
    private String nombre;
    private String apellido;
    private String dni;
}
