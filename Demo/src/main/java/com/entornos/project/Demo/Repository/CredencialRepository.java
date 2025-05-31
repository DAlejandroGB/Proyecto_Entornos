package com.entornos.project.Demo.Repository;

import com.entornos.project.Demo.Model.Credencial;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredencialRepository extends JpaRepository<Credencial, Long> {
    Optional<Credencial> findByUsuarioNombre(String usuarioNombre);

    Credencial findByNombreUsuario(String nombreUsuario);
}