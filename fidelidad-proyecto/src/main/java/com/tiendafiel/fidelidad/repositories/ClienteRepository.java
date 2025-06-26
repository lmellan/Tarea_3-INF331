package com.tiendafiel.fidelidad.repositories;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tiendafiel.fidelidad.models.Cliente;

public class ClienteRepository {
    private Map<String, Cliente> clientes = new HashMap<>();

    public void agregarCliente(Cliente cliente) {
        clientes.put(cliente.getId(), cliente); // actualiza si ya existe
    }

    public Cliente obtenerCliente(String id) {
        return clientes.get(id);
    }

    public void eliminarCliente(String id) {
        clientes.remove(id);
    }

    public Collection<Cliente> listarClientes() {
        return clientes.values();
    }
}
