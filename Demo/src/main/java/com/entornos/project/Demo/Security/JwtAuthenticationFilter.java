package com.entornos.project.Demo.Security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.entornos.project.Demo.Repository.CredencialRepository;
import com.entornos.project.Demo.Repository.UsuarioRepository;
import com.entornos.project.Demo.Service.impl.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private CredencialRepository credencialRepository;

    public JwtAuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, ServletException, IOException {
        String token = extractTokenFromRequest(request);

        if (token != null) {
            try {
                // Verificar el JWT
                DecodedJWT decodedJWT = authService.verificarToken(token);
                String username = decodedJWT.getSubject();// El nombre de usuario dentro del token

                UserDetails usuario = this.credencialRepository.findByNombreUsuario(username);

                var authentication = new UsernamePasswordAuthenticationToken(usuario, null,
                        usuario.getAuthorities()); //Forzamos un inicio de sesion
                SecurityContextHolder.getContext().setAuthentication(authentication);
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

    @Autowired
    public void setCredencialRepository(CredencialRepository credencialRepository) {
        this.credencialRepository = credencialRepository;
    }
}