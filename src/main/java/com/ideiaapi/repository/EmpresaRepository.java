package com.ideiaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.Empresa;
import com.ideiaapi.repository.empresa.EmpresaRepositoryQuery;


public interface EmpresaRepository extends JpaRepository<Empresa, Long>, EmpresaRepositoryQuery {

    Empresa findByCnpj(String cnpj);

}
