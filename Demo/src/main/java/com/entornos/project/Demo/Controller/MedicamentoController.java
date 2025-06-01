package com.entornos.project.Demo.Controller;

import com.entornos.project.Demo.DTO.*;
import com.entornos.project.Demo.Model.Medicamento;
import com.entornos.project.Demo.Service.impl.MedicamentoService;
import com.entornos.project.Demo.Service.interfaces.ICredencialService;
import com.entornos.project.Demo.Service.interfaces.IMedicamentoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

    private IMedicamentoService medicamentoService;

    @Operation(summary = "Listar Medicamentos", description = "Retorna todos los medicamentos ")
    @GetMapping
    public ResponseEntity<List<MedicamentoDTO>> listar() {
        List<MedicamentoDTO> medicamentos = medicamentoService.listar();
        if (medicamentos.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(medicamentos, HttpStatus.OK);
    }

    @Operation(summary = "Crear Medicamento")
    @PostMapping
    public ResponseEntity<MedicamentoDTO> crear(@RequestBody CrearMedicamentoDTO crearMedicamentoDTO) {
       return new ResponseEntity<>(this.medicamentoService.crear(crearMedicamentoDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar medicamento")
    @PutMapping("/{id}")
    public ResponseEntity<MedicamentoDTO> actualizar(@RequestBody MedicamentoDTO medicamentoDTO) {
        return new ResponseEntity<>(medicamentoService.actualizar(medicamentoDTO), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar medicamento")
    @DeleteMapping("/{id}")
    public ResponseEntity<MedicamentoDTO> eliminar(@PathVariable Long id) {
        return new ResponseEntity<>(medicamentoService.eliminar(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtener medicamento por id")
    @GetMapping("/{id}")
    public ResponseEntity<MedicamentoDTO> obtenerPorId(@PathVariable Long id) {
        MedicamentoDTO medicamentoDTO = this.medicamentoService.obtenerPorId(id);
        if (medicamentoDTO == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(medicamentoDTO, HttpStatus.OK);
    }

    @Autowired
    public void setMedicamentoService(IMedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }
}