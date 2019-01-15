package com.ideiaapi.resource;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ideiaapi.dto.s3.AnexoS3DTO;
import com.ideiaapi.event.RecursoCriadoEvent;
import com.ideiaapi.model.SenhaAlterar;
import com.ideiaapi.model.SenhaReiniciar;
import com.ideiaapi.model.Usuario;
import com.ideiaapi.repository.filter.UsuarioFilter;
import com.ideiaapi.service.UsuarioService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/anexo")
    @PreAuthorize(value = "hasAuthority('ROLE_UPLOAD_ARQUIVO') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public AnexoS3DTO salvarFotoUsuario(@RequestParam MultipartFile anexo) {
        return this.usuarioService.salvarFotoUsaruioS3(anexo);
    }

    @GetMapping
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public Page<Usuario> pesquisar(UsuarioFilter filter, Pageable pageable) {
        return this.usuarioService.filtrar(filter, pageable);
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public ResponseEntity<Usuario> criar(@RequestBody @Valid Usuario usuario,
            HttpServletResponse response) {

        final Usuario usuarioSalva = this.usuarioService.cadastraUsuario(usuario);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalva);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public ResponseEntity<Usuario> buscar(@PathVariable Long codigo) {
        Usuario usuario = this.usuarioService.buscaUsuario(codigo);

        if (null == usuario)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public void deletar(@PathVariable Long codigo) {
        this.usuarioService.deletaUsuario(codigo);
    }

    @PutMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long codigo,
            @RequestBody @Valid Usuario usuario) {
        return this.usuarioService.atualizaUsuario(codigo, usuario);
    }

    @PostMapping("/senha/mudar/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_TROCA_SENHA') and #oauth2.hasScope('write')")
    public ResponseEntity<Usuario> alterarSenha(
            @PathVariable Long codigo,
            @RequestBody @Valid SenhaAlterar senhaAlterar) {
        return this.usuarioService.alterarSenhaUsuario(codigo, senhaAlterar);
    }

    @PutMapping("/senha/reiniciar")
    public ResponseEntity reiniciarSenha(@RequestBody @Valid SenhaReiniciar senhaReiniciar) {
        return this.usuarioService.reiniciarSenhaUsuario(senhaReiniciar);
    }
}
