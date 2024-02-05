package org.example;

import java.io.IOException;
import java.net.Socket;

public class LaunchClient {
    public static void main(String[] args) {
        try {
        String ip = "10.17.0.145";
        int  puerto = 8080;
        Socket socketCliente = new Socket(ip,puerto);
        System.out.println(socketCliente.isConnected());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
