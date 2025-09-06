package com.playlist_backend.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {

    @Value("${security.default-passwords.admin}")
    private String adminPassword;

    @Value("${security.default-passwords.alejo}")
    private String alejoPassword;

    public static void main(String[] args) {
        var context = SpringApplication.run(PasswordGenerator.class, args);
        PasswordGenerator generator = context.getBean(PasswordGenerator.class);
        generator.generateHashes();
    }

    public void generateHashes() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        System.out.println("Hash admin: " + encoder.encode(adminPassword));
        System.out.println("Hash alejo: " + encoder.encode(alejoPassword));
    }
}
