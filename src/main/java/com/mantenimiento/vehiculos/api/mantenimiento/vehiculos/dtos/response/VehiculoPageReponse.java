package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class VehiculoPageReponse {
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private long totalElements;
    private List<VehiculoResponse> vehiculos;
}
