package org.example;

import java.io.*;
import java.net.Socket;

public class LaunchClient {
    public static void main(String[] args) {
        // Ejemplo de dirección IP y puerto, deben ser ajustados según el entorno
        String ip = "10.17.0.145";
        int puerto = 8080;

        try {
            // Verificar si existen las claves SSH y si son válidas
            boolean keysExist = KeyManager.keysExist();
            boolean keyIsFresh = KeyManager.isKeyFresh();

            if (!keysExist || !keyIsFresh) {
                // Lógica para generar nuevas claves o manejar la situación
                KeyManager.generateNewKeys();
                // Envía la clave pública al servidor mediante TCP
                sendPublicKeyOverTCP(ip, puerto);
            } else {
                // Lógica para establecer una conexión SSH utilizando las claves existentes
                System.out.println("Conectando mediante SSH con las claves existentes...");
                // Aquí iría la implementación de la conexión SSH
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendPublicKeyOverTCP(String ip, int puerto) {
        try (Socket socketCliente = new Socket(ip, puerto);
             PrintWriter out = new PrintWriter(socketCliente.getOutputStream(), true)) {
            File publicKeyFile = new File(KeyManager.PUBLIC_KEY_PATH);
            String publicKey = new String(Files.readAllBytes(publicKeyFile.toPath()));
            out.println(publicKey);
            System.out.println("Clave pública enviada al servidor mediante TCP.");
        } catch (IOException e) {
            System.err.println("Error al enviar la clave pública: " + e.getMessage());
        }
    }
}
