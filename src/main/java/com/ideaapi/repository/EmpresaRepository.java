package com.ideaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideaapi.model.Empresa;
import com.ideaapi.repository.empresa.EmpresaRepositoryQuery;


public interface EmpresaRepository extends JpaRepository<Empresa, Long>, EmpresaRepositoryQuery {

    Empresa findByCnpj(String cnpj);

}
