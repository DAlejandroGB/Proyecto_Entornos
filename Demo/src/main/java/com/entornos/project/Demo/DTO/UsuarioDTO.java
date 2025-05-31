package com.entornos.project.Demo.DTO;

import com.entornos.project.Demo.Model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private String direccion;
    private String rol;
    private LocalDate fechaCreacion;

    public UsuarioDTO(Usuario usuario) {
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.email = usuario.getEmail();
        this.telefono = usuario.getTelefono();
        this.direccion = usuario.getDireccion();
        this.rol = usuario.getRol().getRol();
        this.fechaCreacion = usuario.getFechaCreacion();
    }
}
