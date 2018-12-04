package com.ideaapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideaapi.model.Usuario;
import com.ideaapi.repository.usario.UsuarioRepositoryQuery;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryQuery {

    Optional<Usuario> findByEmail(String email);

}
