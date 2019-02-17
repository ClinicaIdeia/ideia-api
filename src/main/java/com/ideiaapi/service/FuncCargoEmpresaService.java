package com.ideiaapi.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ideiaapi.model.Empresa;
import com.ideiaapi.model.FuncCargoEmpresa;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.FuncCargoEmpresaRepository;

@Service
public class FuncCargoEmpresaService {

    @Autowired
    private FuncCargoEmpresaRepository funcCargoEmpresaRepository;

    public FuncCargoEmpresa criar(FuncCargoEmpresa funcCargoEmpresa) {
        return this.funcCargoEmpresaRepository.save(funcCargoEmpresa);
    }

    public FuncCargoEmpresa atualizar(FuncCargoEmpresa funcCargoEmpresa) {
        return this.funcCargoEmpresaRepository.save(funcCargoEmpresa);
    }

    public FuncCargoEmpresa buscar(Long codigo) {
        return this.funcCargoEmpresaRepository.findOne(codigo);
    }

    public void deletar(Long codigo) {
        this.funcCargoEmpresaRepository.delete(codigo);
    }

    public List<FuncCargoEmpresa> buscaTodas() {
        return this.funcCargoEmpresaRepository.findAll();
    }

    public void insereCargo(Funcionario funcionario) {

        if (null != funcionario.getEmpresas() && !funcionario.getEmpresas().isEmpty()) {

            Collections.reverse(funcionario.getEmpresas());

            final Optional<Empresa> empresa = funcionario.getEmpresas().stream().findFirst();

            if (empresa.isPresent()) {

                final Long codEmpresa = empresa.get().getCodigo();
                final String cargo = funcionario.getCargo();

                FuncCargoEmpresa funcCargoEmpresa = this.getByCodFuncionarioAndCodEmpresa(funcionario, codEmpresa);

                if (null == funcCargoEmpresa) {

                    this.criar(new FuncCargoEmpresa(cargo, LocalDateTime.now(), LocalDateTime.now(),
                            funcionario.getCodigo(), codEmpresa));

                } else if (!cargo.equalsIgnoreCase(funcCargoEmpresa.getCargo())) {
                    funcCargoEmpresa.setCargo(cargo);
                    this.atualizar(funcCargoEmpresa);
                }
            }
        }

    }

    public FuncCargoEmpresa getByCodFuncionarioAndCodEmpresa(Funcionario funcionario, Long codEmpresa) {
        return this.funcCargoEmpresaRepository.findByCodFuncionarioAndCodEmpresa(
                funcionario.getCodigo(), codEmpresa);
    }
}
