package com.tiendafiel.fidelidad.services;

import com.tiendafiel.fidelidad.models.Cliente;
import com.tiendafiel.fidelidad.models.Compra;
import com.tiendafiel.fidelidad.repositories.CompraRepository;
import com.tiendafiel.fidelidad.repositories.ClienteRepository;



public class ClienteServices {

    private final ClienteRepository clienteRepo;
    private final CompraRepository compraRepo;

    public ClienteServices(ClienteRepository clienteRepo, CompraRepository compraRepo) {
        this.clienteRepo = clienteRepo;
        this.compraRepo = compraRepo;
    }

    public void eliminarClienteYCompras(int idCliente) {
        Cliente cliente = clienteRepo.obtenerCliente(idCliente);
        if (cliente == null) return;

        for (Compra compra : cliente.getHistorialCompras()) {
            compraRepo.eliminarCompra(compra.getId());
        }

        clienteRepo.eliminarCliente(idCliente);
    }
}
