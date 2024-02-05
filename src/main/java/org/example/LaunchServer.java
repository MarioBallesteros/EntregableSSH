package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LaunchServer {
    public static void main(String[] args) {
        int puerto = 8080;

        try(ServerSocket serverSocket = new ServerSocket(puerto)){
            System.out.println("servidor a la escucha en:" + serverSocket.getInetAddress() +":"+ puerto);
            while (true){
                Socket cliente = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + cliente.getInetAddress());

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
