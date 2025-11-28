package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoCorrectivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoPreventivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponsePageDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.MantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/mantenimiento-vehiculos")
public class MantenimientoControlador {

    @Autowired
    private MantenimientoService mantenimientoService;

    @PreAuthorize("hasAuthority('READ')") // Roles que tienen acceso: ADMIN, USER, INVITED, DEVELOPER
    @GetMapping("/{idMantenimiento}")
    public MantenimientoResponseDTO getMantenimientoPorId(@PathVariable Long idMantenimiento) throws MantenimientoNoEncontradoExcepcion {
        return mantenimientoService.getMantenimientoPorId(idMantenimiento);
    }

    @PreAuthorize("hasAuthority('READ')") // Roles que tienen acceso: ADMIN, USER, INVITED, DEVELOPER
    @GetMapping
    public List<MantenimientoResponseDTO> getMantenimientos() {
        return mantenimientoService.getTodosLosMantenimientos();
    }

    @PreAuthorize("hasAuthority('CREATE')") // Roles que tienen acceso: ADMIN, USER, DEVELOPER
    @PostMapping("/preventivo")
    public MantenimientoResponseDTO crearMantenimientoPreventivo(@RequestBody MantenimientoPreventivoRequestDTO mantPrevDTO){
        return mantenimientoService.saveMantenimientoPreventivo(mantPrevDTO);
    }

    @PreAuthorize("hasAuthority('CREATE')") // Roles que tienen acceso: ADMIN, USER, DEVELOPER
    @PostMapping("/correctivo")
    public MantenimientoResponseDTO crearMantenimientoCorrectivo(@RequestBody MantenimientoCorrectivoRequestDTO mantCorrecDTO){
        return mantenimientoService.saveMantenimientoCorrectivo(mantCorrecDTO);
    }

    @PreAuthorize("hasAuthority('UPDATE')") // Roles que tienen acceso: ADMIN, DEVELOPER
    @PutMapping("/correctivo/{idMantenimiento}")
    public MantenimientoResponseDTO actualizarMantenimientoCorrectivo(@PathVariable Long idMantenimiento, @RequestBody MantenimientoCorrectivoRequestDTO mantCorrecDTO) throws MantenimientoNoEncontradoExcepcion{
        return mantenimientoService.updateMantenimientoCorrectivo(idMantenimiento,mantCorrecDTO);
    }

    @PreAuthorize("hasAuthority('UPDATE')") // Roles que tienen acceso: ADMIN, DEVELOPER
    @PutMapping("/preventivo/{idMantenimiento}")
    public MantenimientoResponseDTO actualizarMantenimientoPreventivo(@PathVariable Long idMantenimiento, @RequestBody MantenimientoPreventivoRequestDTO mantPrevenDTO) throws MantenimientoNoEncontradoExcepcion{
        return mantenimientoService.updateMantenimientoPreventivo(idMantenimiento,mantPrevenDTO);
    }

    @PreAuthorize("hasAuthority('DELETE')") // Roles que tienen acceso: ADMIN, DEVELOPER
    @DeleteMapping("/{idMantenimiento}")
    public void eliminarMantenimiento(@PathVariable Long idMantenimiento) throws MantenimientoNoEncontradoExcepcion {
        mantenimientoService.deleteMantenimiento(idMantenimiento);
    }
    @PreAuthorize("hasAuthority('READ')") // Roles que tienen acceso: ADMIN, USER, INVITED, DEVELOPER
    @GetMapping("/search")
    public ResponseEntity<List<MantenimientoResponseDTO>> filtrarPorTipo(@RequestParam("tipo") String tipo) {
        List<MantenimientoResponseDTO> mantenimientos = mantenimientoService.buscarMantenimientosPorTipo(tipo);
        return ResponseEntity.ok(mantenimientos);
    }
    @PreAuthorize("hasAuthority('READ')") // Roles que tienen acceso: ADMIN, USER, INVITED, DEVELOPER
    @GetMapping("/paginacion")
    public ResponseEntity<MantenimientoResponsePageDTO> getMantenimientos(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        MantenimientoResponsePageDTO paginaDeMantenimientos = mantenimientoService.getMantenimientosPaginado(page, size);
        return ResponseEntity.ok(paginaDeMantenimientos);
    }
}
