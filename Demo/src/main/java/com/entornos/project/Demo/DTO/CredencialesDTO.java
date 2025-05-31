package com.entornos.project.Demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredencialesDTO {
    private Long idCredencial;
    private String nombreUsuario;
    private String contrasena;
    private LocalDate fechaCreacion;
}
