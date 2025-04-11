package com.entornos.project.Demo.Controller;

import com.entornos.project.Demo.Service.interfaces.IOrdenService;
import com.entornos.project.Demo.dto.OrdenDTO;
import com.entornos.project.Demo.dto.OrdenMedicamentoDTO;
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

    @PostMapping()
    public ResponseEntity<OrdenDTO> createOrden(Long idUsuario) {
        return ResponseEntity.ok(this.ordenService.createOrden(idUsuario));
    }

    @GetMapping("{idUsuario}")
    public ResponseEntity<Page<OrdenDTO>> getOrdenes(@PathVariable Long idUsuario, Pageable pageable) {
        Page<OrdenDTO> ordenes = this.ordenService.getAllOrdenesByUsuario(idUsuario, pageable);

        if (ordenes.isEmpty()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @GetMapping("getOrdenPendiente/{idUsuario}")
    public ResponseEntity<OrdenDTO> getOrdenPendiente(@PathVariable Long idUsuario) {
        OrdenDTO ordenDTO = this.ordenService.getOrdenPendiente(idUsuario);
        if (ordenDTO == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
        return ResponseEntity.ok(this.ordenService.getOrdenPendiente(idUsuario));
    }

    @PostMapping("addMedicamento")
    public ResponseEntity<OrdenMedicamentoDTO> addMedicamento(@RequestBody OrdenMedicamentoDTO ordenMedicamentoDTO, @RequestPart(name = "imagen") MultipartFile imagen) throws IOException {
        OrdenMedicamentoDTO ordenMedDTO = this.ordenService.addMedicamento(ordenMedicamentoDTO, imagen);
        return ResponseEntity.ok(ordenMedDTO);
    }

    @DeleteMapping("deleteMedicamento")
    public ResponseEntity<?> deleteMedicamento(@RequestBody OrdenMedicamentoDTO ordenMedicamentoDTO) {
        this.ordenService.deleteMedicamento(ordenMedicamentoDTO);
        return ResponseEntity.ok().build();
    }

    @Autowired
    public void setOrdenService(IOrdenService ordenService) {
        this.ordenService = ordenService;
    }
}
