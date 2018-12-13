package com.ideiaapi.repository.usario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideiaapi.model.Usuario;
import com.ideiaapi.repository.filter.UsuarioFilter;

public interface UsuarioRepositoryQuery {

    Page<Usuario> filtrar(UsuarioFilter usarioFilter, Pageable pageable);

}
