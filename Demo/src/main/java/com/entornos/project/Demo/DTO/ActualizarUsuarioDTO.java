package com.entornos.project.Demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private String direccion;
    private Long idRol;
}
