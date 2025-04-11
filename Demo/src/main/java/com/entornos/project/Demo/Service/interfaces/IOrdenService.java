package com.entornos.project.Demo.Service.interfaces;

import com.entornos.project.Demo.dto.OrdenDTO;
import com.entornos.project.Demo.dto.OrdenMedicamentoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IOrdenService {
    OrdenDTO createOrden(Long idUsuario);

    OrdenMedicamentoDTO addMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO, MultipartFile imagen) throws IOException;

    void deleteMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO);

    OrdenDTO getOrdenPendiente(Long idUsuario);

    Page<OrdenDTO> getAllOrdenesByUsuario(Long idUsuario, Pageable pageable);

    OrdenDTO getOrden(Long idOrden);
}
