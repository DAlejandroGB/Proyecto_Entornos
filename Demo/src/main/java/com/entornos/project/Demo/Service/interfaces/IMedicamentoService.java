package com.entornos.project.Demo.Service.interfaces;

import com.entornos.project.Demo.DTO.CrearMedicamentoDTO;
import com.entornos.project.Demo.DTO.MedicamentoDTO;

import java.util.List;

public interface IMedicamentoService {
    List<MedicamentoDTO> listar();
    MedicamentoDTO crear(CrearMedicamentoDTO crearMedicamentoDTO);
    MedicamentoDTO actualizar(MedicamentoDTO medicamentoDTO);
    MedicamentoDTO eliminar(Long id);
    MedicamentoDTO obtenerPorId(Long id);
}
