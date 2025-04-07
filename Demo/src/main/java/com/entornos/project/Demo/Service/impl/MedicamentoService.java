package com.entornos.project.Demo.Service.impl;

import com.entornos.project.Demo.Model.Medicamento;
import com.entornos.project.Demo.dto.*;
import com.entornos.project.Demo.Repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final MedicamentoRepository repository;

    public List<MedicamentoDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MedicamentoDTO agregar(CrearMedicamentoDTO request) {
        Medicamento medicamento = Medicamento.builder()
                .nombre(request.getNombre())
                .precio(request.getPrecio())
                .imagenMed(request.getImagenMed())
                .tipo(request.getTipo())
                .build();
        return toDTO(repository.save(medicamento));
    }

    public MedicamentoDTO actualizar(Long id, CrearMedicamentoDTO request) {
        Medicamento medicamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));

        medicamento.setNombre(request.getNombre());
        medicamento.setPrecio(request.getPrecio());
        medicamento.setImagenMed(request.getImagenMed());
        medicamento.setTipo(request.getTipo());

        return toDTO(repository.save(medicamento));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private MedicamentoDTO toDTO(Medicamento m) {
        return MedicamentoDTO.builder()
                .id(m.getMedicamentoId())
                .nombre(m.getNombre())
                .precio(m.getPrecio())
                .tipo(m.getTipo())
                .build();
    }

}