package com.notesapp.notes_api.security;


// Importaciones del modelo y repositorio
import com.notesapp.notes_api.model.User;
import com.notesapp.notes_api.repository.UserRepository;

// Importaciones de Spring Security
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Importación de utilidades de Java
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Este método es llamado por Spring Security cuando un usuario intenta autenticarse.
     * "username" en nuestro caso es el "email".
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscar al usuario en nuestra base de datos
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró usuario con el email: " + email));

        // 2. Convertir nuestro User a UserDetails de Spring Security
        // Le pasamos el email, la contraseña (ya cifrada) y sus roles
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}