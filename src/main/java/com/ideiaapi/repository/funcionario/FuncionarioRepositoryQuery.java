package com.ideiaapi.repository.funcionario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.filter.FuncionarioFilter;
import com.ideiaapi.repository.projection.ResumoFuncionario;

public interface FuncionarioRepositoryQuery {

    Page<Funcionario> filtrar(FuncionarioFilter funcionarioFilter, Pageable pageable);

    Page<ResumoFuncionario> resumir(FuncionarioFilter funcionarioFilter, Pageable pageable);

}
