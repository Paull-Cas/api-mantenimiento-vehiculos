package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@DiscriminatorValue("juridico")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteJuridico extends Cliente{
    private String razonSocial;
    private String ruc;
    private String representanteLegal;
}
