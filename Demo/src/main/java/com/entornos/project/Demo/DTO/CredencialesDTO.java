package com.entornos.project.Demo.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredencialesDTO {
    @Schema(name = "idCredencial", description = "Identificador de las credenciales", example = "1")
    private Long idCredencial;
    @Schema(name = "nombreUsuario", description = "Nombre de usuario asociado a las credenciales", example = "juan.pepe")
    private String nombreUsuario;
    @Schema(name = "contrasena", description = "Contraseña asociada a las credenciales de acceso", example = "papasconaji")
    private String contrasena;
    @Schema(name = "fechaCreacion", description = "Fecha de creación de las credenciales", example = "05-05-2025")
    private LocalDate fechaCreacion;
}
