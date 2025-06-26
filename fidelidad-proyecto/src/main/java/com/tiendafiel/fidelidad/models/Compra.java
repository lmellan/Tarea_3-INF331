package com.tiendafiel.fidelidad.models;

public class Compra {
    private String idCompra;
    private String idCliente;
    private long monto;
    private String fecha;

    public Compra(String idCompra, String idCliente, long monto, String fecha) {
        this.idCompra = idCompra;
        this.idCliente = idCliente;
        this.monto = monto;
        this.fecha = fecha;
    }

    public String getIdCompra() {
        return idCompra;
    }
    public String getFecha() {
        return fecha;
    }

    public long getMonto() { return monto; }
    public String getIdCliente() { return idCliente; }
}
