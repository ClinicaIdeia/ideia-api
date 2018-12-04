package com.ideaapi.repository.funcionario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideaapi.model.Funcionario;
import com.ideaapi.repository.filter.FuncionarioFilter;
import com.ideaapi.repository.projection.ResumoFuncionario;

public interface FuncionarioRepositoryQuery {

    Page<Funcionario> filtrar(FuncionarioFilter funcionarioFilter, Pageable pageable);

    Page<ResumoFuncionario> resumir(FuncionarioFilter funcionarioFilter, Pageable pageable);

}
