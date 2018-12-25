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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ideiaapi.event.RecursoCriadoEvent;
import com.ideiaapi.model.Laudo;
import com.ideiaapi.repository.filter.LaudoFilter;
import com.ideiaapi.service.LaudoService;

@RestController
@RequestMapping("/laudos")
public class LaudoResource {

    @Autowired
    public LaudoService laudoService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_LAUDO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public Page<Laudo> listar(LaudoFilter filter, Pageable pageable) {
        return this.laudoService.filtrar(filter, pageable);
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('ROLE_CADASTRAR_LAUDO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('write')")
    public ResponseEntity<Laudo> criar(@RequestBody @Valid Laudo laudo, HttpServletResponse response) {

        final Laudo laudoSalva = this.laudoService.cadastraLaudo(laudo);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, laudoSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(laudoSalva);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_LAUDO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public ResponseEntity<Laudo> busca(@PathVariable Long codigo) {

        Laudo laudo = this.laudoService.buscaLaudo(codigo);

        if (null == laudo)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(laudo);
    }

    @PutMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_LAUDO') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public ResponseEntity<Laudo> atualiza(@PathVariable Long codigo,
            @RequestBody @Valid Laudo laudo) {
        return this.laudoService.atualizaLaudo(codigo, laudo);

    }

    @DeleteMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_REMOVER_LAUDO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleta(@PathVariable Long codigo) {
        this.laudoService.deletaLaudo(codigo);
    }
}
