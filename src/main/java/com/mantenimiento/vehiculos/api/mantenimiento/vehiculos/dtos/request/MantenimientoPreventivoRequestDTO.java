package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.TipoMantenimiento;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MantenimientoPreventivoRequestDTO {
    private TipoMantenimiento tipoMantenimiento;
    private String descripcion;
    private BigDecimal tiempoEmpleadoHoras;
    private int kilometrajeRecomendado;
    private boolean esCambioDeComponente;

}
