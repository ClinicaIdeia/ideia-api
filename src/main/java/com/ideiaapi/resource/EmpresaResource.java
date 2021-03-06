package com.ideiaapi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import com.ideiaapi.dto.EmpresaDTO;
import com.ideiaapi.dto.FuncionarioDTO;
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
import com.ideiaapi.model.Empresa;
import com.ideiaapi.repository.filter.EmpresaFilter;
import com.ideiaapi.service.EmpresaService;

@RestController
@RequestMapping("/empresas")
public class EmpresaResource {

    @Autowired
    public EmpresaService service;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_EMPRESA') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public Page<Empresa> listar(EmpresaFilter filter, Pageable pageable) {
        return this.service.filtrar(filter, pageable);
    }

    @GetMapping("/todas")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_EMPRESA') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public ResponseEntity<List<Empresa>> listarTodas() {
        List<Empresa> empresas = this.service.buscarTodas();
        return ResponseEntity.ok(empresas);
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('ROLE_CADASTRAR_EMPRESA') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('write')")
    public ResponseEntity<Empresa> criar(@RequestBody @Valid Empresa empresa, HttpServletResponse response) {

        final Empresa empresaSalva = this.service.cadastraEmpresa(empresa);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, empresaSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaSalva);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_EMPRESA') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public ResponseEntity<Empresa> busca(@PathVariable Long codigo) {

        Empresa empresa = this.service.buscaEmpresa(codigo);

        if (null == empresa)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(empresa);
    }

    @PutMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_EMPRESA') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public ResponseEntity<Empresa> atualiza(@PathVariable Long codigo,
            @RequestBody @Valid Empresa empresa) {
        return this.service.atualizaEmpresa(codigo, empresa);

    }

    @DeleteMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_REMOVER_EMPRESA') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleta(@PathVariable Long codigo) {
        this.service.deletaEmpresa(codigo);
    }

    @GetMapping("/auto-complete")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_FUNCIONARIO')  or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public ResponseEntity<List<EmpresaDTO>> buscaComAutoComlete(@PathParam("nome") String nome) {
        List<EmpresaDTO> empresas = this.service.buscaEmpresaComAutoComplete(nome);

        if (null == empresas || empresas.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(empresas);
    }

}
