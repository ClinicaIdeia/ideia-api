package com.ideiaapi.validate;

import static com.ideiaapi.constants.ErrorsCode.USUARIO_DUPLICADO;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ideiaapi.exceptions.BusinessException;
import com.ideiaapi.model.Usuario;
import com.ideiaapi.repository.UsuarioRepository;

@Component
public class UsuarioValidate {

    @Autowired
    private UsuarioRepository repository;

    public void validaInsercao(Usuario usuario) {
        this.validaUniqueLogin(usuario);
        this.validaSeExaminador(usuario);
    }

    private void validaUniqueLogin(Usuario usuario) {
        Optional<Usuario> user = this.repository.findByEmail(usuario.getEmail());
        if (user.isPresent()) {
            throw new BusinessException(USUARIO_DUPLICADO);
        }
    }

    private void validaSeExaminador(Usuario usuario) {

    }

}
