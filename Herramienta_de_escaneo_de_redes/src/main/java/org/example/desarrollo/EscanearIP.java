package org.example.desarrollo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EscanearIP extends Thread{
    private static final int MAX_PUERTO = 1000; // Constante solicitada
    private static final int TIMEOUT_MS = 200;  // Tiempo maximo para esperar al puerto

    private String ip;
    private Map<Integer, String> diccionario;

    public EscanearIP(String ip, Map<Integer, String> diccionario) {
        this.ip = ip;
        this.diccionario = diccionario;
    }

    @Override
    public void run() {
        if (estaActiva(this.ip)) {
            List<String> puertosAbiertos = new ArrayList<>();

            // Escaneamos los puertos en rango del 0 al MAX_PUERTO
            for (int puerto = 1; puerto <= MAX_PUERTO; puerto++) {
                // Intentamos abrir un socket TCP contra esa IP y puerto
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(this.ip, puerto), TIMEOUT_MS);

                    // Si no salta excepcion, es que esta ABIERTO. Buscamos el servicio asociado
                    String servicio = diccionario.getOrDefault(puerto, "unknown");
                    puertosAbiertos.add("   Puerto " + puerto + " ABIERTO: " + servicio);
                } catch (IOException e) {
                    // Si da error de conexion, el puerto esta cerrado (no hacemos nada)
                }
            }

            // Mostramos los resultados si se ha encontrado la IP activa como pide el enunciado
            synchronized (System.out) {
                System.out.println("IP " + this.ip + " ACTIVA (Puertos abiertos: " + puertosAbiertos.size() + ")");
                for (String linea : puertosAbiertos) {
                    System.out.println(linea);
                }
            }
        }
    }

    // Comprobacion de si la IP responde a Ping (Criterios 2 y 3 de la lista)
    private boolean estaActiva(String ipPrueba) {
        String sOperativo = System.getProperty("os.name").toLowerCase();
        ProcessBuilder pb;

        // Cambiamos los argumentos segun sea Windows o Linux/Mac
        if (sOperativo.contains("win")) {
            // -n 1 (un unico paquete), -w 500 (esperar maximo 500ms)
            pb = new ProcessBuilder("ping", "-n", "1", "-w", "500", ipPrueba);
        } else {
            // -c 1 (un unico paquete), -W 1 (esperar maximo 1 segundo en linux)
            pb = new ProcessBuilder("ping", "-c", "1", "-W", "1", ipPrueba);
        }

        try {
            Process proceso = pb.start();
            int codigoSalida = proceso.waitFor();
            // Si el codigo de salida es 0, es que el host esta arriba
            return codigoSalida == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
