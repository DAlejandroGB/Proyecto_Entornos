package com.entornos.project.Demo.Service;

import com.entornos.project.Demo.Model.Usuario;
import com.entornos.project.Demo.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UsuarioService implements IUsuarioService{


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
        return usuarioRepository.findById(id).map(usuario ->{
            usuario.setNombre(usuarioAct.getNombre());
            usuario.setApellido(usuarioAct.getApellido());
            usuario.setEmail(usuarioAct.getEmail());
            usuario.setTelefono(usuarioAct.getTelefono());
            usuario.setDireccion(usuarioAct.getDireccion());
            usuario.setRol(usuarioAct.getRol());
            return usuarioRepository.save(usuario);
        }).orElse(null);
    }



}
