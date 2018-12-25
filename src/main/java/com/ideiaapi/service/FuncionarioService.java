package com.ideiaapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.FuncionarioRepository;
import com.ideiaapi.repository.filter.FuncionarioFilter;
import com.ideiaapi.repository.projection.ResumoFuncionario;
import com.ideiaapi.validate.FuncionarioValidate;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioValidate funcionarioValidate;

    public Page<Funcionario> filtrar(FuncionarioFilter filter, Pageable pageable) {
        return this.funcionarioRepository.filtrar(filter, pageable);

    }

    public Page<ResumoFuncionario> resumo(FuncionarioFilter filter, Pageable pageable) {

        return this.funcionarioRepository.resumir(filter, pageable);
    }

    public Funcionario cadastraFuncionario(Funcionario entity) {

        this.funcionarioValidate.fluxoCriacao(entity);
        return this.funcionarioRepository.save(entity);
    }

    public Funcionario buscaFuncionario(Long codigo) {
        Funcionario funcionario = this.funcionarioRepository.findOne(codigo);

        if (funcionario == null) {
            throw new EmptyResultDataAccessException(1);
        }

        return funcionario;
    }

    public void deletaFuncionario(Long codigo) {
        this.funcionarioRepository.delete(codigo);
    }

    public ResponseEntity<Funcionario> atualizaFuncionario(Long codigo, Funcionario funcionario) {
        Funcionario funcionarioSalva = this.buscaFuncionario(codigo);
        BeanUtils.copyProperties(funcionario, funcionarioSalva, "codigo");

        this.funcionarioRepository.save(funcionarioSalva);
        return ResponseEntity.ok(funcionarioSalva);
    }
}
