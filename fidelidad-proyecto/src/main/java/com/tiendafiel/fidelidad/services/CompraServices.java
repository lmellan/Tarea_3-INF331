package com.tiendafiel.fidelidad.services;

import com.tiendafiel.fidelidad.models.Cliente;
import com.tiendafiel.fidelidad.models.Compra;
import com.tiendafiel.fidelidad.repositories.CompraRepository;
import com.tiendafiel.fidelidad.repositories.ClienteRepository;


public class CompraServices {

    private CompraRepository repo;
    private ClienteRepository clienteRepo;

    public CompraServices(CompraRepository repo, ClienteRepository clienteRepo) {
        this.repo = repo;
        this.clienteRepo = clienteRepo;
    }

    public void registrarCompra(int idCliente, Compra compra) {
        Cliente cliente = clienteRepo.obtenerCliente(idCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        double factor = cliente.getMultiplicadorNivel();
        int puntos = (int)((compra.getMonto() / 100.0) * factor);

        cliente.sumarPuntos(puntos);
        cliente.agregarCompra(compra);

        long comprasHoy = cliente.contarComprasEnFecha(compra.getFecha());
        if (comprasHoy == 3) {
            cliente.sumarPuntos(10); // bonus
        }

        repo.registrarCompra(compra);
    }

    public void eliminarCompra(int idCompra) {
        Compra compra = repo.obtenerCompra(idCompra);
        if (compra == null) return;

        Cliente cliente = clienteRepo.obtenerCliente(compra.getIdCliente());
        if (cliente != null) {
            cliente.eliminarCompra(compra);
        }

        repo.eliminarCompra(idCompra);
    }


}



