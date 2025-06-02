package com.entornos.project.Demo.Security;

import com.entornos.project.Demo.Service.impl.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;// Asegúrate de que el authService sea inyectado

    // Inyección de dependencias a través del constructor
    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)  // Deshabilitamos CSRF (útil para JWT)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/credencial/login", "/usuarios/list","/credencial","/usuarios").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()// Rutas públicas
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/*").permitAll()
                        .anyRequest().authenticated()  // Rutas protegidas
                )
                .addFilterBefore(new JwtAuthenticationFilter(authService), UsernamePasswordAuthenticationFilter.class);  // Filtro JWT

        return http.build(); // Retornamos la configuración de seguridad
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}