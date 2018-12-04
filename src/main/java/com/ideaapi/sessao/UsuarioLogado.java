package com.ideaapi.sessao;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ideaapi.model.Usuario;

public class UsuarioLogado {

    private UsuarioLogado(){

    }

    public static Usuario gerUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object obj = authentication.getPrincipal();
            if (obj instanceof Usuario) {
                return (Usuario) obj;
            }
        }

        return null;
    }
}
