package com.notesapp.notes_api.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// Importa esto para permitir la conexión desde Angular
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/test")
// ¡CRUCIAL! Esto permite que localhost:4200 haga peticiones
@CrossOrigin(origins = "http://localhost:4200")
public class TestController {

    @GetMapping("/hello")
    public String sayHello() {
        return "{\"message\": \"¡Hola desde el Backend de Spring!\"}";
    }
}