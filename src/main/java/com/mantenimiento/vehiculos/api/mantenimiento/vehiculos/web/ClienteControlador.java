package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.Web;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteJuridicoRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.ClienteNaturalRequest;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteJuridicoResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteNaturalResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.ClienteResponse;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/natural/listar")
    public List<ClienteNaturalResponse> listarClientesNaturales(){
        return clienteService.readAllClienteNatural();
    }

    @GetMapping("/natural/por-id")
    public ClienteNaturalResponse obtenerClienteNaturalPorId(Long id){
        return clienteService.readClienteNaturalById(id);
    }

    @PutMapping("/natural/actualizar")
    public ClienteNaturalResponse actualizarClienteNatural(@PathVariable Long id, @RequestBody ClienteNaturalRequest clienteNaturalRequest){
        return clienteService.updateClienteNatural(id, clienteNaturalRequest);
    }


    @PostMapping("/juridico/guardar")
    public ClienteJuridicoResponse createClienteJuridico(@RequestBody ClienteJuridicoRequest clienteJuridicoRequest){
        return clienteService.createClienteJuridico(clienteJuridicoRequest);
    }

    @GetMapping("/juridico/listar")
    public List<ClienteJuridicoResponse> listarClientesJuridicos(){
        return clienteService.readAllClienteJuridico();
    }

    @GetMapping("/juridico/por-id")
    public ClienteJuridicoResponse obtenerClienteJuridicoPorId(Long id){
        return clienteService.readClienteJuridicoById(id);
    }

    @PutMapping("/juridico/actualizar")
    public ClienteJuridicoResponse actualizarClienteNatural(@PathVariable Long id, @RequestBody ClienteJuridicoRequest clienteJuridicoRequest){
        return clienteService.updateClienteJuridico(id, clienteJuridicoRequest);
    }



    @GetMapping("/listar/todos")
    public List<ClienteResponse> listarClientes(){
        return clienteService.readAllCliente();
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarCliente(@PathVariable Long id){
        clienteService.delete(id);
    }

}
