package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.OrdenMantenimientoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.OrdenMantenimientoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.OrdenMantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orden-mantenimiento")
public class OrdenMantenimientoControlador {

    @Autowired
    private OrdenMantenimientoService ordenMantenimientoService;

    @PostMapping("/guardar")
    public OrdenMantenimientoResponse create(@RequestBody OrdenMantenimientoRequest ordenMantenimientoRequest) {
        return ordenMantenimientoService.create(ordenMantenimientoRequest);
    }

    @GetMapping("/todos")
    public List<OrdenMantenimientoResponse> listar(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
        )
    {
        return ordenMantenimientoService.readAll(keyword, page, size);
    }

    @GetMapping("/por-id/{id}")
    public OrdenMantenimientoResponse findById(@PathVariable Long id){
        return ordenMantenimientoService.readById(id);
    }

    @PutMapping("/actualizar/{id}")
    public OrdenMantenimientoResponse update(@PathVariable Long id, @RequestBody OrdenMantenimientoRequest ordenMantenimientoRequest){
        return ordenMantenimientoService.update(id, ordenMantenimientoRequest);
    }

    @DeleteMapping("/eliminar/{id}")
    public void delete(@PathVariable Long id){
        ordenMantenimientoService.delete(id);
    }
}
