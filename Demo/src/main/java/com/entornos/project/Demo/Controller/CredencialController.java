package com.entornos.project.Demo.Controller;

import com.entornos.project.Demo.Model.Credencial;
import com.entornos.project.Demo.Service.impl.CredencialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credencial")
public class CredencialController {

    @Autowired
    private CredencialService credencialService;

    @PostMapping("/")
    public ResponseEntity<Credencial> credencial(@RequestBody Credencial credencial) {
        try {
            Credencial c= credencialService.saveCredencial(credencial);
            System.out.println("Credencial credencial: "+ c);
            return new ResponseEntity<>(c, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public List<Credencial> listCredencial() {
        return credencialService.listarCredenciales();
    }

    @GetMapping("/list/{idUsuario}")
    public Credencial searchCredencial(@PathVariable Long idUsuario) {
        return credencialService.buscarCredencial(idUsuario);
    }

    @PutMapping("/")
    public ResponseEntity<Credencial> updateCredencial(@RequestBody Credencial credencial) {
        Credencial c = credencialService.getCredencial(credencial.getCredencialId());
        if (c!=null) {
            c.setUsuario(credencial.getUsuario());
            c.setContrasena(credencial.getContrasena());
            c.setUsuarioNombre(credencial.getUsuarioNombre());
            credencialService.saveCredencial(c);
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Credencial> deleteCredencial(@PathVariable Long id) {
        Credencial c = credencialService.getCredencial(id);
        if (c!=null) {
            credencialService.eliminarCredencial(c);
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
