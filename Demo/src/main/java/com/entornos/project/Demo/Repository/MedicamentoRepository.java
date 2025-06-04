package com.entornos.project.Demo.Repository;

import com.entornos.project.Demo.Model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    @Query("SELECT m FROM Medicamento m WHERE m.activo IS TRUE")
    List<Medicamento> findAllMedicamentosActivos();
}