package com.entornos.project.Demo.Service.interfaces;

import com.entornos.project.Demo.DTO.OrdenDTO;
import com.entornos.project.Demo.DTO.OrdenMedicamentoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface IOrdenService {
    OrdenMedicamentoDTO addMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO, Long idUsuario) throws IOException;

    void deleteMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO);

    OrdenDTO getOrdenPendiente(Long idUsuario);

    Page<OrdenDTO> getAllOrdenesByUsuario(Long idUsuario, Pageable pageable);

    OrdenDTO getOrden(Long idOrden);
}
