package com.tiendafiel.fidelidad.repositories;

import java.util.HashMap;
import java.util.Map;
import com.tiendafiel.fidelidad.models.Compra;
import java.util.Collection;


public class CompraRepository {
    private Map<String, Compra> compras = new HashMap<>();

    public void registrarCompra(Compra compra) {
        compras.put(compra.getIdCompra(), compra); // cambio
    }

    public Compra obtenerCompra(String idCompra) {
        return compras.get(idCompra); 
    }

    public Collection<Compra> listarCompras() {
        return compras.values();
    }
}
