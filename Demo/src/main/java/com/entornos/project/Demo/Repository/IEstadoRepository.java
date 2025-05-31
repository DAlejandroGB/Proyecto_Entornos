package com.entornos.project.Demo.Repository;

import com.entornos.project.Demo.Model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadoRepository extends JpaRepository<Estado, Long> {
    Estado findByNombre(String nombre);
}
