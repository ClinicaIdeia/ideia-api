package com.ideiaapi.repository.empresa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideiaapi.model.Empresa;
import com.ideiaapi.repository.filter.EmpresaFilter;
import com.ideiaapi.repository.projection.ResumoEmpresa;

public interface EmpresaRepositoryQuery {

    Page<Empresa> filtrar(EmpresaFilter empresaFilter, Pageable pageable);

    Page<ResumoEmpresa> resumir(EmpresaFilter empresaFilter, Pageable pageable);

}
