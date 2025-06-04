package com.entornos.project.Demo.DTO;

import com.entornos.project.Demo.Model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    @Schema(name = "nombre", description = "Nombres del cliente o usuario")
    private String nombres;
    @Schema(name = "apellidos", description = "Apellidos del cliente o usuario")
    private String apellidos;
    @Schema(name = "email", description = "Correo electronico del usuario")
    private String email;
    @Schema(name = "telefono", description = "Numero telefonico del usuario")
    private String telefono;
    @Schema(name = "direccion", description = "Direccion de residencia del usuario")
    private String direccion;
    @Schema(name = "rol", description = "Nombre del rol asignado al usuario")
    private String rol;
    @Schema(name = "fechaCreacion", description = "Fecha de creación del usuario")
    private LocalDate fechaCreacion;

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.email = usuario.getEmail();
        this.telefono = usuario.getTelefono();
        this.direccion = usuario.getDireccion();
        this.rol = usuario.getRol() != null ? usuario.getRol().getRol() : null;
        this.fechaCreacion = usuario.getFechaCreacion();
    }
}
