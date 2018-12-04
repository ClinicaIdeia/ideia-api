package com.ideaapi.repository.empresa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideaapi.model.Empresa;
import com.ideaapi.repository.filter.EmpresaFilter;
import com.ideaapi.repository.projection.ResumoEmpresa;

public interface EmpresaRepositoryQuery {

    Page<Empresa> filtrar(EmpresaFilter empresaFilter, Pageable pageable);

    Page<ResumoEmpresa> resumir(EmpresaFilter empresaFilter, Pageable pageable);

}
