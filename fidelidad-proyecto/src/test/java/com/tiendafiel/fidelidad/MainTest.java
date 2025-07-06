package com.tiendafiel.fidelidad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.tiendafiel.fidelidad.models.Cliente;
import com.tiendafiel.fidelidad.models.Compra;
import com.tiendafiel.fidelidad.repositories.ClienteRepository;
import com.tiendafiel.fidelidad.repositories.CompraRepository;
import com.tiendafiel.fidelidad.services.CompraServices;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Collection;

class MainTest {

    @Test
    void testMainMenuExecutionWithExit() {
        String input = "4\n"; // opción de salida inmediata
        InputStream simulatedIn = new ByteArrayInputStream(input.getBytes());
        assertDoesNotThrow(() -> Main.ejecutarPrograma(simulatedIn));
    }

    @Test
    void testAgregarClienteValido() {
        ClienteRepository repo = new ClienteRepository();

        Cliente cliente = new Cliente("Ana", "ana@gmail.com");
        repo.agregarCliente(cliente);
        Cliente obtenido = repo.obtenerCliente(cliente.getId());

        assertNotNull(obtenido);
        assertEquals("Ana", obtenido.getNombre());
        assertEquals("ana@gmail.com", obtenido.getCorreo());
        assertEquals(0, obtenido.getPuntos());
        assertEquals("Bronce", obtenido.getNivel());
        assertEquals(0, obtenido.getStreakDias());
    }

