package com.notesapp.notes_api.dto;



import lombok.Data;

@Data
public class RegisterDto {
    private String email;
    private String password;
    // No incluimos 'role' para que un usuario no pueda registrarse como ADMIN
}

