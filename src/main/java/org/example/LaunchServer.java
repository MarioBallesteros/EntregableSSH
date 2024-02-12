package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class LaunchServer {
    public static void main(String[] args) {
        int puerto = 8080;

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
                    }
                } catch (IOException e) {
                    System.err.println("Error al conectar: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
