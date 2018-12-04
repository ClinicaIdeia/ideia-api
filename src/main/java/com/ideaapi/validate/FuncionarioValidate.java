package com.ideaapi.validate;

import static com.ideaapi.constansts.ErrorsCode.CPF_DUPLICADO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ideaapi.exceptions.BusinessException;
import com.ideaapi.model.Funcionario;
import com.ideaapi.repository.FuncionarioRepository;

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
