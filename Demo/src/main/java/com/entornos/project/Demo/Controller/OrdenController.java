package com.entornos.project.Demo.Controller;

import com.entornos.project.Demo.Model.Orden;
import com.entornos.project.Demo.Service.interfaces.IOrdenService;
import com.entornos.project.Demo.DTO.OrdenDTO;
import com.entornos.project.Demo.DTO.OrdenMedicamentoDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/orden")
public class OrdenController {

    private IOrdenService ordenService;

    @Operation(summary = "Listar Ordenes por usuario")
    @GetMapping("/byIdUsuario/{idUsuario}")
    public ResponseEntity<Page<OrdenDTO>> getOrdenes(@PathVariable Long idUsuario, Pageable pageable) {
        Page<OrdenDTO> ordenes = this.ordenService.getAllOrdenesByUsuario(idUsuario, pageable);

        if (ordenes.isEmpty()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @Operation(summary = "Agregar medicamento a una orden")
    @PostMapping("/addMedicamento")
    public ResponseEntity<OrdenMedicamentoDTO> addMedicamento(@RequestBody OrdenMedicamentoDTO ordenMedicamentoDTO, @RequestPart(name = "imagen", required = false) MultipartFile imagen,@RequestHeader(name = "idUsuario") Long idUsuario) throws IOException {
        OrdenMedicamentoDTO ordenMedDTO = this.ordenService.addMedicamento(ordenMedicamentoDTO, idUsuario);
        return ResponseEntity.ok(ordenMedDTO);
    }

    @Operation(summary = "Eliminar medicamento de una orden")
    @DeleteMapping("/deleteMedicamento")
    public ResponseEntity<?> deleteMedicamento(@RequestBody OrdenMedicamentoDTO ordenMedicamentoDTO) {
        this.ordenService.deleteMedicamento(ordenMedicamentoDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener la orden pendiente del usuario")
    @GetMapping("/ordenPendiente/{idUsuario}")
    public ResponseEntity<OrdenDTO> getOrdenPendiente(@PathVariable("idUsuario") Long idUsuario) {
        OrdenDTO orden = this.ordenService.getOrdenPendiente(idUsuario);

        if (orden == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
        return new ResponseEntity<>(orden, HttpStatus.OK);
    }

    @Operation(summary = "Listar ordenes por estado")
    @GetMapping("/{estado}")
    public ResponseEntity<Page<Orden>> getAllOrdenesByEstado(@PathVariable("estado") String estado, Pageable pageable) {
        Page<Orden> ordenes = this.ordenService.getAllOrdenesByEstado(estado, pageable);
        if (ordenes.isEmpty()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @Operation(summary = "Obtener detalles de la orden por id")
    @GetMapping()
    public ResponseEntity<OrdenDTO> getOrdenById(@RequestParam Long idOrden) {
        return new ResponseEntity<>(this.ordenService.getOrden(idOrden), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar estado de la oren")
    @PutMapping
    public ResponseEntity<Orden> updateEstadoOrden(@RequestParam Long idOrden, @RequestParam String estado){
        Orden ordenUpdated = this.ordenService.updateEstadoOrden(idOrden, estado);
        return new ResponseEntity<>(ordenUpdated, HttpStatus.OK);
    }

    @Autowired
    public void setOrdenService(IOrdenService ordenService) {
        this.ordenService = ordenService;
    }
}
