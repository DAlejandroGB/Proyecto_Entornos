package com.entornos.project.Demo.Service.interfaces;

import com.entornos.project.Demo.Model.Credencial;

import java.util.List;

public interface ICredencialService {
    List<Credencial> listarCredenciales();
    Credencial getCredencial(Long id);
    Credencial buscarCredencial(Long idUsuario);
    //Se usa tanto para crear como para actualizar
    Credencial saveCredencial(Credencial credencial);
    void eliminarCredencial(Credencial credencial);

}
