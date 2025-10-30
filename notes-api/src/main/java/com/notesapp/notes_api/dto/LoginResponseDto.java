package com.notesapp.notes_api.dto;


import lombok.Data;

@Data
public class LoginResponseDto {
    private String jwt;

    public LoginResponseDto(String jwt) {
        this.jwt = jwt;
    }
}