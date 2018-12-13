package com.ideiaapi.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideiaapi.model.Motivo;
import com.ideiaapi.service.MotivoService;
import com.ideiaapi.service.MotivoService;

@RestController
@RequestMapping("/motivos")
public class MotivoResource {

    @Autowired
    private MotivoService motivoService;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_AGENDAMENTO') or hasAuthority('ROLE_ADMIN')  and #oauth2" +
            ".hasScope('read')")
    public List<Motivo> pesquisar() {
        return this.motivoService.listaTodosMotivos();
    }


}
