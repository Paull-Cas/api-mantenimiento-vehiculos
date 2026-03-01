package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.TallerRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.TallerResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.TallerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taller")
public class TallerControlador {

    @Autowired
    private TallerService tallerService;

    @PreAuthorize("hasAuthority('CREATE_TALLER')")
    @PostMapping
    public ResponseEntity<TallerResponseDTO> crearTaller(@Valid @RequestBody TallerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tallerService.crearTaller(request));
    }

    @PreAuthorize("hasAuthority('READ_TALLER')")
    @GetMapping("/{id}")
    public ResponseEntity<TallerResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tallerService.obtenerTallerPorId(id));
    }

    @PreAuthorize("hasAuthority('READ_TALLER')")
    @GetMapping
    public ResponseEntity<List<TallerResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(tallerService.obtenerTodos());
    }

    @PreAuthorize("hasAuthority('READ_TALLER')")
    @GetMapping("/ciudad")
    public ResponseEntity<List<TallerResponseDTO>> buscarPorCiudad(@RequestParam String ciudad) {
        return ResponseEntity.ok(tallerService.buscarPorCiudad(ciudad));
    }

    @PreAuthorize("hasAuthority('READ_TALLER')")
    @GetMapping("/distrito")
    public ResponseEntity<List<TallerResponseDTO>> buscarPorDistrito(@RequestParam String distrito) {
        return ResponseEntity.ok(tallerService.buscarPorDistrito(distrito));
    }

    @PreAuthorize("hasAuthority('READ_TALLER')")
    @GetMapping("/buscar")
    public ResponseEntity<List<TallerResponseDTO>> buscarPorKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(tallerService.buscarPorKeyword(keyword));
    }

    @PreAuthorize("hasAuthority('UPDATE_TALLER')")
    @PutMapping("/{id}")
    public ResponseEntity<TallerResponseDTO> actualizarTaller(
            @PathVariable Long id,
            @Valid @RequestBody TallerRequest request) {
        return ResponseEntity.ok(tallerService.actualizarTaller(id, request));
    }

    @PreAuthorize("hasAuthority('DELETE_TALLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTaller(@PathVariable Long id) {
        tallerService.eliminarTaller(id);
        return ResponseEntity.noContent().build();
    }
}