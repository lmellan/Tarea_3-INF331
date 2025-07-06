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
            System.out.println("\n======= Sistema de Fidelidad ========\n");
            System.out.println("1. Gestión de Clientes");
            System.out.println("2. Gestión de Compras");
            System.out.println("3. Mostrar Puntos / Nivel de un Cliente");
            System.out.println("4. Salir");
            System.out.print("\nSeleccione una opción: ");

            int opcion = leerEntero(scanner, 1, 4);

            switch (opcion) {
                case 1 -> gestionClientes(scanner, clienteRepo);
                case 2 -> gestionCompras(scanner, clienteRepo, compraRepo, compraServices);
                case 3 -> {
                    System.out.print("\n");
                    System.out.print("Ingrese ID del cliente: ");
                    int id = leerEntero(scanner, 0, Integer.MAX_VALUE);
                    System.out.print("\n");
                    Cliente c = clienteRepo.obtenerCliente(id);
                    if (c != null) {
                        System.out.println("Nombre: " + c.getNombre());
                        System.out.println("Correo: " + c.getCorreo());
                        System.out.println("Puntos: " + c.getPuntos());
                        System.out.println("Nivel: " + c.getNivel());
                    } else {
                        System.out.println("Cliente no encontrado.");
                    }
                }
                case 4 -> continuar = false;
                default -> System.out.println("Opción inválida.");
            }

            System.out.println();
        }

        System.out.println("Hasta la próxima!");
        scanner.close();
    }

    private static void gestionClientes(Scanner scanner, ClienteRepository clienteRepo) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n======== Gestión de Clientes =========\n");
            System.out.println("1. Agregar Cliente");
            System.out.println("2. Eliminar Cliente");
            System.out.println("3. Ver Detalle de Cliente");
            System.out.println("4. Editar Cliente");
            System.out.println("5. Listar Clientes");
            System.out.println("6. Volver");
            System.out.print("\nSeleccione una opción: ");

            int op = leerEntero(scanner, 1, 6);

            System.out.print("\n");

            switch (op) {
                case 1 -> {
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Correo: ");
                    String correo = scanner.nextLine();
                    try {
                        Cliente nuevo = new Cliente(nombre, correo);
                        clienteRepo.agregarCliente(nuevo);
                        System.out.println("\nCliente agregado exitosamente. ID: " + nuevo.getId());
                    } catch (Exception e) {
                        System.out.println("\nError: " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("ID del cliente: ");
                    int id = leerEntero(scanner, 0, Integer.MAX_VALUE);
                    Cliente cliente = clienteRepo.obtenerCliente(id);
                    if (cliente != null) {
                        clienteRepo.eliminarCliente(id);
                        System.out.println("\nCliente eliminado exitosamente.");
                    } else {
                        System.out.println("No se encontró un cliente con ese ID.");
                    }
                }

                case 3 -> {
                    System.out.print("ID del cliente: ");
                    int id = leerEntero(scanner, 0, Integer.MAX_VALUE);
                    Cliente c = clienteRepo.obtenerCliente(id);
                     System.out.print("\n");
                    if (c != null) {
                        System.out.println("Nombre: " + c.getNombre());
                        System.out.println("Correo: " + c.getCorreo());
                        System.out.println("Puntos: " + c.getPuntos());
                        System.out.println("Nivel: " + c.getNivel());
                    } else {
                        System.out.println("\nCliente no encontrado.");
                    }
                }
                case 4 -> {
                    System.out.print("ID del cliente: ");
                    int id = leerEntero(scanner, 0, Integer.MAX_VALUE);
                    Cliente c = clienteRepo.obtenerCliente(id);
                    if (c != null) {
                        System.out.println("Nombre actual: " + c.getNombre());
                        System.out.print("Nuevo nombre (dejar vacío para no modificar): ");
                        String nuevoNombre = scanner.nextLine();
                        if (nuevoNombre.isBlank()) {
                            nuevoNombre = c.getNombre();
                        }

                        System.out.println("Correo actual: " + c.getCorreo());
                        System.out.print("Nuevo correo (dejar vacío para no modificar): ");
                        String nuevoCorreo = scanner.nextLine();
                        if (nuevoCorreo.isBlank()) {
                            nuevoCorreo = c.getCorreo();
                        }

                        clienteRepo.actualizarDatosCliente(id, nuevoNombre, nuevoCorreo);
                        System.out.println("\nDatos actualizados.");
                    } else {
                        System.out.println("Cliente no encontrado.");
                    }
                }

                case 5 -> {
                    var lista = clienteRepo.listarClientes();
                    if (lista.isEmpty()) {
                        System.out.println("No hay clientes registrados.");
                    } else {
                        for (Cliente c : lista) {
                            System.out.println("ID: " + c.getId() + " | " + c.getNombre() + " (" + c.getCorreo() + ")");
                        }
                    }
                }

                case 6 -> volver = true;
            }
            System.out.println();
        }
    }

    private static void gestionCompras(Scanner scanner, ClienteRepository clienteRepo, CompraRepository compraRepo, CompraServices compraServices) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n======= Gestión de Compras ========\n");
            System.out.println("1. Agregar Compra");
            System.out.println("2. Ver Detalle de Compra");
            System.out.println("3. Listar Compras");
            System.out.println("4. Volver");
            System.out.print("\nSeleccione una opción: ");

            int op = leerEntero(scanner, 1, 4);

            System.out.print("\n");

            switch (op) {
                case 1 -> {
                    System.out.print("ID del cliente: ");
                    int id = leerEntero(scanner, 0, Integer.MAX_VALUE);

                    Cliente c = clienteRepo.obtenerCliente(id);
                    if (c == null) {
                        System.out.println("\nCliente no encontrado.");
                        break;
                    }

                    System.out.print("Monto: ");
                    long monto = leerLong(scanner);
                    System.out.print("Fecha (yyyy-MM-dd): ");
                    String fecha = scanner.nextLine();

                    Compra compra = new Compra(id, monto, fecha);
                    compraServices.registrarCompra(id, compra);
                    System.out.println("\nCompra registrada exitosamente. ID: " + compra.getId());
                }
                case 2 -> {
                    System.out.print("ID de la compra: ");
                    int id = leerEntero(scanner, 0, Integer.MAX_VALUE);
                    Compra c = compraRepo.obtenerCompra(id);
                    System.out.print("\n");
                    if (c != null) {
                        System.out.println("ID Cliente: " + c.getIdCliente());
                        System.out.println("Monto: " + c.getMonto());
                        System.out.println("Fecha: " + c.getFecha());
                    } else {
                        System.out.println("Compra no encontrada.");
                    }
                }
                case 3 -> {
                    for (Compra c : compraRepo.listarCompras()) {
                        System.out.println("ID: " + c.getId() + " | Cliente ID: " + c.getIdCliente() + " | $" + c.getMonto() + " | Fecha: " + c.getFecha());
                    }
                }
                case 4 -> volver = true;
            }
            System.out.println();
        }
    }

    private static int leerEntero(Scanner scanner, int minimo, int maximo) {
        while (true) {
            String entrada = scanner.nextLine();
            try {
                int valor = Integer.parseInt(entrada);
                if (valor < minimo || valor > maximo) {
                    System.out.print("Número fuera de rango [" + minimo + "-" + maximo + "]. Intente de nuevo: ");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingrese un número entero: ");
            }
        }
    }

    private static long leerLong(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine();
            try {
                return Long.parseLong(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingrese un número válido: ");
            }
        }
    }
}
