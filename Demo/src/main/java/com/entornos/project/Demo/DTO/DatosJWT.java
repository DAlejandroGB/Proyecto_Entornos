package com.entornos.project.Demo.DTO;

import com.entornos.project.Demo.Model.Credencial;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatosJWT {
    private String token;
    private Long idUsuario;
    private String nombreUsuario;
    private String rolUsuario;

    public DatosJWT(Credencial user, String token) {
        this.token = token;
        this.idUsuario = user.getIdUsuario();
        this.nombreUsuario = user.getNombreUsuario();
    }
}