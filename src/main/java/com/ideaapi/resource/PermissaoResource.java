package com.ideaapi.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideaapi.model.Permissao;
import com.ideaapi.service.PermissaoService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/permissoes")
public class PermissaoResource {

    @Autowired
    private PermissaoService permissaoService;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public List<Permissao> lista() {
        return this.permissaoService.listaTodasAsPermissoes();
    }
}
