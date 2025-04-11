package com.entornos.project.Demo.Repository;

import com.entornos.project.Demo.Model.OrdenMedicamento;
import com.entornos.project.Demo.DTO.ItemMedicamentoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdenMedicamentoRepository extends JpaRepository<OrdenMedicamento, Long> {

    @Query("SELECT DISTINCT new com.entornos.project.Demo.DTO.ItemMedicamentoDTO(om.medicamento.nombre, om.medicamento.precio, om.medicamento.imagenMed) FROM OrdenMedicamento om" +
            " WHERE om.idOrden = :idOrden")
    List<ItemMedicamentoDTO> findAllByIdOrden(Long idOrden);

    @Query("SELECT om FROM OrdenMedicamento om WHERE om.idMedicamento =:idMedicamento")
    List<OrdenMedicamento> findAllByIdMedicamento(Long idMedicamento);

    Long id(Long id);

    @Query("SELECT om FROM OrdenMedicamento om WHERE om.idOrden =:idOrden and om.idMedicamento =:idMedicamento")
    OrdenMedicamento findMedicamentoByIdOrden(Long idOrden, Long idMedicamento);
}
