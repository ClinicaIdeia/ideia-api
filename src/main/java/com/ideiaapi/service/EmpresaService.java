package com.ideiaapi.service;

import com.ideiaapi.dto.EmpresaDTO;
import com.ideiaapi.model.Empresa;
import com.ideiaapi.repository.EmpresaRepository;
import com.ideiaapi.repository.filter.EmpresaFilter;
import com.ideiaapi.repository.projection.ResumoEmpresa;
import com.ideiaapi.validate.EmpresaValidate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository repository;

    @Autowired
    private ContatoService contatoService;

    @Autowired
    private EmpresaValidate empresaValidate;

    public Page<Empresa> filtrar(EmpresaFilter filter, Pageable pageable) {
        return this.repository.filtrar(filter, pageable);
    }

    public Page<ResumoEmpresa> resumo(EmpresaFilter filter, Pageable pageable) {
        return this.repository.resumir(filter, pageable);
    }

    public Empresa cadastraEmpresa(Empresa entity) {
        this.empresaValidate.fluxoCriacao(entity);

        if (entity.getAtiva() == null) {
            entity.setAtiva(true);
        }

        this.contatoService.cadastraContatos(entity);
        return this.repository.save(entity);
    }

    public Empresa buscaEmpresa(Long codigo) {
        Empresa empresa = this.repository.findOne(codigo);
        if (empresa != null) {
            return empresa;
        }

        return null;
    }

    public void deletaEmpresa(Long codigo) {
        this.repository.delete(codigo);
    }

    public ResponseEntity<Empresa> atualizaEmpresa(Long codigo, Empresa empresa) {
        Empresa empresaSalva = this.buscaEmpresa(codigo);
        BeanUtils.copyProperties(empresa, empresaSalva, "codigo");

        this.repository.save(empresaSalva);
        return ResponseEntity.ok(empresaSalva);
    }

    public Map<String, Empresa> loadEmpresas() {
        Map<String, Empresa> empresaMap = new HashMap<>();

        this.repository.findAll().forEach(emp -> {
            empresaMap.put(emp.getCnpj(), emp);
        });
        return empresaMap;
    }

    public List<Empresa> buscarTodas() {
        return this.repository.findAll().stream().sorted(Comparator.comparing(Empresa::getNome)).collect(
                Collectors.toList());
    }

    public List<EmpresaDTO> buscaEmpresaComAutoComplete(String nome) {

        List<EmpresaDTO> empresas = new ArrayList<>();
        this.repository.findByNomeContainingIgnoreCaseOrderByNomeAscCodigoDesc(nome)
                .forEach(func -> {
                    empresas.add(new EmpresaDTO(func.getCodigo(), func.getNome()));
                });
        return empresas;
    }
}
