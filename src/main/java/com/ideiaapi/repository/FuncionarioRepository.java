package com.ideiaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.funcionario.FuncionarioRepositoryQuery;


public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>, FuncionarioRepositoryQuery {

    Funcionario findByCpf(String cpf);
}
