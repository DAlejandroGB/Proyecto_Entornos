package com.entornos.project.Demo.Controller;

import com.entornos.project.Demo.Service.interfaces.IOrdenService;
import com.entornos.project.Demo.DTO.OrdenDTO;
import com.entornos.project.Demo.DTO.OrdenMedicamentoDTO;
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

    @GetMapping("{idUsuario}")
    public ResponseEntity<Page<OrdenDTO>> getOrdenes(@PathVariable Long idUsuario, Pageable pageable) {
        Page<OrdenDTO> ordenes = this.ordenService.getAllOrdenesByUsuario(idUsuario, pageable);

        if (ordenes.isEmpty()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @PostMapping("addMedicamento")
    public ResponseEntity<OrdenMedicamentoDTO> addMedicamento(@RequestBody OrdenMedicamentoDTO ordenMedicamentoDTO, @RequestPart(name = "imagen") MultipartFile imagen,@RequestParam(name = "idUsuario") Long idUsuario) throws IOException {
        OrdenMedicamentoDTO ordenMedDTO = this.ordenService.addMedicamento(ordenMedicamentoDTO, imagen, idUsuario);
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
