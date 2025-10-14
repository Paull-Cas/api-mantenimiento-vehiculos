package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.DTOs.response.VehiculoDto;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.VehiculoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoControlador {

    @Autowired
    private VehiculoService VehiculoService;

    @GetMapping("/Vehiculos")
    public List<VehiculoDto> listarVehiculos() {
        return VehiculoService.listVehiculos();
    }

    @GetMapping("/Vehiculo/{id}")
    public VehiculoDto obtenerVehiculoPorId(@PathVariable(name = "id") Long id) throws VehiculoNoEncontradoExcepcion {
        return VehiculoService.getVehiculo(id);
    }

    @PostMapping("/Vehiculo")
    public VehiculoDto guardarVehiculo(@RequestBody VehiculoDto vehiculoDto) {
        return VehiculoService.saveVehiculo(vehiculoDto);
    }

    @PutMapping("/Vehiculo/{id}")
    public VehiculoDto actualizarVehiculo(@PathVariable(name = "id") Long id, @RequestBody VehiculoDto vehiculoDto) throws VehiculoNoEncontradoExcepcion {
        vehiculoDto.setId(id);
        return VehiculoService.updateVehiculo(vehiculoDto);
    }

    @DeleteMapping("/Vehiculo/{id}")
    public void eliminarVehiculo(@PathVariable(name = "id") Long id) throws VehiculoNoEncontradoExcepcion {
        VehiculoService.deleteVehiculo(id);
    }

    @GetMapping("/Vehiculos/search")
    public List<VehiculoDto> buscarVehiculos(@RequestParam(name = "keyword", defaultValue = "") String keyword){
        return VehiculoService.searchVehiculos("%" + keyword + "%");
    }


}
