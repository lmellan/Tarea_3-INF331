package com.tiendafiel.fidelidad.repositories;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tiendafiel.fidelidad.models.Cliente;

public class ClienteRepository {
    private final Map<Integer, Cliente> clientes = new HashMap<>();

    public void agregarCliente(Cliente cliente) {
        clientes.put(cliente.getId(), cliente); // actualiza si ya existe
    }

    public Cliente obtenerCliente(int id) {
        return clientes.get(id);
    }

    public void eliminarCliente(int id) {
        clientes.remove(id);
    }

    public Collection<Cliente> listarClientes() {
        return clientes.values();
    }

    public void actualizarDatosCliente(int id, String nuevoNombre, String nuevoCorreo) {
        Cliente cliente = clientes.get(id);
        if (cliente != null) {
            cliente.setNombre(nuevoNombre);
            cliente.setCorreo(nuevoCorreo);
        }
    }


}
