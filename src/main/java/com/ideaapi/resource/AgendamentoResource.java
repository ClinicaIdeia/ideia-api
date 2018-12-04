package com.ideaapi.resource;

import static com.ideaapi.constansts.ErrorsCode.PESSOA_INEXISTENTE_OU_INATIVA;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ideaapi.event.RecursoCriadoEvent;
import com.ideaapi.exceptionhandler.IdeaApiExceptionHandler;
import com.ideaapi.exceptions.EmpesaInexsistenteOuInativaException;
import com.ideaapi.model.Agendamento;
import com.ideaapi.repository.filter.AgendamentoFilter;
import com.ideaapi.repository.projection.ResumoAgendamento;
import com.ideaapi.service.AgendamentoService;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoResource {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_AGENDAMENTO') or hasAuthority('ROLE_ADMIN')  and #oauth2" +
            ".hasScope('read')")
    public Page<Agendamento> pesquisar(AgendamentoFilter filter, Pageable pageable) {
        return this.agendamentoService.listaTodasAgendamentos(filter, pageable);
    }

    @GetMapping("/resumo")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_AGENDAMENTO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public Page<ResumoAgendamento> resumo(AgendamentoFilter filter, Pageable pageable) {
        return this.agendamentoService.resumo(filter, pageable);
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('ROLE_CADASTRAR_AGENDAMENTO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('write')")
    public ResponseEntity<Agendamento> criar(@RequestBody @Valid Agendamento agendamento,
            HttpServletResponse response) {

        final Agendamento agendamentoSalva = this.agendamentoService.cadastraAgendamento(agendamento);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, agendamentoSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoSalva);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_AGENDAMENTO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public ResponseEntity<Agendamento> busca(@PathVariable Long codigo) {
        Agendamento agendamento = this.agendamentoService.buscaAgendamento(codigo);

        if (null == agendamento)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(agendamento);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(value = "hasAuthority('ROLE_REMOVER_AGENDAMENTO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('write')")
    public void deleta(@PathVariable Long codigo) {
        this.agendamentoService.deletaAgendamento(codigo);
    }

    @PutMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_CADASTRAR_AGENDAMENTO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('write')")
    public ResponseEntity<Agendamento> atualiza(@PathVariable Long codigo,
            @RequestBody @Valid Agendamento agendamento) {
        return this.agendamentoService.atualizaAgendamento(codigo, agendamento);

    }

    @ExceptionHandler({EmpesaInexsistenteOuInativaException.class})
    public ResponseEntity<Object> handleFuncionarioInexitenteOuInativaException(
            EmpesaInexsistenteOuInativaException ex) {

        String mensagemUsuario = this.messageSource.getMessage(PESSOA_INEXISTENTE_OU_INATIVA, null,
                LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();

        List<IdeaApiExceptionHandler.Erro> erros = Arrays.asList(
                new IdeaApiExceptionHandler.Erro(mensagemUsuario, mensagemDesenvolvedor));

        return ResponseEntity.badRequest().body(erros);
    }

}
