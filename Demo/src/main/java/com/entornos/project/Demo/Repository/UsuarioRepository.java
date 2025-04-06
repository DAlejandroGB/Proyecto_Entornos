package com.entornos.project.Demo.Repository;

import com.entornos.project.Demo.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
