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
        return repository.findAllMedicamentosActivos()
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
    public MedicamentoDTO actualizar(MedicamentoDTO medicamentoDTO) {
        Medicamento medicamento = repository.findById(medicamentoDTO.getId()).orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));

        medicamento.setNombre(medicamentoDTO.getNombre());
        medicamento.setPrecio(medicamentoDTO.getPrecio());
        medicamento.setImagen(medicamentoDTO.getImagenMed());
        medicamento.setVentaLibre(medicamentoDTO.getVentaLibre());

        return toDTO(repository.save(medicamento));
    }

    @Override
    public MedicamentoDTO eliminar(Long id) {
        Medicamento medicamento = repository.findById(id).orElse(null);
        if (medicamento == null) throw new RuntimeException("Medicamento no encontrado");
        medicamento.setActivo(false);
        repository.save(medicamento);
        return toDTO(medicamento);
    }

    private MedicamentoDTO toDTO(Medicamento m) {
        return MedicamentoDTO.builder()
                .id(m.getId())
                .nombre(m.getNombre())
                .precio(m.getPrecio())
                .ventaLibre(m.getVentaLibre())
                .imagenMed(m.getImagen())
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