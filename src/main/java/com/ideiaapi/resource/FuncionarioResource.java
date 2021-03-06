package com.ideiaapi.resource;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

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

import com.ideiaapi.dto.FuncionarioDTO;
import com.ideiaapi.dto.RowsImportDTO;
import com.ideiaapi.dto.s3.AnexoS3DTO;
import com.ideiaapi.event.RecursoCriadoEvent;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.filter.FuncionarioFilter;
import com.ideiaapi.repository.projection.ResumoFuncionario;
import com.ideiaapi.service.FuncionarioService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioResource {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_FUNCIONARIO')  or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public Page<Funcionario> pesquisar(FuncionarioFilter filter, Pageable pageable) {
        return this.funcionarioService.filtrar(filter, pageable);
    }

    @GetMapping(path = "/resumo")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_FUNCIONARIO')  or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public Page<ResumoFuncionario> resumo(FuncionarioFilter filter, Pageable pageable) {
        return this.funcionarioService.resumo(filter, pageable);
    }

    @GetMapping(path = "/todos")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_FUNCIONARIO')  or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public ResponseEntity<List<Funcionario>> pesquisarTodos() {
        final List<Funcionario> todos = this.funcionarioService.todos();
        return ResponseEntity.ok(todos);
    }

    @PostMapping("/anexo")
    @PreAuthorize(value = "hasAuthority('ROLE_CADASTRAR_FUNCIONARIO')  or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN') and #oauth2" +
            ".hasScope('write')")
    public AnexoS3DTO salvarFotoFuncionario(@RequestParam MultipartFile anexo) {
        return this.funcionarioService.salvarFotoFuncionarioS3(anexo);
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('ROLE_CADASTRAR_FUNCIONARIO')  or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public ResponseEntity<Funcionario> criar(@RequestBody @Valid Funcionario funcionario,
            HttpServletResponse response) {

        final Funcionario funcionarioSalva = this.funcionarioService.cadastraFuncionario(funcionario);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, funcionarioSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioSalva);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_FUNCIONARIO')  or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public ResponseEntity<Funcionario> busca(@PathVariable Long codigo) {
        Funcionario funcionario = this.funcionarioService.buscaFuncionario(codigo);

        if (null == funcionario)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(funcionario);
    }

    @GetMapping("/auto-complete")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_FUNCIONARIO')  or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public ResponseEntity<List<FuncionarioDTO>> buscaComAutoComlete(@PathParam("nome") String nome) {
        List<FuncionarioDTO> funcionarios = this.funcionarioService.buscaFuncionarioComAutoComplete(nome);

        if (null == funcionarios || funcionarios.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/cpf")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_FUNCIONARIO')  or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public ResponseEntity<Funcionario> buscaPorCpf(@PathParam("cpf") String cpf) {
        Funcionario funcionario = this.funcionarioService.buscaFuncionarioPorCpf(cpf);

        if (null == funcionario)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(funcionario);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(value = "hasAuthority('ROLE_REMOVER_FUNCIONARIO')  or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public void deleta(@PathVariable Long codigo) {
        this.funcionarioService.deletaFuncionario(codigo);
    }

    @PutMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_FUNCIONARIO')  or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public ResponseEntity<Funcionario> atualiza(@PathVariable Long codigo,
            @RequestBody @Valid Funcionario funcionario) {
        return this.funcionarioService.atualizaFuncionario(codigo, funcionario);

    }

    @PostMapping("/import")
    @PreAuthorize(value = "hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public RowsImportDTO mapReapExcelDatatoDB(
            @RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {
        return this.funcionarioService.importaFuncionarios(reapExcelDataFile);
    }

}
