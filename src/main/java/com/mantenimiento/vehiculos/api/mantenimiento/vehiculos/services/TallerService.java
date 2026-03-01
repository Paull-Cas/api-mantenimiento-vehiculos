package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.TallerRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.TallerResponseDTO;

import java.util.List;

public interface TallerService {

    TallerResponseDTO crearTaller(TallerRequest request);

    TallerResponseDTO obtenerTallerPorId(Long id);

    List<TallerResponseDTO> obtenerTodos();

    List<TallerResponseDTO> buscarPorCiudad(String ciudad);

    List<TallerResponseDTO> buscarPorDistrito(String distrito);

    List<TallerResponseDTO> buscarPorKeyword(String keyword);

    TallerResponseDTO actualizarTaller(Long id, TallerRequest request);

    void eliminarTaller(Long id);
}