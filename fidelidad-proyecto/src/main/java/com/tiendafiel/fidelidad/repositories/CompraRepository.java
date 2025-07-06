package com.tiendafiel.fidelidad.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

import com.tiendafiel.fidelidad.models.Compra;

public class CompraRepository {
    private Map<Integer, Compra> compras = new HashMap<>();

    public void registrarCompra(Compra compra) {
        compras.put(compra.getId(), compra);
    }

    public Compra obtenerCompra(int idCompra) {
        return compras.get(idCompra);
    }

    public Collection<Compra> listarCompras() {
        return compras.values();
    }
}
