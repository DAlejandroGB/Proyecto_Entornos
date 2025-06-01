package com.entornos.project.Demo.Repository;

import com.entornos.project.Demo.DTO.OrdenDTO;
import com.entornos.project.Demo.Model.Orden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    @Query("SELECT o FROM Orden o WHERE o.idUsuario =:idUsuario AND o.idEstado =:idEstado ")
    List<Orden> findByIdUsuarioAndIdEstado(Long idUsuario, Long idEstado);

    @Query("SELECT o FROM Orden o WHERE o.idUsuario =:idUsuario")
    Page<Orden> findAllByIdUsuario(Long idUsuario, Pageable pageable);

    Long id(Long id);

    @Query("SELECT o FROM Orden o WHERE o.estado.nombre =:estado")
    Page<Orden> findAllByEstado(String estado, Pageable pageable);
}
