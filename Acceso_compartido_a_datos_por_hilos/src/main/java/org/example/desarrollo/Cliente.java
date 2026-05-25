package org.example.desarrollo;

import java.util.Random;

public class Cliente extends Thread{
    private static final int MAX_OPERACIONES = 5;
    private static final double MAX_CANTIDAD = 200.0;
    private static final int MAX_PAUSA = 500;

    private CuentaBancaria cuenta;
    private String nombre;
    private Random random;

    public Cliente(String nombre, CuentaBancaria cuenta) {
        this.nombre = nombre;
        this.cuenta = cuenta;
        this.random = new Random();
    }

    @Override
    public void run() {
        int numOperaciones = random.nextInt(MAX_OPERACIONES) + 1;

        for (int i = 0; i < numOperaciones; i++) {
            int tipo = random.nextInt(2);
            double cantidad = random.nextDouble() * MAX_CANTIDAD;

            cantidad = ((int)(cantidad * 100)) / 100.0;

            if (tipo == 0) {
                cuenta.retirar(cantidad, nombre);
            } else {
                cuenta.depositar(cantidad, nombre);
            }
            try {
                int pausa = random.nextInt(MAX_PAUSA);
                Thread.sleep(pausa);
            } catch (InterruptedException e) {
                System.out.println("Error en el hilo " + nombre);
            }
        }
        System.out.println("Terminado: " + nombre + " ya ha hecho todas sus operaciones.");
    }
}
