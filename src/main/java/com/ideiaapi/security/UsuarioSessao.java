package com.ideiaapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ideiaapi.model.Usuario;
import com.ideiaapi.service.UsuarioService;

@Component
public class UsuarioSessao {

    private static UsuarioService usuarioService;

    @Autowired
    public UsuarioSessao(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    public static Long getCodUsuario() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Object principal = authentication.getPrincipal();
        String email = (String) principal;
        Usuario usuario = usuarioService.buscaUsuarioPorEmail(email);


        return usuario.getCodigo();
    }

    public static String getUsername() {
        return null;
    }

    public static Authentication getAuthentication() {
        return null;
    }
}
