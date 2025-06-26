package com.tiendafiel.fidelidad.models;

import java.util.ArrayList;
import java.util.List;



public class Cliente {
    private String id;
    private String nombre;
    private String correo;
    private int puntos;
    private String nivel;
    private int streak;

    private List<Compra> compras = new ArrayList<>();

    public void agregarCompra(Compra compra) {
        compras.add(compra);
    }

    public long contarComprasEnFecha(String fecha) {
        return compras.stream().filter(c -> c.getFecha().equals(fecha)).count();
    }

    public Cliente(String id, String nombre, String correo) {
        validarCorreo(correo);
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.puntos = 0;
        this.nivel = "Bronce";
        this.streak = 0;
    }
    
    public void sumarPuntos(int puntos) {
        this.puntos += puntos;
        actualizarNivel();
    }

    private void actualizarNivel() {
        if (puntos >= 3000) {
            nivel = "Platino";
        } else if (puntos >= 1500) {
            nivel = "Oro";
        } else if (puntos >= 500) {
            nivel = "Plata";
        } else {
            nivel = "Bronce";
        }
    }
    private void validarCorreo(String correo) {
        if (correo == null || !correo.contains("@")) {
            throw new IllegalArgumentException("Correo inválido");
        }
    }
    public double getMultiplicadorNivel() {
        if (puntos >= 3000) return 2.0; // Platino
        if (puntos >= 1600) return 1.5; // Oro
        if (puntos >= 600) return 1.2;  // Plata
        return 1.0; // Bronce
    }

    public String getNombre() {return nombre;}
    public String getCorreo() {return correo;}
    public String getId() { return id; }
    public int getPuntos() { return puntos; }
    public String getNivel() { return nivel; }
    public int getStreakDias() {return streak;}

}
