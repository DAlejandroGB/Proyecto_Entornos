package com.entornos.project.Demo.Controller;

import com.entornos.project.Demo.DTO.*;
import com.entornos.project.Demo.Service.impl.MedicamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService service;

    @GetMapping
    public List<MedicamentoDTO> listar() {
        return service.listar();
    }

    @PostMapping
    public MedicamentoDTO crear(@RequestBody CrearMedicamentoDTO request) {
        return service.agregar(request);
    }

    @PutMapping("/{id}")
    public MedicamentoDTO actualizar(@PathVariable Long id, @RequestBody CrearMedicamentoDTO crearMedicamentoDTO) {
        return service.actualizar(id, crearMedicamentoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}