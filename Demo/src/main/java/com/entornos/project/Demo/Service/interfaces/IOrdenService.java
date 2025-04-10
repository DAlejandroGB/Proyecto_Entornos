package com.entornos.project.Demo.Service.interfaces;

import com.entornos.project.Demo.dto.OrdenDTO;
import com.entornos.project.Demo.dto.OrdenMedicamentoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IOrdenService {
    OrdenDTO createOrden(Long idUsuario);

    OrdenMedicamentoDTO addMedicamento(Long idOrden, Long idMedicamento, Integer cantidad, MultipartFile imagen) throws IOException;

    List<OrdenDTO> getAllOrdenes();

    OrdenDTO getOrden(Long idOrden);
}
