package com.entornos.project.Demo.Service.impl;

import com.entornos.project.Demo.Model.Usuario;
import com.entornos.project.Demo.Repository.UsuarioRepository;
import com.entornos.project.Demo.Service.interfaces.IUsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario nuevoUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public int borrarUsuario(Long id) {
        usuarioRepository.deleteById(id);
        return 1;
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioAct) {
        return usuarioRepository.findById(id).map(usuario -> {
            if (usuarioAct.getNombre() != null) usuario.setNombre(usuarioAct.getNombre());
            if (usuarioAct.getApellido() != null) usuario.setApellido(usuarioAct.getApellido());
            if (usuarioAct.getEmail() != null) usuario.setEmail(usuarioAct.getEmail());
            if (usuarioAct.getTelefono() != null) usuario.setTelefono(usuarioAct.getTelefono());
            if (usuarioAct.getDireccion() != null) usuario.setDireccion(usuarioAct.getDireccion());
            if (usuarioAct.getRol() != null) usuario.setRol(usuarioAct.getRol());
            return usuarioRepository.save(usuario);
        }).orElse(null);
    }



}