    @Test
    void testCrearClienteCorreoInvalido() {
        String nombre = "Pepe";
        String correoInvalido = "pepegmail.com"; // no contiene "@"

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cliente(nombre, correoInvalido);
        });

        String expectedMessage = "Correo inválido";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testActualizarClienteExistente() {
        ClienteRepository repo = new ClienteRepository();
        Cliente original = new Cliente("Ana", "ana@gmail.com");
        repo.agregarCliente(original);

        repo.actualizarDatosCliente(original.getId(), "Ana María", "ana123@gmail.com");

        Cliente obtenido = repo.obtenerCliente(original.getId());
        assertEquals("Ana María", obtenido.getNombre());
        assertEquals("ana123@gmail.com", obtenido.getCorreo());
    }

    @Test
    void testEliminarClienteExistente() {
        ClienteRepository repo = new ClienteRepository();
        Cliente cliente = new Cliente("Ana", "ana@gmail.com");
        repo.agregarCliente(cliente);
        repo.eliminarCliente(cliente.getId());

        Cliente obtenido = repo.obtenerCliente(cliente.getId());
        assertNull(obtenido);
    }

    @Test
    void testListarClientes() {
        ClienteRepository repo = new ClienteRepository();
        Cliente c1 = new Cliente("Ana", "ana@gmail.com");
        Cliente c2 = new Cliente("Luis", "luis@gmail.com");
        Cliente c3 = new Cliente("Sofía", "sofia@gmail.com");

        repo.agregarCliente(c1);
        repo.agregarCliente(c2);
        repo.agregarCliente(c3);

        Collection<Cliente> clientes = repo.listarClientes();

        assertEquals(3, clientes.size());
        assertTrue(clientes.stream().anyMatch(c -> c.getId() == c1.getId()));
        assertTrue(clientes.stream().anyMatch(c -> c.getId() == c2.getId()));
        assertTrue(clientes.stream().anyMatch(c -> c.getId() == c3.getId()));
    }

    @Test
    void testRegistrarCompraClienteBronce() {
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();

        Cliente cliente = new Cliente("Ana", "ana@gmail.com");
        int idCliente = cliente.getId();
        clienteRepo.agregarCliente(cliente);

        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra compra = new Compra(idCliente, 850, "2025-06-25");
        int idCompra = compra.getId();

        service.registrarCompra(idCliente, compra);

        assertEquals(8, cliente.getPuntos()); // 850 / 100 = 8 puntos
        assertNotNull(compraRepo.obtenerCompra(idCompra));
    }

    @Test
    void testRegistrarCompraClientePlata() {
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();

        Cliente cliente = new Cliente("Beto", "beto@gmail.com");
        int idCliente = cliente.getId(); 
        clienteRepo.agregarCliente(cliente);

        cliente.sumarPuntos(600); // Nivel Plata

        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra compra = new Compra(idCliente, 1000, "2025-06-25");
        int idCompra = compra.getId();

        service.registrarCompra(idCliente, compra);

        assertEquals(612, cliente.getPuntos()); // 600 + (1000/100)*1.2 = 12
        assertNotNull(compraRepo.obtenerCompra(idCompra));
    }

    @Test
    void testRegistrarCompraClienteOro() {
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();

        Cliente cliente = new Cliente("Pedro", "pedro@example.com");
        int idCliente = cliente.getId(); 
        clienteRepo.agregarCliente(cliente);

        cliente.sumarPuntos(1600); // Nivel Oro

        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra compra = new Compra(idCliente, 1000, "2025-06-26");
        int idCompra = compra.getId();

        service.registrarCompra(idCliente, compra);

        assertEquals(1615, cliente.getPuntos()); // 1600 + (1000/100)*1.5 = 15
        assertNotNull(compraRepo.obtenerCompra(idCompra));
    }

    @Test
    void testRegistrarCompraClientePlatino() {
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();

        Cliente cliente = new Cliente("Laura", "laura@example.com");
        int idCliente = cliente.getId(); 
        clienteRepo.agregarCliente(cliente);

        cliente.sumarPuntos(3000); // Nivel Platino

        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra compra = new Compra(idCliente, 1000, "2025-06-26");
        int idCompra = compra.getId();

        service.registrarCompra(idCliente, compra);

        assertEquals(3020, cliente.getPuntos()); // 3000 + (1000/100)*2 = 20
        assertNotNull(compraRepo.obtenerCompra(idCompra));
    }

    @Test
    void testBonificacionPorStreakTresComprasMismoDia() {
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();

        Cliente cliente = new Cliente("Luis", "luis@example.com");
        int idCliente = cliente.getId(); 
        clienteRepo.agregarCliente(cliente);

        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra c1 = new Compra(idCliente, 100, "2025-06-26");
        Compra c2 = new Compra(idCliente, 100, "2025-06-26");
        Compra c3 = new Compra(idCliente, 100, "2025-06-26");

        service.registrarCompra(idCliente, c1);
        service.registrarCompra(idCliente, c2);
        service.registrarCompra(idCliente, c3); // 3ra compra → bonus

        int puntosEsperados = 3 + 10; // 3 puntos + 10 bonus
        assertEquals(puntosEsperados, cliente.getPuntos());
        assertNotNull(compraRepo.obtenerCompra(c1.getId()));
        assertNotNull(compraRepo.obtenerCompra(c2.getId()));
        assertNotNull(compraRepo.obtenerCompra(c3.getId()));
    }

    @Test
    void testRegistrarCompraClienteInexistente() {
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra compra = new Compra(999, 500, "2025-06-26"); // cliente inexistente

        assertThrows(IllegalArgumentException.class, () -> {
            service.registrarCompra(999, compra);
        });
    }

    @Test
    void testListarComprasRegistradas() {
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();

        Cliente cliente = new Cliente("Carlos", "carlos@example.com");
        int idCliente = cliente.getId();
        clienteRepo.agregarCliente(cliente);

        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra c1 = new Compra(idCliente, 500, "2025-06-25");
        Compra c2 = new Compra(idCliente, 400, "2025-06-26");

        service.registrarCompra(idCliente, c1);
        service.registrarCompra(idCliente, c2);

        var compras = compraRepo.listarCompras().stream().toList();
        assertEquals(2, compras.size());
        assertTrue(compras.stream().anyMatch(c -> c.getId() == c1.getId()));
        assertTrue(compras.stream().anyMatch(c -> c.getId() == c2.getId()));
    }

    @Test
    void testSubeAPlataExactamente500() {
        Cliente cliente = new Cliente("Cami", "cami@example.com");
        int id = cliente.getId();
        cliente.sumarPuntos(490); // aún Bronce

        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        clienteRepo.agregarCliente(cliente);
        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra compra = new Compra(id, 1000, "2025-06-26"); // 10 pts ×1.0 = 10
        service.registrarCompra(id, compra);

        assertEquals(500, cliente.getPuntos());
        assertEquals("Plata", cliente.getNivel());
    }

    @Test
    void testSubeAOroExactamente1500() {
        Cliente cliente = new Cliente("Leo", "leo@example.com");
        int id = cliente.getId();
        cliente.sumarPuntos(1488); // en Plata

        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        clienteRepo.agregarCliente(cliente);
        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra compra = new Compra(id, 1000, "2025-06-26"); // 10 ×1.2 = 12
        service.registrarCompra(id, compra);

        assertEquals(1500, cliente.getPuntos());
        assertEquals("Oro", cliente.getNivel());
    }

    @Test
    void testSubeAPlatinoExactamente3000() {
        Cliente cliente = new Cliente("Vale", "vale@example.com");
        int id = cliente.getId();
        cliente.sumarPuntos(2985); // en Oro

        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        clienteRepo.agregarCliente(cliente);
        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra compra = new Compra(id, 1000, "2025-06-26"); // 10 ×1.5 = 15
        service.registrarCompra(id, compra);

        assertEquals(3000, cliente.getPuntos());
        assertEquals("Platino", cliente.getNivel());
    }


    // se incorporan estos test post utilizaciond de TDD

    @Test
    void testCorreoNuloLanzaExcepcion() {
        Cliente cliente = new Cliente("Carlos", "carlos@example.com");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            cliente.setCorreo(null);
        });

        assertEquals("Correo inválido", ex.getMessage());
    }

    @Test
    void testActualizarClienteInexistente() {
        ClienteRepository repo = new ClienteRepository();

        assertDoesNotThrow(() -> {
            repo.actualizarDatosCliente(999, "Nombre", "correo@example.com");
        });
    }

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

    @Test
    void testGetIdClienteConClienteCreado() {
        Cliente cliente = new Cliente("Juan Pérez", "juan@example.com");

        Compra compra = new Compra(cliente.getId(), 5000, "2025-07-01");

        assertEquals(cliente.getId(), compra.getIdCliente());
    }


}