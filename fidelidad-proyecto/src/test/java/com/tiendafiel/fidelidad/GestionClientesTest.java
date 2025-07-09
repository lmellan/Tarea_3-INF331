package com.tiendafiel.fidelidad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tiendafiel.fidelidad.models.Cliente;
import com.tiendafiel.fidelidad.models.Compra;
import com.tiendafiel.fidelidad.repositories.ClienteRepository;
import com.tiendafiel.fidelidad.repositories.CompraRepository;
import com.tiendafiel.fidelidad.services.ClienteServices;
import com.tiendafiel.fidelidad.services.CompraServices;


 
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GestionClientesTest {

    private ClienteRepository clienteRepo;
    private CompraRepository compraRepo;
    private ClienteServices clienteServices;

    @BeforeEach
    void setup() {
        clienteRepo = new ClienteRepository();
        compraRepo = new CompraRepository();
        clienteServices = new ClienteServices(clienteRepo, compraRepo);
    }


    private Cliente crearClienteConPuntos(String nombre, String correo, int puntos) {
    Cliente cliente = new Cliente(nombre, correo);
    cliente.sumarPuntos(puntos);
    clienteRepo.agregarCliente(cliente);
    return cliente;
    }

    private CompraServices crearCompraServices() {
        return new CompraServices(compraRepo, clienteRepo);
    }



    // tests de Gestión de Clientes


    @Test
    void testAgregarClienteValido() {
        Cliente cliente = new Cliente("Ana", "ana@gmail.com");
        clienteRepo.agregarCliente(cliente);

        Cliente obtenido = clienteRepo.obtenerCliente(cliente.getId());

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

        assertTrue(exception.getMessage().contains("Correo inválido"));
    }

    @Test
    void testActualizarClienteExistente() {
        Cliente cliente = new Cliente("Ana", "ana@gmail.com");
        clienteRepo.agregarCliente(cliente);

        clienteRepo.actualizarDatosCliente(cliente.getId(), "Ana María", "ana123@gmail.com");

        Cliente actualizado = clienteRepo.obtenerCliente(cliente.getId());
        assertEquals("Ana María", actualizado.getNombre());
        assertEquals("ana123@gmail.com", actualizado.getCorreo());
    }

    @Test
    void testEliminarClienteExistente() {
        Cliente cliente = new Cliente("Ana", "ana@gmail.com");
        clienteRepo.agregarCliente(cliente);

        clienteServices.eliminarClienteYCompras(cliente.getId());

        Cliente eliminado = clienteRepo.obtenerCliente(cliente.getId());
        assertNull(eliminado);
    }


    @Test
    void testListarClientes() {
        Cliente c1 = new Cliente("Ana", "ana@gmail.com");
        Cliente c2 = new Cliente("Luis", "luis@gmail.com");
        Cliente c3 = new Cliente("Sofía", "sofia@gmail.com");

        clienteRepo.agregarCliente(c1);
        clienteRepo.agregarCliente(c2);
        clienteRepo.agregarCliente(c3);

        Collection<Cliente> clientes = clienteRepo.listarClientes();

        assertEquals(3, clientes.size());
        assertTrue(clientes.stream().anyMatch(c -> c.getId() == c1.getId()));
        assertTrue(clientes.stream().anyMatch(c -> c.getId() == c2.getId()));
        assertTrue(clientes.stream().anyMatch(c -> c.getId() == c3.getId()));
    }

    
    // Se incorporan estos test sin usar TDD tras el análisis en SonarQube  

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
    void testGetIdClienteConClienteCreado() {
        Cliente cliente = new Cliente("Juan Pérez", "juan@example.com");

        Compra compra = new Compra(cliente.getId(), 5000, "2025-07-01");

        assertEquals(cliente.getId(), compra.getIdCliente());
    }

    // Implementado nuevas pruebas con TDD

    @Test
    void eliminarClienteYCompras_eliminaTodoCorrectamente() {
        Cliente cliente = crearClienteConPuntos("María", "maria@example.com", 0);
        CompraServices compraServices = crearCompraServices();

        Compra c1 = new Compra(cliente.getId(), 500, "2025-07-06");
        Compra c2 = new Compra(cliente.getId(), 400, "2025-07-06");

        compraServices.registrarCompra(cliente.getId(), c1);
        compraServices.registrarCompra(cliente.getId(), c2);

        assertNotNull(compraRepo.obtenerCompra(c1.getId()));
        assertNotNull(compraRepo.obtenerCompra(c2.getId()));

        clienteServices.eliminarClienteYCompras(cliente.getId());

        assertNull(clienteRepo.obtenerCliente(cliente.getId()));
        assertNull(compraRepo.obtenerCompra(c1.getId()));
        assertNull(compraRepo.obtenerCompra(c2.getId()));
    }

    @Test
    void eliminarClienteYCompras_clienteNoExiste_noHaceNada() {
        assertNull(clienteRepo.obtenerCliente(999));

        clienteServices.eliminarClienteYCompras(999);

        assertTrue(compraRepo.listarCompras().isEmpty());
        assertTrue(clienteRepo.listarClientes().isEmpty());
    }


}
