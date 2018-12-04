package com.ideaapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ideaapi.model.Empresa;
import com.ideaapi.repository.EmpresaRepository;
import com.ideaapi.repository.filter.EmpresaFilter;
import com.ideaapi.repository.projection.ResumoEmpresa;
import com.ideaapi.validate.EmpresaValidate;

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

        Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();

//        if(authentication != null){
//            Object obj = authentication.getPrincipal();
//            if (obj instanceof Usuario){
//                return (Usuario) obj;
//            }
//        }

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
}
