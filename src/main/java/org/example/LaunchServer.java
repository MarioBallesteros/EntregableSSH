package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LaunchServer {
    public static void main(String[] args) {
        int puerto = 8080;
        String rutaClaves = System.getProperty("user.home") + "/.ssh/authorized_keys";
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor a la escucha en puerto: " + puerto);

            while (true) {
                try (Socket cliente = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()))) {
                    System.out.println("Cliente conectado desde: " + cliente.getInetAddress());
                    System.out.println("Clave publica:");
                    String publicKey = in.readLine();
                    if (publicKey != null && !publicKey.isEmpty()) {
                        System.out.println("Clave pub recibida y guardada");
                        guardarClavePublica(publicKey,rutaClaves);
                    }
                } catch (IOException e) {
                    System.err.println("Error al conectar: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void guardarClavePublica(String publicKey, String rutaClaves) {
        try {
            publicKey = publicKey + "\n";
            Path ruta = Paths.get(rutaClaves);
            Files.createDirectories(ruta.getParent());
            Files.write(ruta,publicKey.getBytes());
            System.out.println("Clave almacenada correctamente");
            System.out.println("almacenada en:");
            System.out.println(ruta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
