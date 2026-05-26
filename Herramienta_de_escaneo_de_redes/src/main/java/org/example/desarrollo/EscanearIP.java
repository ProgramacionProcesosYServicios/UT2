package org.example.desarrollo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EscanearIP extends Thread{
    private static final int MAX_PUERTO = 1000;
    private static final int TIMEOUT_MS = 200;
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

            for (int puerto = 1; puerto <= MAX_PUERTO; puerto++) {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(this.ip, puerto), TIMEOUT_MS);

                    String servicio = diccionario.getOrDefault(puerto, "unknown");
                    puertosAbiertos.add("   Puerto " + puerto + " ABIERTO: " + servicio);
                } catch (IOException e) {
                }
            }

            synchronized (System.out) {
                System.out.println("IP " + this.ip + " ACTIVA (Puertos abiertos: " + puertosAbiertos.size() + ")");
                for (String linea : puertosAbiertos) {
                    System.out.println(linea);
                }
            }
        }
    }

    private boolean estaActiva(String ipPrueba) {
        String sOperativo = System.getProperty("os.name").toLowerCase();
        ProcessBuilder pb;

        if (sOperativo.contains("win")) {
            pb = new ProcessBuilder("ping", "-n", "1", "-w", "500", ipPrueba);
        } else {
            pb = new ProcessBuilder("ping", "-c", "1", "-W", "1", ipPrueba);
        }

        try {
            Process proceso = pb.start();
            int codigoSalida = proceso.waitFor();
            return codigoSalida == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
