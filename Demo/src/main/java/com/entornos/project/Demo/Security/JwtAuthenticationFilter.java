package com.entornos.project.Demo.Security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.entornos.project.Demo.Service.impl.AuthService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public JwtAuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);

        if (token != null) {
            try {
                // Verificar el JWT
                DecodedJWT decodedJWT = authService.verificarToken(token);
                String username = decodedJWT.getSubject(); // El nombre de usuario dentro del token

                // Si el token es válido, establecer la autenticación en el contexto de seguridad
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        username, null, null); // Aquí solo pasamos el nombre de usuario, ya que no estamos usando contraseñas en el JWT
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();  // Si el token no es válido, limpiamos el contexto de seguridad
            }
        }

        // Continuar con el filtro de la cadena
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Extrae el token eliminando "Bearer "
        }else {
            return null; // Si no hay token, retornamos null
        }
    }
}