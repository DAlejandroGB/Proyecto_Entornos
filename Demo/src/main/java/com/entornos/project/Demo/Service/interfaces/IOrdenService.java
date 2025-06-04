package com.entornos.project.Demo.Service.interfaces;

import com.entornos.project.Demo.DTO.OrdenDTO;
import com.entornos.project.Demo.DTO.OrdenMedicamentoDTO;
import com.entornos.project.Demo.Model.Orden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IOrdenService {
    OrdenMedicamentoDTO addMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO, Long idUsuario);

    void deleteMedicamento(OrdenMedicamentoDTO ordenMedicamentoDTO);

    OrdenDTO getOrdenPendiente(Long idUsuario);

    Page<OrdenDTO> getAllOrdenesByUsuario(Long idUsuario, Pageable pageable);

    OrdenDTO getOrden(Long idOrden);

    Page<Orden> getAllOrdenesByEstado(String estado, Pageable pageable);

    Orden updateEstadoOrden(Long idOrden, String estado);

    OrdenDTO cargarReciboPago(Long idOrden, MultipartFile reciboPago) throws IOException;
}
