package com.ideiaapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.funcionario.FuncionarioRepositoryQuery;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>, FuncionarioRepositoryQuery {

    Funcionario findByCpf(String cpf);

    @Query(value = "select func.* from funcionario func\n" +
            "  inner join funcionario_empresa fe on func.codigo = fe.codigo_funcionario\n" +
            "where fe.codigo_empresa = :codigo", nativeQuery = true)
    List<Funcionario> findAllByEmpresas(@Param(value = "codigo") Long codigo);

    @Query(value = "select max(numero_cadastro) + 1 as next from funcionario", nativeQuery = true)
    Long getProximoNumeroCadastroDisponivel();

    List<Funcionario> findByNomeContainingIgnoreCaseOrderByNome(String nome);
}
