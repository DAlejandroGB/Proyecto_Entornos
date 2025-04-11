package com.entornos.project.Demo.Service.impl;

import com.entornos.project.Demo.Model.Credencial;
import com.entornos.project.Demo.Model.Usuario;
import com.entornos.project.Demo.Repository.CredencialRepository;
import com.entornos.project.Demo.Repository.UsuarioRepository;
import com.entornos.project.Demo.Service.interfaces.ICredencialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CredencialService implements ICredencialService {

    @Autowired
    private CredencialRepository credencialRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Credencial> listarCredenciales() {
        return credencialRepository.findAll();
    }

    @Override
    public Credencial getCredencial(Long id) {
        return credencialRepository.findById(id).orElse(null);
    }

    @Override
    public Credencial buscarCredencial(Long idUsuario) {
        Usuario u = usuarioRepository.findById(idUsuario).orElse(null);
        if (u == null) {
            return null;
        }else {
        return credencialRepository.findByUsuario(u);
        }
    }

    @Override
    public Credencial saveCredencial(Credencial credencial) {
        return credencialRepository.save(credencial);
    }

    @Override
    public void eliminarCredencial(Credencial credencial) {
        credencialRepository.delete(credencial);
    }
}
