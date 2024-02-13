package org.example;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;
import com.jcraft.jsch.Session;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.time.*;
import java.util.Arrays;
import java.util.Set;

public class LaunchClient {
    private static final String RUTA_CLAVES_SSH = System.getProperty("user.home") + "/.ssh";
    private static final String RUTA_PUB = RUTA_CLAVES_SSH + "/id_rsa.pub";
    private static final String RUTA_PRIV = RUTA_CLAVES_SSH + "/id_rsa";

    public static void main(String[] args) {
        String ip = "10.18.0.170";
        int puerto = 8080;

        try {
            if (!clavesExisten() || claveCaducada()) {
                System.out.println("Primera conexion, generando claves");
                generarClavesSSH();
            }
            enviarClavePublicaPorTCP(ip, puerto);
            System.out.println("Conectando mediante SSH...");
            conectarSSH(ip, "usuario", RUTA_PRIV);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean clavesExisten() {
        Path rutaClavePublica = Paths.get(RUTA_PUB);
        Path rutaClavePrivada = Paths.get(RUTA_PRIV);
        return Files.exists(rutaClavePublica) && Files.exists(rutaClavePrivada);
    }

    private static boolean claveCaducada() {
        return false;
    }

    private static void enviarClavePublicaPorTCP(String ip, int puerto) {
        try (Socket socketCliente = new Socket(ip, puerto);
             OutputStream out = socketCliente.getOutputStream()) {
            byte[] clavePublicaBytes = Files.readAllBytes(Paths.get(RUTA_PUB));
            out.write(clavePublicaBytes);
            out.flush();
            System.out.println("Clave pública enviada al servidor mediante TCP");
        } catch (IOException e) {
            System.err.println("Error al enviar la clave pública: " + e.getMessage());
        }
    }

    private static void generarClavesSSH() {
        try {
            JSch jsch = new JSch();
            KeyPair keyPair = KeyPair.genKeyPair(jsch, KeyPair.RSA, 2048);
            keyPair.writePrivateKey(RUTA_PRIV);
            keyPair.writePublicKey(RUTA_PUB, "usuario@dominio.com");
            keyPair.dispose();

            Set<PosixFilePermission> permisos = Set.of(PosixFilePermission.OWNER_READ,PosixFilePermission.OWNER_WRITE);
            Files.setPosixFilePermissions(Paths.get(RUTA_PRIV),permisos);
            System.out.println("Claves generadas con permisos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void conectarSSH(String hostname, String username, String rutaClavePrivada) {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(rutaClavePrivada);
            Session session = jsch.getSession(username, hostname, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Iniciando conexión SSH...");
            session.connect();
            System.out.println("Conexión SSH establecida.");
            session.disconnect();
        } catch (Exception e) {
            System.err.println("Error al conectar mediante SSH: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
