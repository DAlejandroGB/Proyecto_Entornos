package com.entornos.project.Demo.Service.interfaces;

import com.entornos.project.Demo.DTO.CreateCredencialesDTO;
import com.entornos.project.Demo.DTO.CredencialesDTO;
import com.entornos.project.Demo.Model.Credencial;

import java.util.List;

public interface ICredencialService {
    List<CredencialesDTO> listarCredenciales();
    CredencialesDTO getCredencialByNombreUsuario(String nombreUsuario);
    //Se usa tanto para crear como para actualizar
    CredencialesDTO saveCredenciales(CreateCredencialesDTO createCredencialesDTO);
    CredencialesDTO eliminarCredencial(Long id);

    CredencialesDTO updateCredenciales(CreateCredencialesDTO credencialesDTO);
}
