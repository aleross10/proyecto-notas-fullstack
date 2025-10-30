package com.notesapp.notes_api.dto;


import lombok.Data;

@Data
public class ShareNoteDto {
    private String recipientEmail; // El email del usuario con quien compartir
}