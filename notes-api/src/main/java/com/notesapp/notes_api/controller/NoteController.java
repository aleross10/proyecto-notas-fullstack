package com.notesapp.notes_api.controller;


import com.notesapp.notes_api.dto.NoteDto;
import com.notesapp.notes_api.dto.ShareNoteDto; // Importa DTO de compartir
import com.notesapp.notes_api.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    /**
     * GET /api/notes - Lista todas las notas visibles.
     */
    @GetMapping
    public ResponseEntity<List<NoteDto>> getAllNotes(Authentication authentication) {
        String userEmail = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<NoteDto> notes = noteService.getAllNotes(userEmail, isAdmin);
        return ResponseEntity.ok(notes);
    }

    /**
     * GET /api/notes/{id} - Obtiene una sola nota por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNoteById(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        NoteDto note = noteService.getNoteById(id, userEmail);
        return ResponseEntity.ok(note);
    }

    /**
     * POST /api/notes - Crea una nueva nota.
     */
    @PostMapping
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto noteDto, Authentication authentication) {
        String userEmail = authentication.getName();
        NoteDto createdNote = noteService.createNote(noteDto, userEmail);
        return ResponseEntity.ok(createdNote);
    }

    /**
     * 🎯 NUEVO: PUT /api/notes/{id} - Edita una nota existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> updateNote(
            @PathVariable Long id,
            @RequestBody NoteDto noteDto,
            Authentication authentication) {

        String userEmail = authentication.getName();
        NoteDto updatedNote = noteService.updateNote(id, noteDto, userEmail);
        return ResponseEntity.ok(updatedNote);
    }

    /**
     * DELETE /api/notes/{id} - Elimina una nota.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        noteService.deleteNote(id, userEmail, isAdmin);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/notes/{id}/share - Endpoint para compartir una nota.
     */
    @PostMapping("/{id}/share")
    public ResponseEntity<Void> shareNote(
            @PathVariable Long id,
            @RequestBody ShareNoteDto shareDto,
            Authentication authentication) {

        String ownerEmail = authentication.getName();

        noteService.shareNote(id, ownerEmail, shareDto.getRecipientEmail());

        return ResponseEntity.ok().build();
    }
}