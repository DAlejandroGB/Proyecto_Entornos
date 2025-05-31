package com.entornos.project.Demo.Service.impl;

import com.entornos.project.Demo.DTO.ActualizarUsuarioDTO;
import com.entornos.project.Demo.DTO.CreateUsuarioDTO;
import com.entornos.project.Demo.DTO.UsuarioDTO;
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

    private UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioDTO> getUsuarios() {
        return this.usuarioRepository.findAll().stream().map(UsuarioDTO::new).toList();
    }

    @Override
    public UsuarioDTO buscarUsuario(Long id) {
        Usuario user = usuarioRepository.findById(id).orElse(null);
        if (user == null) return null;
        return new UsuarioDTO(user);
    }

    @Override
    public UsuarioDTO nuevoUsuario(CreateUsuarioDTO createUsuarioDTO) {
        Usuario usuario = new Usuario(createUsuarioDTO);
        return new UsuarioDTO(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioDTO borrarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) throw new RuntimeException("Usuario no encontrado");
        usuarioRepository.delete(usuario);
        return new UsuarioDTO(usuario);
    }

    @Override
    public UsuarioDTO actualizarUsuario(ActualizarUsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioDTO.getId()).orElse(null);
        if (usuario == null) throw new RuntimeException("Usuario no encontrado");

        usuario.setNombres(usuarioDTO.getNombres());
        usuario.setApellidos(usuarioDTO.getApellidos());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setIdRol(usuarioDTO.getIdRol());

        return new UsuarioDTO(usuarioRepository.save(usuario));
    }

    @Autowired
    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
}
