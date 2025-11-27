package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String username,
                               @NotBlank String password) {
}
