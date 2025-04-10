package com.entornos.project.Demo.Repository;

import com.entornos.project.Demo.Model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
}
