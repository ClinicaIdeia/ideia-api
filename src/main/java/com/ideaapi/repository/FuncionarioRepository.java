package com.ideaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideaapi.model.Funcionario;
import com.ideaapi.repository.funcionario.FuncionarioRepositoryQuery;


public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>, FuncionarioRepositoryQuery {

    Funcionario findByCpf(String cpf);
}
