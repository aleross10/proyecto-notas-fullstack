package com.notesapp.notes_api.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService; // (Este es nuestro CustomUserDetailsService)

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Obtener el header 'Authorization'
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Si no hay header o no empieza con "Bearer ", ignoramos y pasamos al siguiente filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token (quitando "Bearer ")
        jwt = authHeader.substring(7);

        // 4. Extraer el email (username) del token
        userEmail = jwtUtil.extractUsername(jwt);

        // 5. Validar el token
        // (Si hay email y el usuario aún no está autenticado en el contexto de seguridad)
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Si el token es válido...
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // 7. Creamos la autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        // ¡USAMOS userDetails.getAuthorities() AQUÍ!
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 8. ¡Guardamos la autenticación en el Contexto de Seguridad!
                // Spring Security ahora sabrá que este usuario está autenticado.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 9. Pasa al siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
}
