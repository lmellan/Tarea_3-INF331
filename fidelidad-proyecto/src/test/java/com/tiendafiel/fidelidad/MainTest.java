package com.tiendafiel.fidelidad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.tiendafiel.fidelidad.models.Cliente;
import com.tiendafiel.fidelidad.repositories.ClienteRepository;
import com.tiendafiel.fidelidad.models.Compra;
import com.tiendafiel.fidelidad.repositories.CompraRepository;
import com.tiendafiel.fidelidad.services.CompraServices;



import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.List;

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

        Cliente cliente = new Cliente("1", "Ana", "ana@gmail.com");

        repo.agregarCliente(cliente);
        Cliente obtenido = repo.obtenerCliente("1");

        assertNotNull(obtenido);
        assertEquals("Ana", obtenido.getNombre());
        assertEquals("ana@gmail.com", obtenido.getCorreo());
        assertEquals(0, obtenido.getPuntos());
        assertEquals("Bronce", obtenido.getNivel());
        assertEquals(0, obtenido.getStreakDias());
    }

    @Test
    void testCrearClienteCorreoInvalido() {

        String id = "2";
        String nombre = "Pepe";
        String correoInvalido = "pepegmail.com"; // no contiene "@"

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cliente(id, nombre, correoInvalido);
        });

        String expectedMessage = "Correo inválido";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testActualizarClienteExistente() {
        ClienteRepository repo = new ClienteRepository();
        Cliente original = new Cliente("1", "Ana", "ana@gmail.com");
        repo.agregarCliente(original);

        Cliente actualizado = new Cliente("1", "Ana María", "ana123@gmail.com");
        repo.agregarCliente(actualizado); // sobrescribe

        Cliente obtenido = repo.obtenerCliente("1");
        assertEquals("Ana María", obtenido.getNombre());
        assertEquals("ana123@gmail.com", obtenido.getCorreo());
    }

    @Test
    void testEliminarClienteExistente() {
        ClienteRepository repo = new ClienteRepository();
        Cliente cliente = new Cliente("1", "Ana", "ana@gmail.com");
        repo.agregarCliente(cliente);

        repo.eliminarCliente("1");

        Cliente obtenido = repo.obtenerCliente("1");
        assertNull(obtenido);
    }

    @Test
    void testListarClientes() {
        ClienteRepository repo = new ClienteRepository();
        repo.agregarCliente(new Cliente("1", "Ana", "ana@gmail.com"));
        repo.agregarCliente(new Cliente("2", "Luis", "luis@gmail.com"));
        repo.agregarCliente(new Cliente("3", "Sofía", "sofia@gmail.com"));

        Collection<Cliente> clientes = repo.listarClientes();

        assertEquals(3, clientes.size());
        assertTrue(clientes.stream().anyMatch(c -> c.getNombre().equals("Ana")));
        assertTrue(clientes.stream().anyMatch(c -> c.getNombre().equals("Luis")));
        assertTrue(clientes.stream().anyMatch(c -> c.getNombre().equals("Sofía")));
    }
    @Test
    void testRegistrarCompraClienteBronce() {
        Cliente cliente = new Cliente("1", "Ana", "ana@gmail.com");
        Compra compra = new Compra("C1", "1", 850, "2025-06-25");

        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        clienteRepo.agregarCliente(cliente);

        CompraServices service = new CompraServices(compraRepo, clienteRepo);
        service.registrarCompra("1", compra);

        assertEquals(8, cliente.getPuntos());
        assertNotNull(compraRepo.obtenerCompra("C1"));
    }

    @Test
    void testRegistrarCompraClientePlata() {
        Cliente cliente = new Cliente("2", "Beto", "beto@gmail.com");
        cliente.sumarPuntos(600); // Nivel Plata

        Compra compra = new Compra("C2", "2", 1000, "2025-06-25");
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        clienteRepo.agregarCliente(cliente);

        CompraServices service = new CompraServices(compraRepo, clienteRepo);
        service.registrarCompra("2", compra);

        assertEquals(612, cliente.getPuntos()); // 600 + 12
        assertNotNull(compraRepo.obtenerCompra("C2"));
    }

    @Test
    void testRegistrarCompraClienteOro() {
        Cliente cliente = new Cliente("3", "Pedro", "pedro@example.com");
        cliente.sumarPuntos(1600); // Nivel Oro

        Compra compra = new Compra("C3", "3", 1000, "2025-06-26");
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        clienteRepo.agregarCliente(cliente);

        CompraServices service = new CompraServices(compraRepo, clienteRepo);
        service.registrarCompra("3", compra);

        assertEquals(1615, cliente.getPuntos()); // 1600 + 15
    }

    @Test
    void testRegistrarCompraClientePlatino() {
        Cliente cliente = new Cliente("4", "Laura", "laura@example.com");
        cliente.sumarPuntos(3000); // Nivel Platino

        Compra compra = new Compra("C4", "4", 1000, "2025-06-26");
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        clienteRepo.agregarCliente(cliente);

        CompraServices service = new CompraServices(compraRepo, clienteRepo);
        service.registrarCompra("4", compra);

        assertEquals(3020, cliente.getPuntos()); // 3000 + 20
    }

    @Test
    void testBonificacionPorStreakTresComprasMismoDia() {
        Cliente cliente = new Cliente("5", "Luis", "luis@example.com");
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        clienteRepo.agregarCliente(cliente);

        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra c1 = new Compra("C51", "5", 100, "2025-06-26");
        Compra c2 = new Compra("C52", "5", 100, "2025-06-26");
        Compra c3 = new Compra("C53", "5", 100, "2025-06-26");

        service.registrarCompra("5", c1);
        service.registrarCompra("5", c2);
        service.registrarCompra("5", c3); // 3ra compra → bonus

        int puntosEsperados = (3 * (100 / 100)) + 10; // 3 puntos + 10 bonus
        assertEquals(puntosEsperados, cliente.getPuntos());
    }

    @Test
    void testRegistrarCompraClienteInexistente() {
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra compra = new Compra("C5", "999", 500, "2025-06-26");

        assertThrows(IllegalArgumentException.class, () -> {
            service.registrarCompra("999", compra);
        });
    }

    @Test
    void testListarComprasRegistradas() {
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        Cliente cliente = new Cliente("6", "Carlos", "carlos@example.com");
        clienteRepo.agregarCliente(cliente);

        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        Compra c1 = new Compra("C61", "6", 500, "2025-06-25");
        Compra c2 = new Compra("C62", "6", 400, "2025-06-26");

        service.registrarCompra("6", c1);
        service.registrarCompra("6", c2);

        List<Compra> compras = compraRepo.listarCompras().stream().toList();
        assertEquals(2, compras.size());
        assertTrue(compras.stream().anyMatch(c -> c.getIdCompra().equals("C61")));
        assertTrue(compras.stream().anyMatch(c -> c.getIdCompra().equals("C62")));
    }
    @Test
    void testAscensoANivelPlata() {
        Cliente cliente = new Cliente("10", "Mario", "mario@example.com");
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        clienteRepo.agregarCliente(cliente);
        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        for (int i = 0; i < 50; i++) {
            Compra compra = new Compra("CPL" + i, "10", 1000, "2025-06-26");
            service.registrarCompra("10", compra);
        }

        assertEquals("Plata", cliente.getNivel());
    }

    @Test
    void testAscensoANivelOro() {
        Cliente cliente = new Cliente("11", "Sofia", "sofia@example.com");
        cliente.sumarPuntos(600); // Parte en nivel Plata
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        clienteRepo.agregarCliente(cliente);
        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        int i = 0;
        while (!cliente.getNivel().equals("Oro")) {
            Compra compra = new Compra("CO" + i, "11", 1000, "2025-06-26");
            service.registrarCompra("11", compra);
            i++;
            if (i > 1000) fail("No se alcanzó nivel Oro tras 1000 compras"); // evita loop infinito
        }

        assertEquals("Oro", cliente.getNivel());
    }

    @Test
    void testAscensoANivelPlatino() {
        Cliente cliente = new Cliente("12", "Elena", "elena@example.com");
        cliente.sumarPuntos(2900); // ya es Oro
        CompraRepository compraRepo = new CompraRepository();
        ClienteRepository clienteRepo = new ClienteRepository();
        clienteRepo.agregarCliente(cliente);
        CompraServices service = new CompraServices(compraRepo, clienteRepo);

        for (int i = 0; i < 10; i++) {
            Compra compra = new Compra("CP" + i, "12", 1000, "2025-06-26");
            service.registrarCompra("12", compra);
        }

        assertEquals("Platino", cliente.getNivel());
    }

}