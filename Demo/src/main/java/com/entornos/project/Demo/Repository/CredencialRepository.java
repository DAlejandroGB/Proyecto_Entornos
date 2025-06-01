package com.entornos.project.Demo.Repository;

import com.entornos.project.Demo.Model.Credencial;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CredencialRepository extends JpaRepository<Credencial, Long> {
    Credencial findByNombreUsuario(String usuarioNombre);
}