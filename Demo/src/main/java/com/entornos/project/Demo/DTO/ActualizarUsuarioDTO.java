package com.entornos.project.Demo.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioDTO {

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
}
