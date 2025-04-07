package com.entornos.project.Demo.Controller;

import com.entornos.project.Demo.DTO.*;
import com.entornos.project.Demo.Service.MedicamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService service;

    @GetMapping
    @PreAuthorize("hasAuthority('gerente')")
    public List<MedicamentoDTO> listar() {
        return service.listar();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('gerente')")
    public MedicamentoDTO crear(@RequestBody CrearMedicamentoRequest request) {
        return service.agregar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('gerente')")
    public MedicamentoDTO actualizar(@PathVariable Long id, @RequestBody CrearMedicamentoRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('gerente')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}