package com.notesapp.notes_api.dto;


import lombok.Data;

@Data
public class NoteDto {
    private Long id; // Útil para la edición
    private String title;
    private String content;
    private boolean isPublic;
    private String ownerEmail; // Para referencia del frontend
}