package com.notesapp.notes_api.repository;


import com.notesapp.notes_api.model.Note;
import com.notesapp.notes_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // Encontrar todas las notas que pertenecen a un usuario específico
    List<Note> findByOwner(User owner);

    // Encontrar todas las notas que son públicas
    List<Note> findByIsPublicTrue();

    // Encontrar todas las notas que han sido compartidas con un usuario específico
    List<Note> findBySharedWithContains(User user);
}