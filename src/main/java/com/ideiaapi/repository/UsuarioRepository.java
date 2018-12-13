package com.ideiaapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.Usuario;
import com.ideiaapi.repository.usario.UsuarioRepositoryQuery;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryQuery {

    Optional<Usuario> findByEmail(String email);

}
