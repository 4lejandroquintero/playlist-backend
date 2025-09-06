package com.playlist_backend.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String adminPassword = "admin123";
        String alejoPassword = "alejo123";

        // Genera los hashes
        String adminHash = encoder.encode(adminPassword);
        String alejoHash = encoder.encode(alejoPassword);

        // Imprime solo los hashes
        System.out.println("Hash para admin: " + adminHash);
        System.out.println("Hash para alejo: " + alejoHash);
    }
}
