package com.tiendafiel.fidelidad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.tiendafiel.fidelidad.models.Cliente;
import com.tiendafiel.fidelidad.models.Compra;
import com.tiendafiel.fidelidad.repositories.ClienteRepository;
import com.tiendafiel.fidelidad.repositories.CompraRepository;
import com.tiendafiel.fidelidad.services.CompraServices;

 
import org.junit.jupiter.api.BeforeEach;

class GestionComprasTest {

    private ClienteRepository clienteRepo;
    private CompraRepository compraRepo;
 
    @BeforeEach
    void setup() {
        clienteRepo = new ClienteRepository();
        compraRepo = new CompraRepository();

    }

    // métodos para inicialización repetida -> se modulariza un poco el código más no la lógica de los tests

    private Cliente crearClienteConPuntos(String nombre, String correo, int puntos) {
        Cliente cliente = new Cliente(nombre, correo);
        cliente.sumarPuntos(puntos);
        clienteRepo.agregarCliente(cliente);
        return cliente;
    }

    private CompraServices crearCompraServices() {
        return new CompraServices(compraRepo, clienteRepo);
    }

    // Gestión de Compras

    @Test
    void testRegistrarCompraClienteBronce() {
        Cliente cliente = crearClienteConPuntos("Ana", "ana@gmail.com", 0);
        CompraServices service = crearCompraServices();

        Compra compra = new Compra(cliente.getId(), 850, "2025-06-25");
        service.registrarCompra(cliente.getId(), compra);

        assertEquals(8, cliente.getPuntos()); // 850 / 100 = 8 puntos
        assertNotNull(compraRepo.obtenerCompra(compra.getId()));
    }

    @Test
    void testRegistrarCompraClientePlata() {
        Cliente cliente = crearClienteConPuntos("Beto", "beto@gmail.com", 600); // Nivel Plata
        CompraServices service = crearCompraServices();

        Compra compra = new Compra(cliente.getId(), 1000, "2025-06-25");
        service.registrarCompra(cliente.getId(), compra);

        assertEquals(612, cliente.getPuntos()); // 600 + 12
        assertNotNull(compraRepo.obtenerCompra(compra.getId()));
    }

    @Test
    void testRegistrarCompraClienteOro() {
        Cliente cliente = crearClienteConPuntos("Pedro", "pedro@example.com", 1600); // Nivel Oro
        CompraServices service = crearCompraServices();

        Compra compra = new Compra(cliente.getId(), 1000, "2025-06-26");
        service.registrarCompra(cliente.getId(), compra);

        assertEquals(1615, cliente.getPuntos()); // 1600 + 15
        assertNotNull(compraRepo.obtenerCompra(compra.getId()));
    }

    @Test
    void testRegistrarCompraClientePlatino() {
        Cliente cliente = crearClienteConPuntos("Laura", "laura@example.com", 3000); // Nivel Platino
        CompraServices service = crearCompraServices();

        Compra compra = new Compra(cliente.getId(), 1000, "2025-06-26");
        service.registrarCompra(cliente.getId(), compra);

        assertEquals(3020, cliente.getPuntos()); // 3000 + 20
        assertNotNull(compraRepo.obtenerCompra(compra.getId()));
    }

    @Test
    void testBonificacionPorStreakTresComprasMismoDia() {
        Cliente cliente = crearClienteConPuntos("Luis", "luis@example.com", 0);
        CompraServices service = crearCompraServices();

        Compra c1 = new Compra(cliente.getId(), 100, "2025-06-26");
        Compra c2 = new Compra(cliente.getId(), 100, "2025-06-26");
        Compra c3 = new Compra(cliente.getId(), 100, "2025-06-26");

        service.registrarCompra(cliente.getId(), c1);
        service.registrarCompra(cliente.getId(), c2);
        service.registrarCompra(cliente.getId(), c3); // 3ra compra → bonus

        assertEquals(13, cliente.getPuntos()); // 3 + 10 bonus
        assertNotNull(compraRepo.obtenerCompra(c1.getId()));
        assertNotNull(compraRepo.obtenerCompra(c2.getId()));
        assertNotNull(compraRepo.obtenerCompra(c3.getId()));
    }

    @Test
    void testRegistrarCompraClienteInexistente() {
        CompraServices service = crearCompraServices();

        Compra compra = new Compra(999, 500, "2025-06-26"); // cliente inexistente

        assertThrows(IllegalArgumentException.class, () -> {
            service.registrarCompra(999, compra);
        });
    }

    @Test
    void testListarComprasRegistradas() {
        Cliente cliente = crearClienteConPuntos("Carlos", "carlos@example.com", 0);
        CompraServices service = crearCompraServices();

        Compra c1 = new Compra(cliente.getId(), 500, "2025-06-25");
        Compra c2 = new Compra(cliente.getId(), 400, "2025-06-26");

        service.registrarCompra(cliente.getId(), c1);
        service.registrarCompra(cliente.getId(), c2);

        var compras = compraRepo.listarCompras().stream().toList();
        assertEquals(2, compras.size());
        assertTrue(compras.stream().anyMatch(c -> c.getId() == c1.getId()));
        assertTrue(compras.stream().anyMatch(c -> c.getId() == c2.getId()));
    }

    // Se incorporan estos test sin usar TDD tras el análisis en SonarQube 

    @Test
    void testMontoNegativoLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Compra(1, -100, "2024-01-01")
        );
        assertEquals("El monto debe ser positivo.", exception.getMessage());
    }

    @Test
    void testFechaNulaLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Compra(1, 100, null)
        );
        assertEquals("La fecha no puede ser nula ni vacía.", exception.getMessage());
    }

    @Test
    void testFechaVaciaLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Compra(1, 100, "  ")
        );
        assertEquals("La fecha no puede ser nula ni vacía.", exception.getMessage());
    }

    // Implementado nuevas pruebas con TDD

    @Test
    void eliminarCompra_existente_eliminaCorrectamente() {
        Cliente cliente = crearClienteConPuntos("Juan Pérez", "juan@example.com", 0);
        CompraServices compraServices = crearCompraServices();

        Compra compra = new Compra(cliente.getId(), 1000, "2025-07-06");
        compraServices.registrarCompra(cliente.getId(), compra);

        assertTrue(cliente.getHistorialCompras().contains(compra));
        compraServices.eliminarCompra(compra.getId());
        assertNull(compraRepo.obtenerCompra(compra.getId()));
        assertFalse(cliente.getHistorialCompras().contains(compra));
    }

    @Test
    void eliminarCompra_inexistente_noHaceNada() {
        CompraServices compraServices = crearCompraServices();
        compraServices.eliminarCompra(999); // ID inválido
        assertTrue(compraRepo.listarCompras().isEmpty());
    }

    @Test
    void eliminarCompra_clienteEliminado_noFalla() {
        Cliente cliente = crearClienteConPuntos("Luis", "luis@example.com", 0);
        CompraServices compraServices = crearCompraServices();

        Compra compra = new Compra(cliente.getId(), 1500, "2025-07-06");
        compraServices.registrarCompra(cliente.getId(), compra);

        clienteRepo.eliminarCliente(cliente.getId());
        compraServices.eliminarCompra(compra.getId());
        assertNull(compraRepo.obtenerCompra(compra.getId()));
    }


}
