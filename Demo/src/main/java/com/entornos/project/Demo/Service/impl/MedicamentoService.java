package com.entornos.project.Demo.Service.impl;

import com.entornos.project.Demo.Model.Medicamento;
import com.entornos.project.Demo.DTO.*;
import com.entornos.project.Demo.Repository.MedicamentoRepository;
import com.entornos.project.Demo.Service.interfaces.IMedicamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicamentoService implements IMedicamentoService {

    private final MedicamentoRepository repository;

    public List<MedicamentoDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MedicamentoDTO crear(CrearMedicamentoDTO crearMedicamentoDTO) {
        Medicamento medicamento = new Medicamento(crearMedicamentoDTO);
        return toDTO(repository.save(medicamento));
    }

    @Override
    public MedicamentoDTO actualizar(ActualizarMedicamentoDTO actualizarMedicamentoDTO) {
        Medicamento medicamento = repository.findById(actualizarMedicamentoDTO.getId()).orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));

        medicamento.setNombre(actualizarMedicamentoDTO.getNombre());
        medicamento.setPrecio(actualizarMedicamentoDTO.getPrecio());
        medicamento.setImagen(actualizarMedicamentoDTO.getImagenMed());
        medicamento.setVentaLibre(actualizarMedicamentoDTO.getVentaLibre());

        return toDTO(repository.save(medicamento));
    }

    @Override
    public MedicamentoDTO eliminar(Long id) {
        Medicamento medicamento = repository.findById(id).orElse(null);
        if (medicamento == null) throw new RuntimeException("Medicamento no encontrado");
        repository.deleteById(id);
        return toDTO(medicamento);
    }

    private MedicamentoDTO toDTO(Medicamento m) {
        return MedicamentoDTO.builder()
                .id(m.getId())
                .nombre(m.getNombre())
                .precio(m.getPrecio())
                .ventaLibre(m.getVentaLibre())
                .build();
    }

    @Override
    public MedicamentoDTO obtenerPorId(Long id) {
        Medicamento medicamento = repository.findById(id)
                .orElse(null);
        if (medicamento == null) return null;
        return toDTO(medicamento);
    }


}