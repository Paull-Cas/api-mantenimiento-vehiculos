package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.VehiculoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.VehiculoPageReponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.VehiculoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.VehiculoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoControlador {

    @Autowired
    private VehiculoService vehiculoService;

    // Roles con acceso: ADMIN_VEHICULOS, INVITADO
    @PreAuthorize("hasAuthority('READ_VEHICULO')")
    @GetMapping("/Vehiculos")
    public List<VehiculoResponse> listarVehiculos() {
        return vehiculoService.listVehiculos();
    }

    // Roles con acceso: ADMIN_VEHICULOS, INVITADO
    @PreAuthorize("hasAuthority('READ_VEHICULO')")
    @GetMapping("/Vehiculo/{id}")
    public VehiculoResponse obtenerVehiculoPorId(@PathVariable(name = "id") Long id) throws VehiculoNoEncontradoExcepcion {
        return vehiculoService.getVehiculo(id);
    }

    // Roles con acceso: ADMIN_VEHICULOS
    @PreAuthorize("hasAuthority('CREATE_VEHICULO')")
    @PostMapping("/Vehiculo")
    public VehiculoResponse guardarVehiculo(@RequestBody VehiculoRequest vehiculoDto) {
        return vehiculoService.saveVehiculo(vehiculoDto);
    }

    // Roles con acceso: ADMIN_VEHICULOS
    @PreAuthorize("hasAuthority('UPDATE_VEHICULO')")
    @PutMapping("/Vehiculo/{id}")
    public VehiculoResponse actualizarVehiculo(@PathVariable(name = "id") Long id, @RequestBody VehiculoRequest vehiculoDto) throws VehiculoNoEncontradoExcepcion {
        return vehiculoService.updateVehiculo(id, vehiculoDto);
    }

    // Roles con acceso: ADMIN_VEHICULOS
    @PreAuthorize("hasAuthority('DELETE_VEHICULO')")
    @DeleteMapping("/Vehiculo/{id}")
    public void eliminarVehiculo(@PathVariable(name = "id") Long id) throws VehiculoNoEncontradoExcepcion {
        vehiculoService.deleteVehiculo(id);
    }

    // Roles con acceso: ADMIN_VEHICULOS, INVITADO
    // Filtrado por placa
    @PreAuthorize("hasAuthority('READ_VEHICULO')")
    @GetMapping("/Vehiculos/search")
    public List<VehiculoResponse> buscarVehiculos(@RequestParam(name = "keyword", defaultValue = "") String keyword){
        return vehiculoService.searchVehiculos("%" + keyword + "%");
    }

    // Roles con acceso: ADMIN_VEHICULOS, INVITADO
    @PreAuthorize("hasAuthority('READ_VEHICULO')")
    @GetMapping("/por-cliente/{clienteId}")
    public List<VehiculoResponse> listarVehiculosPorCliente(@PathVariable Long clienteId) {
        return vehiculoService.listByCliente(clienteId);
    }

    // Roles con acceso: ADMIN_VEHICULOS, INVITADO
    @PreAuthorize("hasAuthority('READ_VEHICULO')")
    @GetMapping("/paginacion")
    public ResponseEntity<VehiculoPageReponse> getVehiculo(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        VehiculoPageReponse paginaDecehiculos = vehiculoService.getVehiculoPageReponse(page, size);
        return ResponseEntity.ok(paginaDecehiculos);
    }
}