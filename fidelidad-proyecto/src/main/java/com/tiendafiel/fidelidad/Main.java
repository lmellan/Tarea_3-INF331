package com.tiendafiel.fidelidad;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("=== Programa de Fidelidad ===");
            System.out.println("1. Agregar Cliente");
            System.out.println("2. Registrar Compra");
            System.out.println("3. Ver puntos y nivel");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    System.out.println("Funcionalidad para agregar cliente");
                    break;
                case 2:
                    System.out.println("Funcionalidad para registrar compra");
                    break;
                case 3:
                    System.out.println("Funcionalidad para ver puntos y nivel");
                    break;
                case 4:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
            System.out.println();
        }

        System.out.println("¡Gracias por usar el sistema!");
        scanner.close();
    }
}
