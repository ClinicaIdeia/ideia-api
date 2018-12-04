package com.ideaapi.resource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

import com.ideaapi.event.RecursoCriadoEvent;
import com.ideaapi.model.Funcionario;
import com.ideaapi.repository.filter.FuncionarioFilter;
import com.ideaapi.repository.projection.ResumoFuncionario;
import com.ideaapi.service.FuncionarioService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioResource {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_FUNCIONARIO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public Page<ResumoFuncionario> pesquisar(FuncionarioFilter filter, Pageable pageable) {
        return this.funcionarioService.resumo(filter, pageable);
    }

    @GetMapping(path = "/todos")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_FUNCIONARIO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public Page<ResumoFuncionario> pesquisarTodos(FuncionarioFilter filter, Pageable pageable) {
        return this.funcionarioService.resumo(filter, pageable);
    }

    @PostMapping("/anexo")
    @PreAuthorize(value = "hasAuthority('ROLE_CADASTRAR_FUNCIONARIO') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public String carregaAnexo(@RequestParam MultipartFile anexo) throws IOException {

        OutputStream out =
                new FileOutputStream("/home/alexalvesdesouza/Pictures/anexo--" + anexo.getOriginalFilename());
        out.write(anexo.getBytes());
        out.close();

        return "OK";
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('ROLE_CADASTRAR_FUNCIONARIO') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public ResponseEntity<Funcionario> criar(@RequestBody @Valid Funcionario funcionario,
            HttpServletResponse response) {

        final Funcionario funcionarioSalva = this.funcionarioService.cadastraFuncionario(funcionario);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, funcionarioSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioSalva);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_FUNCIONARIO') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public ResponseEntity<Funcionario> busca(@PathVariable Long codigo) {
        Funcionario funcionario = this.funcionarioService.buscaFuncionario(codigo);

        if (null == funcionario)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(funcionario);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(value = "hasAuthority('ROLE_REMOVER_FUNCIONARIO') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public void deleta(@PathVariable Long codigo) {
        this.funcionarioService.deletaFuncionario(codigo);
    }

    @PutMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_FUNCIONARIO') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public ResponseEntity<Funcionario> atualiza(@PathVariable Long codigo,
            @RequestBody @Valid Funcionario funcionario) {
        return this.funcionarioService.atualizaFuncionario(codigo, funcionario);

    }

}
