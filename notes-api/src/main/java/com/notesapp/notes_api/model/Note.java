package com.notesapp.notes_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Data
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob // Large Object, para textos largos
    @Column(nullable = false)
    private String content;

    private LocalDateTime creationDate;

    private boolean isPublic;

    // Relación: Muchas notas pertenecen a UN usuario (dueño)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "note_shared_with", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> sharedWith = new HashSet<>();

    // Esto lo implementaremos después
    // @ManyToMany
    // private Set<User> sharedWith = new HashSet<>();

    // Antes de guardar, asigna la fecha de creación
    @PrePersist
    public void onPrePersist() {
        creationDate = LocalDateTime.now();
    }
}
