package com.entornos.project.Demo.Controller;

import com.entornos.project.Demo.DTO.ActualizarUsuarioDTO;
import com.entornos.project.Demo.DTO.CreateUsuarioDTO;
import com.entornos.project.Demo.DTO.UsuarioDTO;
import com.entornos.project.Demo.Service.impl.AuthService;
import com.entornos.project.Demo.Service.impl.UsuarioService;
import com.entornos.project.Demo.Service.interfaces.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    IUsuarioService usuarioService;
    AuthService authService;

    @Operation(summary = "Listar usuarios")
    @GetMapping("/list")
    public ResponseEntity<List<UsuarioDTO>> cargarUsuarios() {
        List<UsuarioDTO> usuarioDTOS = this.usuarioService.getUsuarios();
        if (usuarioDTOS.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(usuarioDTOS, HttpStatus.OK);
    }

    @Operation(summary = "Obtener usuario por id")
    @GetMapping("/list/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        UsuarioDTO usuarioDTO = usuarioService.buscarUsuario(id);
        if (usuarioDTO == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @Operation(summary = "Crear usuario")
    @PostMapping("")
    public ResponseEntity<UsuarioDTO> agregarUsuario(@RequestBody CreateUsuarioDTO createUsuarioDTO) {
        UsuarioDTO usuarioDTO = usuarioService.nuevoUsuario(createUsuarioDTO);
        return new ResponseEntity<>(usuarioDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar información del usuario")
    @PutMapping("")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@RequestBody ActualizarUsuarioDTO actualizarUsuarioDTO) {
        UsuarioDTO obj = usuarioService.actualizarUsuario(actualizarUsuarioDTO);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar Usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioDTO> eliminarUsuario(@PathVariable Long id) {
        return new ResponseEntity<>(this.usuarioService.borrarUsuario(id), HttpStatus.OK);
    }

    @PostMapping({"/login"})
    public ResponseEntity<String> login(@RequestParam String usuarioNombre, @RequestParam String contrasena) {
        try {
            String token = this.authService.login(usuarioNombre, contrasena);
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping({"/verify"})
    public ResponseEntity<String> verifyToken(@RequestParam String token) {
        try {
            this.authService.verificarToken(token);
            return ResponseEntity.ok("Token es válido");
        } catch (Exception var3) {
            return ResponseEntity.status(401).body("Token no válido");
        }
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Autowired
    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
}
