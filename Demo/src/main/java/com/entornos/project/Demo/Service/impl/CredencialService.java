package com.entornos.project.Demo.Service.impl;

import com.entornos.project.Demo.DTO.CreateCredencialesDTO;
import com.entornos.project.Demo.DTO.CredencialesDTO;
import com.entornos.project.Demo.Model.Credencial;
import com.entornos.project.Demo.Repository.CredencialRepository;
import com.entornos.project.Demo.Repository.UsuarioRepository;
import com.entornos.project.Demo.Service.interfaces.ICredencialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CredencialService implements ICredencialService {

    private CredencialRepository credencialRepository;
    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public List<CredencialesDTO> listarCredenciales() {
        List<Credencial> credenciales = this.credencialRepository.findAll();
        return credenciales.stream().map(credencial -> new CredencialesDTO(credencial.getId(), credencial.getNombreUsuario(), credencial.getContrasena(), credencial.getFechaCreacion())).toList();
    }

    @Override
    public CredencialesDTO getCredencialByNombreUsuario(String nombreUsuario) {
        Credencial credencial = credencialRepository.findByNombreUsuario(nombreUsuario);
        if (credencial == null) return null;
        return new CredencialesDTO(credencial.getId(), credencial.getNombreUsuario(), credencial.getContrasena(), credencial.getFechaCreacion());
    }



    @Override
    public CredencialesDTO saveCredenciales(CreateCredencialesDTO createCredencialesDTO) {
        if (createCredencialesDTO.getIdUsuario() == null) throw new RuntimeException("No se ha encontrado el id del usuario");
        //Se verifica que el usuario exista
        usuarioRepository.findById(createCredencialesDTO.getIdUsuario()).orElseThrow(RuntimeException::new);

        String password = passwordEncoder.encode(createCredencialesDTO.getContrasena());
        Credencial credencial = new Credencial(createCredencialesDTO.getIdUsuario(), createCredencialesDTO.getNombreUsuario(), password);

        Credencial savedCredencial = credencialRepository.save(credencial);
        return new CredencialesDTO(savedCredencial.getId(), savedCredencial.getNombreUsuario(), savedCredencial.getContrasena(), savedCredencial.getFechaCreacion());
    }

    @Override
    public CredencialesDTO eliminarCredencial(Long idCredencial) {
        Credencial credencial = credencialRepository.findById(idCredencial).orElse(null);
        if (credencial == null) throw new RuntimeException("No se encontraron las credenciales");
        credencialRepository.delete(credencial);
        return new CredencialesDTO(credencial.getId(), credencial.getNombreUsuario(), credencial.getContrasena(), credencial.getFechaCreacion());
    }

    @Override
    public CredencialesDTO updateCredenciales(CreateCredencialesDTO credencialesDTO) {
        Credencial usuario = this.credencialRepository.findByNombreUsuario(credencialesDTO.getNombreUsuario());
        if (usuario == null) throw new RuntimeException("No se ha encontrado el usuario");
        usuario.setContrasena(credencialesDTO.getContrasena());

        Credencial updatedCredencial = this.credencialRepository.save(usuario);
        return new CredencialesDTO(updatedCredencial.getId(), updatedCredencial.getNombreUsuario(), updatedCredencial.getContrasena(), updatedCredencial.getFechaCreacion());
    }

    @Autowired
    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Autowired
    public void setCredencialRepository(CredencialRepository credencialRepository) {
        this.credencialRepository = credencialRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
