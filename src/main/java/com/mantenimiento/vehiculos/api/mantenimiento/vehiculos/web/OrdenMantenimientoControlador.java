package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.Web;

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
    public List<OrdenMantenimientoResponse> findAll() {
        return ordenMantenimientoService.readAll();
    }

    @GetMapping("/por-id/{id}")
    public OrdenMantenimientoResponse findById(@PathVariable Long id){
        return ordenMantenimientoService.readById(id);
    }

    @PutMapping("/actualizar/{id}")
    public OrdenMantenimientoResponse update(@PathVariable Long id, @RequestBody OrdenMantenimientoRequest ordenMantenimientoRequest){
        return ordenMantenimientoService.update(id, ordenMantenimientoRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        ordenMantenimientoService.delete(id);
    }
}
