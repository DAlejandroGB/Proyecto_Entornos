package com.entornos.project.Demo.Repository;

import com.entornos.project.Demo.Model.Credencial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface CredencialRepository extends JpaRepository<Credencial, Long> {
    Credencial findCredencialByNombreUsuario(String usuarioNombre);
    @Query("SELECT c.usuario.rol.rol FROM Credencial c WHERE c.nombreUsuario =:nombreUsuario")
    String getRolByUsername(String nombreUsuario);

    UserDetails findByNombreUsuario(String username);
}