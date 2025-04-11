package com.entornos.project.Demo.Service.interfaces;

import com.entornos.project.Demo.DTO.OrdenDTO;
import com.entornos.project.Demo.DTO.OrdenMedicamentoDTO;
import com.entornos.project.Demo.Model.Orden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IOrdenService {
    OrdenMedicamentoDTO addMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO, MultipartFile imagen, Long idUsuario) throws IOException;

    void deleteMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO);

    Orden getOrdenPendiente(Long idUsuario);

    Page<OrdenDTO> getAllOrdenesByUsuario(Long idUsuario, Pageable pageable);

    OrdenDTO getOrden(Long idOrden);
}
