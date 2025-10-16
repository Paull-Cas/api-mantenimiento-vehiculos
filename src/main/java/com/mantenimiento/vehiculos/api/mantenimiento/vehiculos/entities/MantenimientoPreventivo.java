package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("PREVENTIVO")
public class MantenimientoPreventivo extends Mantenimiento {
    private int kilometrajeRecomendado;

    private Date proximoMantenimientoSugerido;

    private boolean esCambioDeComponente;
}
