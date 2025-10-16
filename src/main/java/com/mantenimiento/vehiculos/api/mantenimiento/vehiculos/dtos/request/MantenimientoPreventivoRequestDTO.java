package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.TipoMantenimiento;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MantenimientoPreventivoRequestDTO {
    private Long ordenMantenimientoId;

    private TipoMantenimiento tipoMantenimiento;
    private String descripcion;
    private double costo;
    private BigDecimal tiempoEmpleadoHoras;

    private int kilometrajeRecomendado;
    private Date proximoMantenimientoSugerido;
    private boolean esCambioDeComponente;

}
