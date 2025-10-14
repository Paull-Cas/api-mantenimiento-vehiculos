package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@DiscriminatorValue("natural")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ClienteNatural extends Cliente{
    private String nombre;
    private String apellido;
    private String dni;
}
