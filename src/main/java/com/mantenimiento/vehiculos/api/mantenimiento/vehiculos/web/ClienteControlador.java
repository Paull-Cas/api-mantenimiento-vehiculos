package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteJuridicoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteNaturalRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteJuridicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteNaturalResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cliente")
public class ClienteControlador {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/natural/guardar")
    public ClienteNaturalResponse createClienteNatural(@RequestBody ClienteNaturalRequest clienteNaturalRequest){
        return clienteService.createClienteNatural(clienteNaturalRequest);
    }

    @PutMapping("/natural/actualizar/{id}")
    public ClienteNaturalResponse actualizarClienteNatural(@PathVariable Long id, @RequestBody ClienteNaturalRequest clienteNaturalRequest){
        return clienteService.updateClienteNatural(id, clienteNaturalRequest);
    }

    @PostMapping("/juridico/guardar")
    public ClienteJuridicoResponse createClienteJuridico(@RequestBody ClienteJuridicoRequest clienteJuridicoRequest){
        return clienteService.createClienteJuridico(clienteJuridicoRequest);
    }

    @PutMapping("/juridico/actualizar/{id}")
    public ClienteJuridicoResponse actualizarClienteJuridico(@PathVariable Long id, @RequestBody ClienteJuridicoRequest clienteJuridicoRequest){
        return clienteService.updateClienteJuridico(id, clienteJuridicoRequest);
    }

    @GetMapping("/listar")
    public List<ClienteResponse> listarClientes(@RequestParam(name="keyword", defaultValue = "") String keyword,
                                                @RequestParam(name="tipo", defaultValue = "natural") String tipo,
                                                @RequestParam(name="page", defaultValue = "0") int page,
                                                @RequestParam(name="size", defaultValue = "5") int size)
    {
        return clienteService.readAllClientes(keyword, tipo, page, size);
    }

    @GetMapping("/por-id/{id}")
    public ClienteResponse clientePorId(@PathVariable Long id){
        return clienteService.readClienteById(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarCliente(@PathVariable Long id){
        clienteService.delete(id);
    }
}