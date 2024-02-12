package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.time.*;

public class LaunchClient {
    private static final String RUTA_CLAVES_SSH = System.getProperty("user.home") + "/.ssh";
    private static final String RUTA_PUB = RUTA_CLAVES_SSH + "/id_rsa.pub";
    private static final String RUTA_PRIV = RUTA_CLAVES_SSH + "/id_rsa";

    public static void main(String[] args) {
        String ip = "10.17.0.145";
        int puerto = 8080;

        try {
            if (!clavesExisten() || !claveNoCaducada()) {
                System.out.println("No existe clave anterior");
                enviarClavePublicaPorTCP(ip, puerto);
            } else {
                System.out.println("Conectando");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean clavesExisten() {
        Path rutaClavePublica = Paths.get(RUTA_PUB);
        Path rutaClavePrivada = Paths.get(RUTA_PRIV);
        return Files.exists(rutaClavePublica) && Files.exists(rutaClavePrivada);
    }

    private static boolean claveNoCaducada() {
        return false;
    }

    private static void enviarClavePublicaPorTCP(String ip, int puerto) {
        try (Socket socketCliente = new Socket(ip, puerto);
             OutputStream out = socketCliente.getOutputStream()) {
            // Leer el archivo de la clave pública en un array de bytes
            byte[] clavePublicaBytes = Files.readAllBytes(Paths.get(RUTA_PUB));
            // Enviar la clave pública
            out.write(clavePublicaBytes);
            // Hacer flush explícitamente para asegurar que todos los datos se envían
            out.flush();
            System.out.println("Clave pública enviada al servidor mediante TCP.");
        } catch (IOException e) {
            System.err.println("Error al enviar la clave pública: " + e.getMessage());
        }
    }
}
