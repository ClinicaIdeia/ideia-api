package com.ideiaapi.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ideiaapi.base.BaseTest;
import com.ideiaapi.exceptions.BusinessException;
import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.SenhaAlterar;
import com.ideiaapi.model.SenhaReiniciar;
import com.ideiaapi.model.Usuario;
import com.ideiaapi.repository.UsuarioRepository;

public class UsuarioServiceTest extends BaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EnvioEmail envioEmail;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    public void mudarSenhaUsuarioSucesso() {

        Usuario usuario = new Usuario();
        usuario.setEmail("email@test");

        SenhaAlterar senhaAlterar = new SenhaAlterar();
        senhaAlterar.setSenhaNova("new-password");
        senhaAlterar.setConfirmacao("new-password");

        when(usuarioRepository.findOne(anyLong())).thenReturn(usuario);

        ResponseEntity responseEntity = usuarioService.alterarSenhaUsuario(1L, senhaAlterar);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void mudarSenhaUsuarioFalha() {

        thrown.expect(BusinessException.class);

        SenhaAlterar senhaAlterar = new SenhaAlterar();
        senhaAlterar.setSenhaNova("new-password");

        usuarioService.alterarSenhaUsuario(1L, senhaAlterar);
    }

    @Test
    public void reiniciarSenhaUsuarioSucesso() {

        Usuario usuario = new Usuario();
        usuario.setEmail("email@test");

        SenhaReiniciar senhaReiniciar = new SenhaReiniciar();
        senhaReiniciar.setEmail("email@test");

        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        ResponseEntity responseEntity = usuarioService.reiniciarSenhaUsuario(senhaReiniciar);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void reiniciarSenhaUsuarioFalha() {

        thrown.expect(BusinessException.class);

        SenhaReiniciar senhaReiniciar = new SenhaReiniciar();
        senhaReiniciar.setEmail("email@test");

        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.empty());

        usuarioService.reiniciarSenhaUsuario(senhaReiniciar);
    }
}