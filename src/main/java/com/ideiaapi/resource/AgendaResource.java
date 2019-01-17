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
import com.ideiaapi.model.Agenda;
import com.ideiaapi.repository.filter.AgendaFilter;
import com.ideiaapi.repository.projection.ResumoAgendamento;
import com.ideiaapi.service.AgendaService;
import com.ideiaapi.service.AgendaService;

@RestController
@RequestMapping("/agendas")
public class AgendaResource {

    @Autowired
    private AgendaService agendaService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_HORARIO') or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN')  and #oauth2" +
            ".hasScope('read')")
    public Page pesquisar(AgendaFilter filter, Pageable pageable) {
       // return this.agendaService.listaAgendamentos(filter, pageable);
        return this.agendaService.listaFuturosAgendamentos(); //TODO : Ver com alex
    }

    @GetMapping("/resumo")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_HORARIO') or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public Page<ResumoAgendamento> resumo(AgendaFilter filter, Pageable pageable) {
        return this.agendaService.resumo(filter, pageable);
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('ROLE_CADASTRAR_HORARIO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('write')")
    public ResponseEntity<Agenda> criar(@RequestBody @Valid Agenda agenda,
            HttpServletResponse response) {

        final Agenda agendaSalva = this.agendaService.cadastraAgenda(agenda);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, agendaSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(agendaSalva);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_HORARIO')  or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_DEFAULT') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public ResponseEntity<Agenda> busca(@PathVariable Long codigo) {
        Agenda agenda = this.agendaService.buscaAgendamento(codigo);

        if (null == agenda)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(agenda);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(value = "hasAuthority('ROLE_REMOVER_HORARIO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('write')")
    public void deleta(@PathVariable Long codigo) {
        this.agendaService.deletaAgendamento(codigo);
    }

    @PutMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_CADASTRAR_HORARIO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('write')")
    public ResponseEntity<Agenda> atualiza(@PathVariable Long codigo,
            @RequestBody @Valid Agenda agenda) {
        return this.agendaService.atualizaAgendamento(codigo, agenda);

    }

}
