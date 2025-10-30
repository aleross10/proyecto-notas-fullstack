package com.notesapp.notes_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // <-- IMPORTA

@SpringBootApplication
@EnableMethodSecurity // <-- ¡AÑADE ESTA ANOTACIÓN!
public class NotesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesApiApplication.class, args);
    }
}
