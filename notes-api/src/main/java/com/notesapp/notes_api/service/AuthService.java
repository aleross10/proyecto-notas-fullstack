package com.notesapp.notes_api.service;


// Importaciones de los DTOs
import com.notesapp.notes_api.dto.LoginRequestDto;
import com.notesapp.notes_api.dto.LoginResponseDto;
import com.notesapp.notes_api.dto.RegisterDto; // <-- Esta es la que te falta

// Importaciones del modelo y repositorio
import com.notesapp.notes_api.model.User;
import com.notesapp.notes_api.repository.UserRepository;

// Importación de la utilidad JWT
import com.notesapp.notes_api.security.JwtUtil; // <-- Esta también te falta

// Importaciones de Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager; // <-- Esta también
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // <-- Esta también
import org.springframework.security.crypto.password.PasswordEncoder; // <-- Esta también
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. INYECTA LAS NUEVAS DEPENDENCIAS
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService; // (Este es nuestro CustomUserDetailsService)

    @Autowired
    private JwtUtil jwtUtil;

    public User registerUser(RegisterDto registerDto) {
        // 1. Verificar si el email ya existe
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            // En un proyecto real, lanzaríamos una excepción personalizada
            throw new RuntimeException("El email ya está en uso");
        }

        // 2. Crear el nuevo usuario
        User newUser = new User();
        newUser.setEmail(registerDto.getEmail());
        // 3. Cifrar la contraseña
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        // 4. Asignar rol por defecto
        newUser.setRole("ROLE_USER");

        // 5. Guardar en la base de datos y DEVOLVERLO
        return userRepository.save(newUser);
    }

    // 2. NUEVO MÉTODO DE LOGIN
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {

        // 3. Autentica al usuario
        // Esto usa el AuthenticationManager, que usa el AuthenticationProvider,
        // que usa nuestro CustomUserDetailsService y PasswordEncoder.
        // Si el email o contraseña son incorrectos, lanzará una excepción.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );

        // 4. Si la autenticación fue exitosa, carga los detalles del usuario
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.getEmail());

        // 5. Genera el token JWT
        final String jwt = jwtUtil.generateToken(userDetails);

        // 6. Devuelve el token en el DTO de respuesta
        return new LoginResponseDto(jwt);
    }
}