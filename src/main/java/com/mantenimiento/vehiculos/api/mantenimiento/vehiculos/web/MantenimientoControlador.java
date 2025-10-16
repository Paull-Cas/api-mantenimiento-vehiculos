package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ActualizarEstadoMantenimientoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoCorrectivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoPreventivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.MantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/mantenimiento-vehiculos")
public class MantenimientoControlador {

    @Autowired
    private MantenimientoService mantenimientoService;

    @PostMapping("/mantenimiento-preventivo")
    public MantenimientoResponseDTO crearMantenimientoPreventivo(@RequestBody MantenimientoPreventivoRequestDTO requestDTO){
        return mantenimientoService.saveMantenimientoPreventivo(requestDTO);
    }
    @PostMapping("/mantenimiento-correctivo")
    public MantenimientoResponseDTO crearMantenimientoCorrectivo(@RequestBody MantenimientoCorrectivoRequestDTO requestDTO){
        return mantenimientoService.saveMantenimientoCorrectivo(requestDTO);
    }

    @GetMapping("/{idMantenimiento}")
    public MantenimientoResponseDTO getMantenimientoPorId(@PathVariable Long idMantenimiento) throws MantenimientoNoEncontradoExcepcion {
        return mantenimientoService.getMantenimientoById(idMantenimiento);
    }
    @GetMapping("/por-orden/{idOrden}")
    public List<MantenimientoResponseDTO> listarMantenimientoPorOrden(@PathVariable Long ordenId){
        return mantenimientoService.getMantenimientosByOrdenId(ordenId);
    }

    @PatchMapping("/{idMantenimiento}/estado")
    public MantenimientoResponseDTO actualizarEstadoMantenimiento(@PathVariable  Long idMantenimiento, @RequestBody ActualizarEstadoMantenimientoRequestDTO requestDTO) throws MantenimientoNoEncontradoExcepcion {
        return mantenimientoService.updateEstadoMantenimiento(idMantenimiento, requestDTO);
    }

    @DeleteMapping("/{idMantenimiento}")
    public void eliminarMantenimiento(@PathVariable Long idMantenimiento) throws MantenimientoNoEncontradoExcepcion {
        mantenimientoService.deleteMantenimiento(idMantenimiento);
    }
}
