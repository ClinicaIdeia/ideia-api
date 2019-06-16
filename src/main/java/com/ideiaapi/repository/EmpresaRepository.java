package com.ideiaapi.repository;

import com.ideiaapi.model.Empresa;
import com.ideiaapi.repository.empresa.EmpresaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EmpresaRepository extends JpaRepository<Empresa, Long>, EmpresaRepositoryQuery {

    Empresa findByCnpj(String cnpj);

    List<Empresa> findByNomeContainingIgnoreCaseOrderByNomeAscCodigoDesc(String nome);

}
