package com.notesapp.notes_api.repository;

import com.notesapp.notes_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA crea la consulta automáticamente basado en el nombre del método
    // Necesitamos esto para el Login y para compartir notas
    Optional<User> findByEmail(String email);
}