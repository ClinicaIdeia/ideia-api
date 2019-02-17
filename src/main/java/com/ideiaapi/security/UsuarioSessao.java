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
    UsuarioSessao(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;//NOSONAR
    }

    public static Long getCodUsuario() {
        return null;
    }

    public static String getUsername() {
        return null;
    }

    public static Usuario getUserLogado() {

        Authentication authentication = getAuthentication();

        if (authentication != null) {
            final Object principal = authentication.getPrincipal();
            String email = (String) principal;
            return usuarioService.buscaUsuarioPorEmail(email);
        }

        return null;
    }

    public static boolean isAdmin(Usuario usuario) {

        return usuario.getPermissoes().stream().anyMatch(permissao -> permissao.getDescricao().equals(
                "ROLE_ADMIN"));

    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
