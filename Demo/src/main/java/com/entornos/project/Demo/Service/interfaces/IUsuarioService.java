package com.entornos.project.Demo.Service.interfaces;

import com.entornos.project.Demo.DTO.ActualizarUsuarioDTO;
import com.entornos.project.Demo.DTO.CreateUsuarioDTO;
import com.entornos.project.Demo.DTO.UsuarioDTO;
import com.entornos.project.Demo.Model.Credencial;
import com.entornos.project.Demo.Model.Usuario;

import java.util.List;

public interface IUsuarioService {

    List<UsuarioDTO> getUsuarios();
    UsuarioDTO buscarUsuario(Long id);
    UsuarioDTO nuevoUsuario(CreateUsuarioDTO createUsuarioDTO);
    UsuarioDTO actualizarUsuario(ActualizarUsuarioDTO actualizarUsuarioDTO);
    UsuarioDTO borrarUsuario(Long id);
}
