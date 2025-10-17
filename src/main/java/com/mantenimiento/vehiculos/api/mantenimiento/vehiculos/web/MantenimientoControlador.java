package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoCorrectivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoPreventivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.MantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/mantenimiento-vehiculos")
public class MantenimientoControlador {

    @Autowired
    private MantenimientoService mantenimientoService;


    @GetMapping("/{idMantenimiento}")
    public MantenimientoResponseDTO getMantenimientoPorId(@PathVariable Long idMantenimiento) throws MantenimientoNoEncontradoExcepcion {
        return mantenimientoService.getMantenimientoPorId(idMantenimiento);
    }
    @GetMapping
    public List<MantenimientoResponseDTO> getMantenimientos() {
        return mantenimientoService.getTodosLosMantenimientos();
    }
    @PostMapping("/preventivo")
    public MantenimientoResponseDTO crearMantenimientoPreventivo(@RequestBody MantenimientoPreventivoRequestDTO mantPrevDTO){
        return mantenimientoService.saveMantenimientoPreventivo(mantPrevDTO);
    }
    @PostMapping("/correctivo")
    public MantenimientoResponseDTO crearMantenimientoCorrectivo(@RequestBody MantenimientoCorrectivoRequestDTO mantCorrecDTO){
        return mantenimientoService.saveMantenimientoCorrectivo(mantCorrecDTO);
    }
    @PutMapping("/correctivo/{idMantenimiento}")
    public MantenimientoResponseDTO actualizarMantenimientoCorrectivo(@PathVariable Long idMantenimiento, @RequestBody MantenimientoCorrectivoRequestDTO mantCorrecDTO) throws MantenimientoNoEncontradoExcepcion{
        return mantenimientoService.updateMantenimientoCorrectivo(idMantenimiento,mantCorrecDTO);
    }
    @PutMapping("/preventivo/{idMantenimiento}")
    public MantenimientoResponseDTO actualizarMantenimientoPreventivo(@PathVariable Long idMantenimiento, @RequestBody MantenimientoPreventivoRequestDTO mantPrevenDTO) throws MantenimientoNoEncontradoExcepcion{
        return mantenimientoService.updateMantenimientoPreventivo(idMantenimiento,mantPrevenDTO);
    }

    @DeleteMapping("/{idMantenimiento}")
    public void eliminarMantenimiento(@PathVariable Long idMantenimiento) throws MantenimientoNoEncontradoExcepcion {
        mantenimientoService.deleteMantenimiento(idMantenimiento);
    }
}
