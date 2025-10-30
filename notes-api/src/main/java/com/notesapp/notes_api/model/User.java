package com.notesapp.notes_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users") // Es buena práctica nombrar la tabla en plural
@Data // (Lombok) Genera getters, setters, toString, etc.
@NoArgsConstructor // (Lombok) Genera un constructor vacío
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role; // "ROLE_USER" o "ROLE_ADMIN"

    // Más adelante, añadiremos:
    // @OneToMany(mappedBy = "owner")
    // private List<Note> notes;
}

