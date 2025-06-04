package com.entornos.project.Demo.Service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.entornos.project.Demo.Model.Credencial;
import com.entornos.project.Demo.Repository.CredencialRepository;
import com.entornos.project.Demo.Service.interfaces.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
public class AuthService implements IAuthService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private CredencialRepository credencialRepository;
    private PasswordEncoder passwordEncoder;


    public String login(String usuarioNombre, String contrasena) {
        Credencial credencial = credencialRepository.findCredencialByNombreUsuario(usuarioNombre);
        String password = this.passwordEncoder.encode(contrasena);
        if (!password.equals(credencial.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        String token = generarJWT(credencial);
        return token;
    }

    public String generarJWT(Credencial credencial) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        Instant now = Instant.now();
        Date expire = Date.from(now.plus(1, ChronoUnit.DAYS));

        return JWT.create()
                .withSubject(credencial.getNombreUsuario())
                .withIssuer("auth0")
                .withIssuedAt(Date.from(now))
                .withExpiresAt(expire)
                .sign(algorithm);
    }

    public DecodedJWT verificarToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        return JWT.require(algorithm)
                .withIssuer("auth0")
                .build()
                .verify(token);

    }

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setCredencialRepository(CredencialRepository credencialRepository) {
        this.credencialRepository = credencialRepository;
    }
}
