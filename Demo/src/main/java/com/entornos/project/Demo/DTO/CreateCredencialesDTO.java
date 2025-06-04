package com.entornos.project.Demo.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCredencialesDTO {
    @Schema(name = "idUsuario", description = "Identificador del usuario al cual se van a crear las credenciales")
    private Long idUsuario;
    @Schema(name = "nombreUsuario", description = "Nombre de usuario usado para acceder a los servicios")
    private String nombreUsuario;
    @Schema(name = "contrasena", description = "Contraseña usada para acceder a los servicios")
    private String contrasena;
}
