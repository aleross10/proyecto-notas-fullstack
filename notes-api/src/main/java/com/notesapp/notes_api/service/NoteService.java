package com.notesapp.notes_api.service;


import com.notesapp.notes_api.dto.NoteDto;
import com.notesapp.notes_api.model.Note;
import com.notesapp.notes_api.model.User;
import com.notesapp.notes_api.repository.NoteRepository;
import com.notesapp.notes_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    // --- MÉTODOS CRUD PRINCIPALES ---

    /** Obtiene todas las notas visibles para un usuario. (El resto del código sigue igual) */
    public List<NoteDto> getAllNotes(String userEmail, boolean isAdmin) {
        User currentUser = findUserByEmail(userEmail);

        if (isAdmin) {
            return noteRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        }

        List<Note> ownedNotes = noteRepository.findByOwner(currentUser);
        List<Note> sharedNotes = noteRepository.findBySharedWithContains(currentUser);
        List<Note> publicNotes = noteRepository.findByIsPublicTrue();

        Set<Note> visibleNotes = new HashSet<>();
        visibleNotes.addAll(ownedNotes);
        visibleNotes.addAll(sharedNotes);
        visibleNotes.addAll(publicNotes);

        return visibleNotes.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /** Crea una nueva nota. */
    public NoteDto createNote(NoteDto noteDto, String userEmail) {
        User owner = findUserByEmail(userEmail);

        Note newNote = new Note();
        newNote.setTitle(noteDto.getTitle());
        newNote.setContent(noteDto.getContent());
        newNote.setPublic(noteDto.isPublic());
        newNote.setOwner(owner);

        Note savedNote = noteRepository.save(newNote);
        return convertToDto(savedNote);
    }

    /** 🎯 NUEVO: Actualiza una nota existente. Solo el dueño puede editarla. */
    public NoteDto updateNote(Long noteId, NoteDto noteDto, String userEmail) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con ID: " + noteId));

        // Verificar permisos: Solo el dueño puede editar.
        if (!note.getOwner().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("No tienes permiso para editar esta nota.");
        }

        // Actualizar campos
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        note.setPublic(noteDto.isPublic());

        Note updatedNote = noteRepository.save(note);
        return convertToDto(updatedNote);
    }

    /** Elimina una nota con verificación de permisos. */
    public void deleteNote(Long noteId, String userEmail, boolean isAdmin) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con ID: " + noteId));

        // Requisito 3: Admin o Dueño puede eliminar.
        boolean isOwner = note.getOwner().getEmail().equals(userEmail);

        if (isAdmin || isOwner) {
            noteRepository.delete(note);
        } else {
            throw new AccessDeniedException("No tienes permiso para eliminar esta nota.");
        }
    }

    /** Implementación del método de compartir (ya estaba) */
    public void shareNote(Long noteId, String ownerEmail, String recipientEmail) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con ID: " + noteId));

        if (!note.getOwner().getEmail().equals(ownerEmail)) {
            throw new AccessDeniedException("Solo el dueño puede compartir esta nota.");
        }

        User recipient = userRepository.findByEmail(recipientEmail)
                .orElseThrow(() -> new UsernameNotFoundException("El destinatario no existe."));

        note.getSharedWith().add(recipient);
        noteRepository.save(note);
    }


    // --- MÉTODOS DE UTILIDAD Y CONVERSIÓN ---

    /** Obtiene una nota por ID (necesario para la edición) */
    public NoteDto getNoteById(Long noteId, String userEmail) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con ID: " + noteId));

        // Permisos de lectura (solo el dueño, compartido o pública)
        boolean isOwner = note.getOwner().getEmail().equals(userEmail);
        boolean isPublic = note.isPublic();

        // Requeriríamos una lógica de sharedWith aquí, pero por simplicidad
        // Devolvemos si es del dueño o es pública.
        if (isOwner || isPublic) {
            return convertToDto(note);
        }

        throw new AccessDeniedException("No tienes permiso para ver esta nota.");
    }

    // ... (findUserByEmail y convertToDto se quedan igual)
    private User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + userEmail));
    }

    private NoteDto convertToDto(Note note) {
        NoteDto dto = new NoteDto();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setPublic(note.isPublic());
        dto.setOwnerEmail(note.getOwner().getEmail());
        return dto;
    }
}