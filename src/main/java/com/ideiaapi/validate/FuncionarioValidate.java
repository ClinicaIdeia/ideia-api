package com.ideiaapi.validate;

import static com.ideiaapi.constansts.ErrorsCode.CPF_DUPLICADO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ideiaapi.exceptions.BusinessException;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.FuncionarioRepository;

@Component
public class FuncionarioValidate {

    @Autowired
    private FuncionarioRepository repository;

    public void fluxoCriacao(Funcionario entity) {
        this.validaCpfUnique(entity);
    }

    private void validaCpfUnique(Funcionario funcionario) {

        Funcionario saved = this.repository.findByCpf(funcionario.getCpf());

        if (null != saved) {
            throw new BusinessException(CPF_DUPLICADO);
        }
    }
}
