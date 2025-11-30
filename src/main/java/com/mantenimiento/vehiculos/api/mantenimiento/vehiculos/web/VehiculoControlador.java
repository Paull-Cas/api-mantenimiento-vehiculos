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

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/Vehiculos")
    public List<VehiculoResponse> listarVehiculos() {
        return vehiculoService.listVehiculos();
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/Vehiculo/{id}")
    public VehiculoResponse obtenerVehiculoPorId(@PathVariable(name = "id") Long id) throws VehiculoNoEncontradoExcepcion {
        return vehiculoService.getVehiculo(id);
    }

    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping("/Vehiculo")
    public VehiculoResponse guardarVehiculo(@RequestBody VehiculoRequest vehiculoDto) {
        return vehiculoService.saveVehiculo(vehiculoDto);
    }
    @PreAuthorize("hasAuthority('UPDATE')")
    @PutMapping("/Vehiculo/{id}")
    public VehiculoResponse actualizarVehiculo(@PathVariable(name = "id") Long id, @RequestBody VehiculoRequest vehiculoDto) throws VehiculoNoEncontradoExcepcion {
        return vehiculoService.updateVehiculo(id, vehiculoDto);
    }

    @PreAuthorize("hasAuthority('DELETE')")
    @DeleteMapping("/Vehiculo/{id}")
    public void eliminarVehiculo(@PathVariable(name = "id") Long id) throws VehiculoNoEncontradoExcepcion {
        vehiculoService.deleteVehiculo(id);
    }
    //fitrado por placa
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/Vehiculos/search")
    public List<VehiculoResponse> buscarVehiculos(@RequestParam(name = "keyword", defaultValue = "") String keyword){
        return vehiculoService.searchVehiculos("%" + keyword + "%");
    }
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/por-cliente/{clienteId}")
    public List<VehiculoResponse> listarVehiculosPorCliente(@PathVariable Long clienteId) {
        return vehiculoService.listByCliente(clienteId);  // Método del servicio
    }

    /*
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/cuentas/operaciones/{cuentaId}")
    public List<VehiculoResponse> listHistorialCuenta(@PathVariable Long vehiculoId) throws VehiculoNoEncontradoExcepcion {
        return vehiculoService.listHistorialVheiculo(vehiculoId);
    }
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/cuentas/pageVehiculos/{vehiculoId}")
    public List<VehiculoResponse> listHistorialPaginadoCuenta(@PathVariable Long id, @RequestParam(name = "page", defaultValue = "0") int page,
                                                              @RequestParam(name = "size", defaultValue = "5") int size) throws VehiculoNoEncontradoExcepcion {
        return vehiculoService.getHistorialVehiculo( id, page, size);
    }*/
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/paginacion")
    public ResponseEntity<VehiculoPageReponse> getVehiculo(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        VehiculoPageReponse paginaDecehiculos= vehiculoService.getVehiculoPageReponse(page, size);
        return ResponseEntity.ok(paginaDecehiculos);
    }
}
