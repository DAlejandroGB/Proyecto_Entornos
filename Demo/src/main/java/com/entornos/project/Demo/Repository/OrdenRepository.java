package com.entornos.project.Demo.Repository;

import com.entornos.project.Demo.Model.Orden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    @Query("SELECT o FROM Orden o WHERE o.idUsuario =:idUsuario AND o.estado = 'pendiente' ")
    Orden findByIdUsuario(Long idUsuario);

    @Query("SELECT o FROM Orden o WHERE o.idUsuario =:idUsuario")
    Page<Orden> findAllByIdUsuario(Long idUsuario, Pageable pageable);

    Long id(Long id);
}
