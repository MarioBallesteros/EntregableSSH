package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<PosixFilePermission> set = Set.of(PosixFilePermission.OWNER_WRITE,PosixFilePermission.OWNER_READ);
        try{
            Files.setPosixFilePermissions(Paths.get("/home/mballesterosv/prueba.txt"),set);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Hello world!");
    }
}