package com.entornos.project.Demo.Service;

import com.entornos.project.Demo.Model.Usuario;

import java.util.List;

public interface IUsuarioService {

    List<Usuario> getUsuarios();

    Usuario buscarUsuario(Long id);

    Usuario nuevoUsuario(Usuario usuario);

    Usuario actualizarUsuario(Long id, Usuario usuario);

    int borrarUsuario(Long id);
}
