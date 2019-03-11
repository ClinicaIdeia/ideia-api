package com.ideiaapi.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideiaapi.model.Permissao;
import com.ideiaapi.service.PermissaoService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/permissoes")
public class PermissaoResource {

    @Autowired
    private PermissaoService permissaoService;

    @GetMapping("/{codigoUsuario}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')  and #oauth2.hasScope('read')")
    public List<Permissao> lista(@PathVariable Long codigoUsuario) {
        return this.permissaoService.listaTodasAsPermissoes(codigoUsuario);
    }
}
