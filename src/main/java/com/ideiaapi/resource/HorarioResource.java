package com.ideiaapi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ideiaapi.event.RecursoCriadoEvent;
import com.ideiaapi.model.Horario;
import com.ideiaapi.service.HorarioService;

@RestController
@RequestMapping("/horarios")
public class HorarioResource {

    @Autowired
    public HorarioService horarioService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/motivos")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_HORARIO') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public List<Horario> listar(@RequestParam(value = "trabalhoArmado") Boolean trabalhoArmado) {
        return this.horarioService.listaHorarios(trabalhoArmado);
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('ROLE_CADASTRAR_HORARIO') or hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('write')")
    public ResponseEntity<Horario> criar(@RequestBody @Valid Horario horario, HttpServletResponse response) {

        final Horario horarioSalvo = this.horarioService.cadastraHorario(horario);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, horarioSalvo.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(horarioSalvo);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_HORARIO') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    public ResponseEntity<Horario> busca(@PathVariable Long codigo) {
        Horario horario = this.horarioService.buscaHorario(codigo);

        if (null == horario)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(horario);
    }

    @DeleteMapping("/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_REMOVER_HORARIO') or hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('read')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleta(@PathVariable Long codigo) {
        this.horarioService.deletaHorario(codigo);
    }
}
