package com.entornos.project.Demo.Repository;

import com.entornos.project.Demo.Model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
}