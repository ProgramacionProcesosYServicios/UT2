package org.example.desarrollo;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        CuentaBancaria cuentaCompartida = new CuentaBancaria();

        System.out.print("Introduce el numero de clientes: ");
        int numClientes = 0;

        try {
            numClientes = teclado.nextInt();
            if (numClientes <= 0) {
                System.out.println("Numero no valido. Tiene que ser mayor que 0.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error: No has introducido un numero entero.");
            return;
        }

        Cliente[] clientes = new Cliente[numClientes];

        System.out.println("Empiezan las operaciones de los hilos...");

        for (int i = 0; i < numClientes; i++) {
            clientes[i] = new Cliente("Cliente-" + (i + 1), cuentaCompartida);
            clientes[i].start();
        }

        for (int i = 0; i < numClientes; i++) {
            try {
                clientes[i].join();
            } catch (InterruptedException e) {
                System.out.println("Error esperando a los hilos.");
            }
        }

        System.out.println("Todos los clientes han terminado.");
        System.out.println("Saldo final en la cuenta: " + cuentaCompartida.getSaldo() + " euros.");
    }
}
