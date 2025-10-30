package com.notesapp.notes_api.controller;

import com.notesapp.notes_api.dto.LoginRequestDto;
import com.notesapp.notes_api.dto.LoginResponseDto;
import com.notesapp.notes_api.dto.RegisterDto;
import com.notesapp.notes_api.model.User;
import com.notesapp.notes_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
// Ya no necesitamos @CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto) {
        try {
            User registeredUser = authService.registerUser(registerDto);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            LoginResponseDto response = authService.loginUser(loginRequestDto);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}
// Asegúrate de que esta sea la ÚLTIMA línea del archivo