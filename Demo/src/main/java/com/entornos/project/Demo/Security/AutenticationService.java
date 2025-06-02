package com.entornos.project.Demo.Security;

import com.entornos.project.Demo.Repository.CredencialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticationService implements UserDetailsService {

    //UserDetailsService es una clase q ya usa spring para autenticar usuarios
    private CredencialRepository credencialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.credencialRepository.findByNombreUsuario(username);
    }

    @Autowired
    public void setUserRepository(CredencialRepository credencialRepository) {
        this.credencialRepository = credencialRepository;
    }
}
