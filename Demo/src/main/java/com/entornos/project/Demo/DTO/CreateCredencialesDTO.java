package com.entornos.project.Demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCredencialesDTO {
    private Long idUsuario;
    private String nombreUsuario;
    private String contrasena;
}
