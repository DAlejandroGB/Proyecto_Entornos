package com.entornos.project.Demo.Controller;

import com.entornos.project.Demo.Model.Usuario;
import com.entornos.project.Demo.Service.impl.AuthService;
import com.entornos.project.Demo.Service.impl.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    AuthService authService;

    @GetMapping("/list")
    public List<Usuario> cargarUsuarios() {
        return usuarioService.getUsuarios();
    }

    @GetMapping("/list/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarUsuario(id);
    }

    @PostMapping("/")
    public ResponseEntity<Usuario> agregarUsuario(@RequestBody Usuario usuario) {
        Usuario obj = usuarioService.nuevoUsuario(usuario);
        return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario obj = usuarioService.actualizarUsuario(id, usuario);
        if (obj == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> eliminarUsuario(@PathVariable Long id) {
        Usuario obj = usuarioService.buscarUsuario(id);
        if (obj != null) {
            usuarioService.borrarUsuario(id);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    @PostMapping({"/login"})
    public ResponseEntity<String> login(@RequestParam String usuarioNombre, @RequestParam String contrasena) {
        try {
            String token = this.authService.login(usuarioNombre, contrasena);
            System.out.println(token);
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
}
