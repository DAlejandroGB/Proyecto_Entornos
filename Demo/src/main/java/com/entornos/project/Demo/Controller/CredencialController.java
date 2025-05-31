package com.entornos.project.Demo.Controller;

import com.entornos.project.Demo.DTO.CreateCredencialesDTO;
import com.entornos.project.Demo.DTO.CredencialesDTO;
import com.entornos.project.Demo.Model.Credencial;
import com.entornos.project.Demo.Service.interfaces.ICredencialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("credencial")
public class CredencialController {

    @Autowired
    private ICredencialService credencialService;

    @PostMapping()
    public ResponseEntity<CredencialesDTO> guardarCredenciales(@RequestBody CreateCredencialesDTO createCredencialesDTO) {
        try {
            CredencialesDTO credenciales = credencialService.saveCredenciales(createCredencialesDTO);
            return new ResponseEntity<>(credenciales, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<CredencialesDTO>> listarCredenciales() {
        List<CredencialesDTO> credenciales = this.credencialService.listarCredenciales();
        if(credenciales == null || credenciales.isEmpty()) return ResponseEntity.noContent().build();
        return new ResponseEntity<>(credenciales, HttpStatus.OK);
    }

    @GetMapping("/byNombreUsuario/{nombreUsuario}")
    public ResponseEntity<CredencialesDTO> searchCredencial(@PathVariable String nombreUsuario) {
        CredencialesDTO credencial = this.credencialService.getCredencialByNombreUsuario(nombreUsuario);
        if(credencial == null) return ResponseEntity.noContent().build();
        return new ResponseEntity<>(credencial, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<CredencialesDTO> updateCredencial(@RequestBody CreateCredencialesDTO credencialesDTO) {
        CredencialesDTO credenciales = this.credencialService.updateCredenciales(credencialesDTO);
        return new ResponseEntity<>(credenciales, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CredencialesDTO> deleteCredencial(@PathVariable Long id) {
        CredencialesDTO credencialesDTO = this.credencialService.eliminarCredencial(id);
        return new ResponseEntity<>(credencialesDTO, HttpStatus.OK);
    }

}
