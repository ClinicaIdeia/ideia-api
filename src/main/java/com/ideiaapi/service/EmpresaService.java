package com.ideiaapi.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ideiaapi.model.Empresa;
import com.ideiaapi.repository.EmpresaRepository;
import com.ideiaapi.repository.filter.EmpresaFilter;
import com.ideiaapi.repository.projection.ResumoEmpresa;
import com.ideiaapi.validate.EmpresaValidate;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ContatoService contatoService;

    @Autowired
    private EmpresaValidate empresaValidate;

    public Page<Empresa> filtrar(EmpresaFilter filter, Pageable pageable) {
        return this.empresaRepository.filtrar(filter, pageable);
    }

    public Page<ResumoEmpresa> resumo(EmpresaFilter filter, Pageable pageable) {
        return this.empresaRepository.resumir(filter, pageable);
    }

    public Empresa cadastraEmpresa(Empresa entity) {
        this.empresaValidate.fluxoCriacao(entity);

        if (entity.getAtiva() == null) {
            entity.setAtiva(true);
        }

        this.contatoService.cadastraContatos(entity);
        return this.empresaRepository.save(entity);
    }

    public Empresa buscaEmpresa(Long codigo) {
        Empresa empresa = this.empresaRepository.findOne(codigo);
        if (empresa != null) {
            return empresa;
        }

        return null;
    }

    public void deletaEmpresa(Long codigo) {
        this.empresaRepository.delete(codigo);
    }

    public ResponseEntity<Empresa> atualizaEmpresa(Long codigo, Empresa empresa) {
        Empresa empresaSalva = this.buscaEmpresa(codigo);
        BeanUtils.copyProperties(empresa, empresaSalva, "codigo");

        this.empresaRepository.save(empresaSalva);
        return ResponseEntity.ok(empresaSalva);
    }

    public Map<String, Empresa> loadEmpresas() {
        Map<String, Empresa> empresaMap = new HashMap<>();

        this.empresaRepository.findAll().forEach(emp -> {
            empresaMap.put(emp.getCnpj(), emp);
        });
        return empresaMap;
    }
}
