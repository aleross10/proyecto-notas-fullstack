package com.notesapp.notes_api.dto;


import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
