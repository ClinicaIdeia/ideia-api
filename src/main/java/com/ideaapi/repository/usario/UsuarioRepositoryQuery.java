package com.ideaapi.repository.usario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideaapi.model.Usuario;
import com.ideaapi.repository.filter.UsuarioFilter;

public interface UsuarioRepositoryQuery {

    Page<Usuario> filtrar(UsuarioFilter usarioFilter, Pageable pageable);

}
