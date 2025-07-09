package com.tiendafiel.fidelidad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.tiendafiel.fidelidad.models.Cliente;
import com.tiendafiel.fidelidad.models.Compra;
import com.tiendafiel.fidelidad.repositories.ClienteRepository;
import com.tiendafiel.fidelidad.repositories.CompraRepository;
import com.tiendafiel.fidelidad.services.CompraServices;

import org.junit.jupiter.api.BeforeEach;

class GestionNivelesTest {

    private ClienteRepository clienteRepo;
    private CompraRepository compraRepo;
 

    @BeforeEach
    void setup() {
        clienteRepo = new ClienteRepository();
        compraRepo = new CompraRepository();
    }

    // Métodos auxiliares reutilizables

    private Cliente crearClienteConPuntos(String nombre, String correo, int puntos) {
        Cliente cliente = new Cliente(nombre, correo);
        cliente.sumarPuntos(puntos);
        clienteRepo.agregarCliente(cliente);
        return cliente;
    }

    private CompraServices crearCompraServices() {
        return new CompraServices(compraRepo, clienteRepo);
    }

    // Gestión de niveles

    @Test
    void testSubeAPlataExactamente500() {
        Cliente cliente = crearClienteConPuntos("Cami", "cami@example.com", 490); // aún Bronce
        CompraServices service = crearCompraServices();

        Compra compra = new Compra(cliente.getId(), 1000, "2025-06-26"); // 10 pts ×1.0 = 10
        service.registrarCompra(cliente.getId(), compra);

        assertEquals(500, cliente.getPuntos());
        assertEquals("Plata", cliente.getNivel());
    }

    @Test
    void testSubeAOroExactamente1500() {
        Cliente cliente = crearClienteConPuntos("Leo", "leo@example.com", 1488); // en Plata
        CompraServices service = crearCompraServices();

        Compra compra = new Compra(cliente.getId(), 1000, "2025-06-26"); // 10 ×1.2 = 12
        service.registrarCompra(cliente.getId(), compra);

        assertEquals(1500, cliente.getPuntos());
        assertEquals("Oro", cliente.getNivel());
    }

    @Test
    void testSubeAPlatinoExactamente3000() {
        Cliente cliente = crearClienteConPuntos("Vale", "vale@example.com", 2985); // en Oro
        CompraServices service = crearCompraServices();

        Compra compra = new Compra(cliente.getId(), 1000, "2025-06-26"); // 10 ×1.5 = 15
        service.registrarCompra(cliente.getId(), compra);

        assertEquals(3000, cliente.getPuntos());
        assertEquals("Platino", cliente.getNivel());
    }
}
