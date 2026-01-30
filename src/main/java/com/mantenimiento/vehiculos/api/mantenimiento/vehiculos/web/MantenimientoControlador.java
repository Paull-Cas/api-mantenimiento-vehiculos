package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponsePageDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.TipoMantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.MantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/mantenimiento-vehiculos")
public class MantenimientoControlador {

    @Autowired
    private MantenimientoService mantenimientoService;

    @GetMapping("/{idMantenimiento}")
    public ResponseEntity<MantenimientoResponseDTO> getMantenimientoPorId(@PathVariable Long idMantenimiento) throws MantenimientoNoEncontradoExcepcion {
        MantenimientoResponseDTO mantenimiento = mantenimientoService.getMantenimientoPorId(idMantenimiento);
        return ResponseEntity.ok(mantenimiento);
    }

    @GetMapping
    public ResponseEntity<List<MantenimientoResponseDTO>> getMantenimientos() {
        List<MantenimientoResponseDTO> mantenimientos = mantenimientoService.getTodosLosMantenimientos();
        return ResponseEntity.ok(mantenimientos);
    }

    @PostMapping
    public ResponseEntity<MantenimientoResponseDTO> crearMantenimiento(@RequestBody MantenimientoRequest mantenimientoRequest) {
        MantenimientoResponseDTO mantenimiento = mantenimientoService.saveMantenimiento(mantenimientoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(mantenimiento);
    }

    @PutMapping("/{idMantenimiento}")
    public ResponseEntity<MantenimientoResponseDTO> actualizarMantenimiento(
            @PathVariable Long idMantenimiento,
            @RequestBody MantenimientoRequest mantenimientoRequest) throws MantenimientoNoEncontradoExcepcion {
        MantenimientoResponseDTO mantenimiento = mantenimientoService.updateMantenimiento(idMantenimiento, mantenimientoRequest);
        return ResponseEntity.ok(mantenimiento);
    }

    @DeleteMapping("/{idMantenimiento}")
    public ResponseEntity<Void> eliminarMantenimiento(@PathVariable Long idMantenimiento) throws MantenimientoNoEncontradoExcepcion {
        mantenimientoService.deleteMantenimiento(idMantenimiento);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<MantenimientoResponseDTO>> filtrarPorTipo(@RequestParam("tipo") TipoMantenimiento tipo) {
        List<MantenimientoResponseDTO> mantenimientos = mantenimientoService.buscarMantenimientosPorTipo(tipo);
        return ResponseEntity.ok(mantenimientos);
    }

    @GetMapping("/paginacion")
    public ResponseEntity<MantenimientoResponsePageDTO> getMantenimientosPaginados(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        MantenimientoResponsePageDTO paginaDeMantenimientos = mantenimientoService.getMantenimientosPaginado(page, size);
        return ResponseEntity.ok(paginaDeMantenimientos);
    }
}