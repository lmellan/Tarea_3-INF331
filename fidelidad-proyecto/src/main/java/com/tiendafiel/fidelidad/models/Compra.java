package com.tiendafiel.fidelidad.models;

public class Compra {
    private static int contadorIds = 1;

    private final int idCompra;
    private final int idCliente;
    private final long monto;
    private final String fecha;

    public Compra(int idCliente, long monto, String fecha) {
        if (fecha == null || fecha.isBlank()) {
            throw new IllegalArgumentException("La fecha no puede ser nula ni vacía.");
        }
        if (monto < 0) {
            throw new IllegalArgumentException("El monto debe ser positivo.");
        }

        this.idCompra = contadorIds++;
        this.idCliente = idCliente;
        this.monto = monto;
        this.fecha = fecha;
    }

    public int getId() {
        return idCompra;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public long getMonto() {
        return monto;
    }

    public String getFecha() {
        return fecha;
    }
}
