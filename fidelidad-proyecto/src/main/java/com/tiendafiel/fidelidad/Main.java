package com.tiendafiel.fidelidad;
import com.tiendafiel.fidelidad.models.Cliente;
import com.tiendafiel.fidelidad.models.Compra;
import com.tiendafiel.fidelidad.repositories.ClienteRepository;
import com.tiendafiel.fidelidad.repositories.CompraRepository;
import com.tiendafiel.fidelidad.services.CompraServices;


import java.io.InputStream;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ejecutarPrograma(System.in);
    }

    public static void ejecutarPrograma(InputStream inputStream) {
    Scanner scanner = new Scanner(inputStream);
    ClienteRepository clienteRepo = new ClienteRepository();
    CompraRepository compraRepo = new CompraRepository();
    CompraServices compraServices = new CompraServices(compraRepo, clienteRepo);

    boolean continuar = true;

    while (continuar) {
        System.out.println("=== Programa de Fidelidad ===");
        System.out.println("1. Agregar Cliente");
        System.out.println("2. Registrar Compra");
        System.out.println("3. Ver puntos y nivel de un Cliente");
        System.out.println("4. Salir");
        System.out.print("Seleccione una opción: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Entrada no válida.");
            break;
        }

        int opcion = scanner.nextInt();
        scanner.nextLine(); // limpiar el salto de línea

        switch (opcion) {
            case 1:
                System.out.print("Ingrese ID del cliente: ");
                String id = scanner.nextLine();
                System.out.print("Ingrese nombre: ");
                String nombre = scanner.nextLine();
                System.out.print("Ingrese correo: ");
                String correo = scanner.nextLine();

                if (!correo.contains("@")) {
                    System.out.println("Correo inválido. Debe contener '@'.");
                } else if (clienteRepo.obtenerCliente(id) != null) {
                    System.out.println("Ya existe un cliente con ese ID.");
                } else {
                    Cliente nuevo = new Cliente(id, nombre, correo);
                    clienteRepo.agregarCliente(nuevo);
                    System.out.println("Cliente agregado exitosamente.");
                }
                break;

            case 2:
                System.out.print("Ingrese ID del cliente: ");
                String idCliente = scanner.nextLine();
                Cliente cliente = clienteRepo.obtenerCliente(idCliente);
                if (cliente == null) {
                    System.out.println("Cliente no encontrado.");
                    break;
                }

                System.out.print("Ingrese ID de la compra: ");
                String idCompra = scanner.nextLine();
                System.out.print("Ingrese monto de la compra (entero): ");
                long monto = scanner.nextInt();
                scanner.nextLine(); // limpiar salto

                System.out.print("Ingrese fecha (yyyy-MM-dd): ");
                String fecha = scanner.nextLine();

                Compra compra = new Compra(idCompra, idCliente, monto, fecha);
                compraServices.registrarCompra(idCliente, compra);
                System.out.println("Compra registrada exitosamente.");
                break;

            case 3:
                System.out.print("Ingrese ID del cliente: ");
                String idConsulta = scanner.nextLine();
                Cliente consultado = clienteRepo.obtenerCliente(idConsulta);
                if (consultado == null) {
                    System.out.println("Cliente no encontrado.");
                } else {
                    System.out.println("Nombre: " + consultado.getNombre());
                    System.out.println("Puntos: " + consultado.getPuntos());
                    System.out.println("Nivel: " + consultado.getNivel());
                }
                break;

            case 4:
                continuar = false;
                break;

            default:
                System.out.println("Opción inválida.");
        }

        System.out.println();
    }

    System.out.println("Hasta la próxima!");
    scanner.close();
}

}
