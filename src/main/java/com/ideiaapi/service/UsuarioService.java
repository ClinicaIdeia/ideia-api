package com.ideiaapi.service;

import static com.ideiaapi.constants.ErrorsCode.RECURSO_NAO_ENCONTRADO;
import static com.ideiaapi.constants.ErrorsCode.SENHAS_DIFERENTES;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ideiaapi.dto.s3.AnexoS3DTO;
import com.ideiaapi.exceptions.BusinessException;
import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.SenhaAlterar;
import com.ideiaapi.model.SenhaReiniciar;
import com.ideiaapi.model.Usuario;
import com.ideiaapi.repository.UsuarioRepository;
import com.ideiaapi.repository.filter.UsuarioFilter;
import com.ideiaapi.storage.S3;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnvioEmail envioEmail;

    @Autowired
    private S3 s3;

    public AnexoS3DTO salvarFotoUsaruioS3(MultipartFile file) {
        String nome = s3.salvarArquivoS3Temporatimente(file);
        return new AnexoS3DTO(nome, s3.configuraUrl(nome));
    }

    public Page<Usuario> filtrar(UsuarioFilter filter, Pageable pageable) {
        final Page<Usuario> filtrar = this.usuarioRepository.filtrar(filter, pageable);
        filtrar.iterator().forEachRemaining(usuario -> usuario.setSenha("*******"));

        return filtrar;
    }

    public Usuario cadastraUsuario(Usuario entity) {
        Usuario usuarioSalvo = reiniciarSenhaAleatoriamenteParaUsuario(entity);
        usuarioSalvo.setSenha("*******");
        return usuarioSalvo;
    }

    public ResponseEntity<Usuario> atualizaUsuario(Long codigo, Usuario usuario) {
        Usuario usuarioSalvo = this.buscaUsuario(codigo);
        BeanUtils.copyProperties(usuario, usuarioSalvo, "codigo");

        this.usuarioRepository.save(usuarioSalvo);
        return ResponseEntity.ok(usuarioSalvo);
    }

    public Usuario buscaUsuario(Long codigo) {
        Usuario usuario = this.usuarioRepository.findOne(codigo);
        if (usuario != null) {
            return usuario;
        }

        return null;
    }

    public Usuario buscaUsuarioPorEmail(String email) {
        Optional<Usuario> usuario = this.usuarioRepository.findByEmail(email);
        if (usuario.isPresent()) {
            return usuario.get();
        }

        return null;
    }

    public void deletaUsuario(Long codigo) {
        this.usuarioRepository.delete(codigo);
    }

    public ResponseEntity<Usuario> alterarSenhaUsuario(Long codigo, SenhaAlterar senhaAlterar) {
        Usuario usuario = this.usuarioRepository.findOne(codigo);

        if (usuario != null) {
            this.validaSenhasDivergentes(senhaAlterar);
            usuario.setSenha(new BCryptPasswordEncoder().encode(senhaAlterar.getSenhaNova()));
            this.usuarioRepository.save(usuario);
            return ResponseEntity.ok(usuario);

        } else {
            throw new BusinessException(RECURSO_NAO_ENCONTRADO);
        }
    }

    private void validaSenhasDivergentes(SenhaAlterar senhaAlterar) {
        if (!senhaAlterar.getSenhaNova().equals(senhaAlterar.getConfirmacao())) {
            throw new BusinessException(SENHAS_DIFERENTES);
        }
    }

    public ResponseEntity reiniciarSenhaUsuario(SenhaReiniciar senhaReiniciar) {
        Usuario usuario = this.buscaUsuarioPorEmail(senhaReiniciar.getEmail());

        if (usuario != null) {
            this.reiniciarSenhaAleatoriamenteParaUsuario(usuario);
            return new ResponseEntity(HttpStatus.OK);

        } else {
            throw new BusinessException(RECURSO_NAO_ENCONTRADO);
        }
    }

    private Usuario reiniciarSenhaAleatoriamenteParaUsuario(Usuario usuario) {
        final String senha = senhaRandomica();
        usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
        Usuario usuarioSalvo = this.usuarioRepository.save(usuario);
        this.enviaEmailSenhaGerada(usuarioSalvo, senha);
        return usuarioSalvo;
    }

    private String senhaRandomica() {
        StringBuilder sb = new StringBuilder();
        Random gerador = new Random(); //NOSONAR

        for (int i = 0; i < 6; i++) {
            sb.append(gerador.nextInt(9));
        }
        return sb.toString();
    }

    private void enviaEmailSenhaGerada(Usuario usuario, String senha) {

        String template = "email/envio-senha";

        Map<String, Object> map = new HashMap<>();
        map.put("senha", senha);

        this.envioEmail.enviarEmail("openlinkti@gmail.com", Collections.singletonList(usuario.getEmail()),
                "Senha de Acesso ao Sistema Ideia", template, map);
    }
}
