package org.example.desarrollo;

import java.util.Map;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        System.out.print("Introduce la subred de 24 bits (ej: 192.168.1 o 127.0.0): ");
        String subred = teclado.nextLine().trim();

        if (!subred.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")) {
            System.out.println("Error: El formato de la subred debe ser X.X.X (ejemplo: 192.168.0)");
            return;
        }

        System.out.println("Descargando listado de servicios de IANA...");
        Map<Integer, String> diccionario = LectorServicios.obtenerDiccionarioPuertos();
        System.out.println("Diccionario cargado correctamente. Escaneando red en paralelo...");

        EscanearIP[] hilos = new EscanearIP[254];

        for (int i = 0; i < 254; i++) {
            String ipCompleta = subred + "." + (i + 1);
            hilos[i] = new EscanearIP(ipCompleta, diccionario);
            hilos[i].start();
        }

        for (int i = 0; i < 254; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                System.out.println("Error esperando hilos.");
            }
        }

        System.out.println("Escaneo de red finalizado por completo.");
    }
}
