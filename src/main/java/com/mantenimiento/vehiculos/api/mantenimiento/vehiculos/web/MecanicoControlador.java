package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MecanicoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MecanicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.enums.EstadoMecanico;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.MecanicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mecanicos")
@RequiredArgsConstructor
public class MecanicoControlador {

    private final MecanicoService mecanicoService;

    @PreAuthorize("hasAuthority('CREATE_MECANICO')")
    @PostMapping
    public ResponseEntity<MecanicoResponse> crearMecanico(@RequestBody MecanicoRequest request) {
        MecanicoResponse response = mecanicoService.crearMecanico(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('UPDATE_MECANICO')")
    @PutMapping("/{id}")
    public ResponseEntity<MecanicoResponse> actualizarMecanico(
            @PathVariable Long id,
            @RequestBody MecanicoRequest request) {
        MecanicoResponse response = mecanicoService.actualizarMecanico(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('READ_MECANICO')")
    @GetMapping("/{id}")
    public ResponseEntity<MecanicoResponse> obtenerMecanicoPorId(@PathVariable Long id) {
        MecanicoResponse response = mecanicoService.obtenerMecanicoPorId(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('READ_MECANICO')")
    @GetMapping
    public ResponseEntity<List<MecanicoResponse>> obtenerTodosMecanicos() {
        List<MecanicoResponse> response = mecanicoService.obtenerTodosMecanicos();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('READ_MECANICO')")
    @GetMapping("/disponibles")
    public ResponseEntity<List<MecanicoResponse>> obtenerMecanicosDisponibles() {
        List<MecanicoResponse> response = mecanicoService.obtenerMecanicosDisponibles();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('READ_MECANICO')")
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<MecanicoResponse>> buscarPorEspecialidad(@PathVariable String especialidad) {
        List<MecanicoResponse> response = mecanicoService.buscarPorEspecialidad(especialidad);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('DELETE_MECANICO')")
    @DeleteMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarMecanico(@PathVariable Long id) {
        mecanicoService.desactivarMecanico(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('UPDATE_MECANICO')")
    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activarMecanico(@PathVariable Long id) {
        mecanicoService.activarMecanico(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('UPDATE_MECANICO')")
    @PutMapping("/{id}/cambiar-estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoMecanico nuevoEstado) {
        mecanicoService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok().build();
    }
}
