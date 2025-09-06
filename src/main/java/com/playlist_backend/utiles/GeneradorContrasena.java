package com.playlist_backend.utiles;

import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class GeneradorContrasena {

    @Value("${security.default-passwords.admin}")
    private String adminPassword;

    @Value("${security.default-passwords.alejo}")
    private String alejoPassword;

    public static void main(String[] args) {
        var context = SpringApplication.run(GeneradorContrasena.class, args);
        GeneradorContrasena generator = context.getBean(GeneradorContrasena.class);
        generator.generateHashes();
    }

    public void generateHashes() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        System.out.println("Hash admin: " + encoder.encode(adminPassword));
        System.out.println("Hash alejo: " + encoder.encode(alejoPassword));
    }
}
