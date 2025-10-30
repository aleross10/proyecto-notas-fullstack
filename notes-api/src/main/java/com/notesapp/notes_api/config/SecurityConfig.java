package com.notesapp.notes_api.config;

// 1. IMPORTA LAS CLASES NUEVAS
import com.notesapp.notes_api.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // <-- Importante
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // <-- Importante

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 2. INYECTA TU FILTRO
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // Rutas públicas de API
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        // Rutas protegidas de API
                        .requestMatchers("/api/**").authenticated()
                        // Rutas del Frontend (Angular)
                        .anyRequest().permitAll()
                )
                // 3. GESTIÓN DE SESIÓN STATELESS
                // Le dice a Spring Security que no cree sesiones, porque usaremos JWT.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 4. AÑADE EL FILTRO JWT
                // Le decimos a Spring que use nuestro 'jwtAuthFilter' ANTES
                // del filtro de login estándar.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // (Tus @Beans para PasswordEncoder y AuthenticationManager se quedan exactamente igual)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}