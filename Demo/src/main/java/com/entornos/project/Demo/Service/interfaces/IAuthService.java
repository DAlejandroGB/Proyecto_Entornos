package com.entornos.project.Demo.Service.interfaces;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.entornos.project.Demo.Model.Credencial;

public interface IAuthService {
    String login(String usuario, String password);
    private String generarJWT(Credencial credencial){
        return null;
    }
    DecodedJWT verificarToken(String token);
}
