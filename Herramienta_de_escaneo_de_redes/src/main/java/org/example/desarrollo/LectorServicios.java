package org.example.desarrollo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LectorServicios {
    private static final String URL_TEXTO = "http://ftp.sun.ac.za/ftp/pub/documentation/security/port-numbers.txt";

    public static Map<Integer, String> obtenerDiccionarioPuertos() {
        Map<Integer, String> mapa = new HashMap<>();
        try {
            URL url = new URL(URL_TEXTO);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.contains("tcp") && !linea.startsWith("#")) {
                    String[] partes = linea.trim().split("\\s+");
                    if (partes.length >= 2) {
                        try {
                            String nombre = partes[0];
                            String puertoTexto = partes[1].split("/")[0];
                            int puerto = Integer.parseInt(puertoTexto);

                            mapa.putIfAbsent(puerto, nombre);
                        } catch (Exception e) {

                        }
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("No se pudo descargar el diccionario de puertos, se mostraran vacios.");
        }
        return mapa;
    }
}
