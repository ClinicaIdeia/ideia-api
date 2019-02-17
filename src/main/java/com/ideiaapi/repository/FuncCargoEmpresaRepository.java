package com.ideiaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.FuncCargoEmpresa;

public interface FuncCargoEmpresaRepository extends JpaRepository<FuncCargoEmpresa, Long> {

    FuncCargoEmpresa findByCodFuncionarioAndCodEmpresa(Long codFuncionario, Long codEmpresa);
}
